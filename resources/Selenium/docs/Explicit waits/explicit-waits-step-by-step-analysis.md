# Analisis detallado de esperas explicitas en Dynamic Loading

## Alcance del analisis

Este proyecto automatiza la pagina Dynamic Loading de The Internet usando Serenity BDD con el patron Screenplay. La suite valida tres comportamientos:

- Example 1: el texto Hello World ya existe en el DOM, pero esta oculto antes de iniciar la carga.
- Example 2: el texto Hello World no existe en el DOM antes de iniciar la carga.
- Regla transversal: el loader debe desaparecer antes de considerar visible el resultado final.

La implementacion de las esperas explicitas esta centralizada en la clase `DynamicLoadingWaits` y todas las esperas detectadas son de Serenity BDD, no de Selenium puro.

## Inventario de esperas explicitas detectadas

### 1. Espera al boton Start listo para clic

- Ubicacion: `src/test/java/com/sofka/dynamicloading/interactions/DynamicLoadingWaits.java`
- Metodo: `untilStartButtonIsReady()`
- Tecnologia: Serenity BDD Screenplay
- Implementacion: `WaitUntil.the(DynamicLoadingPage.START_BUTTON, isClickable())`
- Elemento objetivo: boton Start, localizado como `#start button`
- Condicion: clickable
- Timeout maximo: 15 segundos
- Polling: no esta configurado manualmente en el proyecto; Serenity delega la verificacion periodica a la implementacion de espera subyacente de Selenium.
- Exito: cuando el boton es clickeable, la tarea `StartDynamicLoading.process()` ejecuta el clic.
- Timeout: si el boton nunca queda disponible, el paso falla y la prueba se detiene con una excepcion de timeout o fallo de espera reportada por Serenity/Selenium.

### 2. Espera a que el loader sea visible

- Ubicacion: `src/test/java/com/sofka/dynamicloading/interactions/DynamicLoadingWaits.java`
- Metodo: `untilLoaderIsVisible()`
- Tecnologia: Serenity BDD Screenplay
- Implementacion: `WaitUntil.the(DynamicLoadingPage.LOADER, isVisible())`
- Elemento objetivo: loader o spinner, localizado como `#loading`
- Condicion: visible
- Timeout maximo: 15 segundos
- Polling: no esta redefinido en el proyecto; se usa el mecanismo periodico por defecto de la libreria.
- Exito: confirma que el proceso de carga realmente comenzo.
- Timeout: si el loader no aparece dentro del tiempo esperado, la prueba falla porque no se puede demostrar el comportamiento dinamico.

### 3. Espera a que el loader desaparezca

- Ubicacion: `src/test/java/com/sofka/dynamicloading/interactions/DynamicLoadingWaits.java`
- Metodo: `untilLoaderDisappears()`
- Tecnologia: Serenity BDD Screenplay
- Implementacion: `WaitUntil.the(DynamicLoadingPage.LOADER, isNotVisible())`
- Elemento objetivo: loader o spinner, localizado como `#loading`
- Condicion: invisible / no visible
- Timeout maximo: 15 segundos
- Polling: no esta redefinido manualmente; Serenity vuelve a consultar el estado del elemento hasta que deja de ser visible o vence el timeout.
- Exito: habilita continuar al siguiente paso, que valida la aparicion del resultado final.
- Timeout: si el loader queda visible demasiado tiempo, la prueba falla por timeout y se interpreta como un problema de sincronizacion o de la aplicacion bajo prueba.

### 4. Espera a que Hello World sea visible

- Ubicacion: `src/test/java/com/sofka/dynamicloading/interactions/DynamicLoadingWaits.java`
- Metodo: `untilHelloWorldIsVisible()`
- Tecnologia: Serenity BDD Screenplay
- Implementacion: `WaitUntil.the(DynamicLoadingPage.FINISH_TEXT, isVisible())`
- Elemento objetivo: texto final, localizado como `#finish h4`
- Condicion: visible
- Timeout maximo: 15 segundos
- Polling: no esta configurado en el proyecto; la libreria verifica periodicamente la condicion hasta el exito o el timeout.
- Exito: el actor puede leer el texto y compararlo con `Hello World!`.
- Timeout: la prueba falla porque el resultado esperado no llego a hacerse visible.

## Esperas de Selenium puro detectadas

No se detectaron usos de `WebDriverWait`, `FluentWait` ni `ExpectedConditions` en el codigo fuente de pruebas. Eso significa que el proyecto eligio una sola estrategia de sincronizacion explicita: Serenity BDD Screenplay con `WaitUntil`.

## Uso de esperas fijas

No se detectaron llamadas a `Thread.sleep(...)` en el codigo fuente bajo `src/`. Esto es una buena señal de estabilidad, porque evita pausas rigidas e innecesarias.

## Flujo tecnico general de una espera en este proyecto

1. El test invoca una tarea o interaccion Screenplay.
2. La interaccion usa `WaitUntil` sobre un `Target` definido en `DynamicLoadingPage`.
3. Serenity empieza a consultar periodicamente el estado del elemento.
4. Si la condicion se cumple antes de 15 segundos, el flujo continua con la siguiente accion o validacion.
5. Si la condicion no se cumple antes de 15 segundos, Serenity marca el paso como fallido y JUnit reporta la prueba como fallida.

En lenguaje simple: la automatizacion no se queda esperando un tiempo fijo. En lugar de eso, revisa repetidamente si la pagina ya esta lista para el siguiente paso. Si la pagina responde rapido, la prueba sigue rapido. Si la pagina tarda demasiado, la prueba falla con evidencia.

---

# Test: shouldDisplayHelloWorldInExample1

## Que valida este test

Comprueba el comportamiento de Dynamic Loading Example 1. En este escenario, el mensaje final ya existe en la pagina desde el inicio, pero permanece oculto hasta que termina la carga.

## Paso 1: abrir la pagina del ejemplo 1

- Accion: `OpenDynamicLoadingExample.page(DynamicLoadingExample.EXAMPLE_1)`
- Elemento involucrado: pagina `https://the-internet.herokuapp.com/dynamic_loading/1`
- Espera usada: ninguna espera explicita en este paso
- Resultado esperado: la pagina abre correctamente y deja preparado el boton Start

## Paso 2: validar el estado inicial del mensaje

- Accion: `HelloWorldPresence.inTheDom()` y `HelloWorldVisibility.displayed()`
- Elemento: `#finish h4`
- Espera usada: no hay espera explicita; son consultas directas al estado del DOM y de visibilidad
- Condicion observada: el elemento existe en el DOM, pero no es visible
- Resultado esperado: presencia `true` y visibilidad `false`

## Paso 3: esperar que el boton Start sea clickeable y hacer clic

- Elemento: boton Start `#start button`
- Espera usada: `WaitUntil`
- Condicion: clickable
- Timeout: 15 segundos
- Resultado esperado: cuando el boton esta listo, Serenity ejecuta el clic dentro de `StartDynamicLoading.process()`
- Resultado timeout: falla el paso de inicio y no se dispara la carga dinamica

## Paso 4: esperar que el loader aparezca

- Elemento: loader `#loading`
- Espera usada: `WaitUntil`
- Condicion: visible
- Timeout: 15 segundos
- Resultado esperado: se confirma que la carga empezo realmente
- Resultado timeout: la prueba falla porque no se observa el comportamiento de carga esperado

## Paso 5: esperar que el loader desaparezca

- Elemento: loader `#loading`
- Espera usada: `WaitUntil`
- Condicion: invisible / no visible
- Timeout: 15 segundos
- Resultado esperado: el flujo continua solo cuando la carga termina
- Resultado timeout: la prueba falla porque la aplicacion parece no completar el proceso

## Paso 6: esperar que Hello World sea visible

- Elemento: texto final `#finish h4`
- Espera usada: `WaitUntil`
- Condicion: visible
- Timeout: 15 segundos
- Resultado esperado: el mensaje se hace visible y se puede validar el texto
- Resultado timeout: la prueba falla porque el mensaje no aparece a tiempo

## Paso 7: validar el texto final

- Accion: `HelloWorldText.displayed()`
- Elemento: `#finish h4`
- Espera usada: no hay una nueva espera; se apoya en la espera del paso anterior
- Resultado esperado: el texto sea exactamente `Hello World!`

## Diferencia entre Selenium puro y Serenity en este test

- Serenity BDD: encapsula la espera en un metodo legible (`untilLoaderDisappears()`) y la integra al flujo Screenplay.
- Selenium puro: requeriria algo similar a construir manualmente `new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.visibilityOfElementLocated(...))`.
- En este proyecto se eligio Serenity, por lo que el test resulta mas declarativo y mas facil de leer.

## Evaluacion

- Buenas practicas aplicadas: uso de espera dinamica, ausencia de `Thread.sleep`, espera encapsulada y reutilizable, validacion del estado inicial del DOM.
- Posibles mejoras: documentar el polling esperado; parametrizar el timeout para no dejarlo fijo en codigo; evitar capturar `Exception` generica en algunas preguntas porque eso puede ocultar errores reales.

---

# Test: shouldRenderHelloWorldInExample2

## Que valida este test

Comprueba Dynamic Loading Example 2. En este escenario, el mensaje final no existe en el DOM al principio; aparece solo despues del proceso de carga.

## Paso 1: abrir la pagina del ejemplo 2

- Accion: `OpenDynamicLoadingExample.page(DynamicLoadingExample.EXAMPLE_2)`
- Elemento involucrado: pagina `https://the-internet.herokuapp.com/dynamic_loading/2`
- Espera usada: ninguna espera explicita en este paso
- Resultado esperado: la pagina abre correctamente

## Paso 2: validar que Hello World aun no existe en el DOM

- Accion: `HelloWorldPresence.inTheDom()`
- Elemento: `#finish h4`
- Espera usada: no hay espera explicita
- Condicion observada: el elemento todavia no existe
- Resultado esperado: presencia `false`

## Paso 3: esperar que el boton Start sea clickeable y hacer clic

- Elemento: boton Start `#start button`
- Espera usada: `WaitUntil`
- Condicion: clickable
- Timeout: 15 segundos
- Resultado esperado: se inicia la carga dinamica
- Resultado timeout: la prueba falla antes de iniciar el flujo principal

## Paso 4: esperar que el loader aparezca

- Elemento: loader `#loading`
- Espera usada: `WaitUntil`
- Condicion: visible
- Timeout: 15 segundos
- Resultado esperado: evidencia de que la pagina esta generando el contenido
- Resultado timeout: la prueba falla porque no aparece la senal visual de procesamiento

## Paso 5: esperar que el loader desaparezca

- Elemento: loader `#loading`
- Espera usada: `WaitUntil`
- Condicion: invisible / no visible
- Timeout: 15 segundos
- Resultado esperado: indica que la generacion del contenido termino
- Resultado timeout: la prueba falla por procesamiento excesivamente lento o bloqueado

## Paso 6: esperar que Hello World sea visible

- Elemento: texto final `#finish h4`
- Espera usada: `WaitUntil`
- Condicion: visible
- Timeout: 15 segundos
- Resultado esperado: el elemento ahora existe y ademas ya es visible para el usuario
- Resultado timeout: la prueba falla porque el contenido no se renderiza a tiempo

## Paso 7: validar presencia y texto final

- Accion: `HelloWorldPresence.inTheDom()` y `HelloWorldText.displayed()`
- Elemento: `#finish h4`
- Espera usada: no hay una nueva espera explicita; la estabilidad la aporta el paso 6
- Resultado esperado: presencia `true` y texto `Hello World!`

## Diferencia entre Selenium puro y Serenity en este test

- Serenity BDD: expresa la intencion de negocio como pasos legibles y reutilizables.
- Selenium puro: haria la misma validacion con `WebDriverWait` y `ExpectedConditions`, pero con un nivel mayor de detalle tecnico dentro del propio test.
- Este test muestra claramente una ventaja de Serenity: la espera se lee como una accion de negocio y no como una instruccion de infraestructura.

## Evaluacion

- Buenas practicas aplicadas: comprobacion previa de ausencia en DOM, espera dinamica antes de validar el resultado, reutilizacion de metodos de espera.
- Posibles mejoras: separar el timeout en configuracion externa; registrar de forma mas explicita el comportamiento esperado del polling si el equipo necesita auditorias mas profundas.

---

# Test: shouldHideLoaderBeforeShowingHelloWorld

## Que valida este test

Confirma la regla de sincronizacion mas importante de la pantalla: el loader debe desaparecer antes de considerar estable la aparicion del texto final.

## Paso 1: abrir la pagina del ejemplo 1

- Accion: `OpenDynamicLoadingExample.page(DynamicLoadingExample.EXAMPLE_1)`
- Espera usada: ninguna espera explicita en este paso
- Resultado esperado: se carga el escenario inicial

## Paso 2: esperar que el boton Start sea clickeable y hacer clic

- Elemento: boton Start `#start button`
- Espera usada: `WaitUntil`
- Condicion: clickable
- Timeout: 15 segundos
- Resultado esperado: arranca el proceso de carga
- Resultado timeout: la prueba falla antes de iniciar el escenario dinamico

## Paso 3: esperar que el loader sea visible

- Elemento: loader `#loading`
- Espera usada: `WaitUntil`
- Condicion: visible
- Timeout: 15 segundos
- Resultado esperado: se detecta que el spinner esta mostrando el proceso en curso
- Resultado timeout: la prueba falla porque no llega a verse el loader

## Paso 4: esperar que el loader desaparezca

- Elemento: loader `#loading`
- Espera usada: `WaitUntil`
- Condicion: invisible / no visible
- Timeout: 15 segundos
- Resultado esperado: el spinner deja de estar visible antes de seguir
- Resultado timeout: la prueba falla y sugiere un problema de sincronizacion o de rendimiento

## Paso 5: esperar que Hello World sea visible

- Elemento: texto final `#finish h4`
- Espera usada: `WaitUntil`
- Condicion: visible
- Timeout: 15 segundos
- Resultado esperado: el texto final se presenta al usuario
- Resultado timeout: la prueba falla porque el resultado final no quedo estable

## Paso 6: validar estado final de loader y resultado

- Accion: `LoaderVisibility.displayed()`, `HelloWorldVisibility.displayed()` y `HelloWorldText.displayed()`
- Elementos: `#loading` y `#finish h4`
- Espera usada: no se agrega una espera nueva; se verifica el estado despues del flujo sincronizado
- Resultado esperado: loader `false`, visibilidad de Hello World `true`, texto `Hello World!`

## Diferencia entre Selenium puro y Serenity en este test

- Serenity BDD: permite narrar una regla de negocio con pasos reutilizables y muy legibles.
- Selenium puro: obligaria a escribir varias esperas y validaciones en bajo nivel dentro del test.
- En este caso, Serenity aporta una lectura mas cercana a la especificacion funcional.

## Evaluacion

- Buenas practicas aplicadas: la prueba valida directamente la relacion entre desaparicion del loader y visibilidad del resultado, que es una regla de sincronizacion critica.
- Posibles mejoras: como en los otros tests, el timeout es unico y fijo; si la aplicacion creciera, podria ser util diferenciar tiempos por tipo de paso.

---

## Evaluacion global de la implementacion

### Buenas practicas cumplidas

- Las esperas son dinamicas y explicitas.
- No se usa `Thread.sleep`, lo que reduce fragilidad y tiempos muertos.
- La logica de espera esta abstraida en una clase dedicada (`DynamicLoadingWaits`).
- Los tests son legibles: primero describen el escenario y luego consumen pasos reutilizables.
- La suite valida tanto visibilidad como presencia en DOM, lo que mejora el valor del analisis.

### Posibles problemas o puntos de atencion

- No hay implementaciones de Selenium puro en el proyecto. Si el objetivo pedagogico era comparar Selenium puro contra Serenity, hoy solo existe la variante Serenity.
- El polling no esta configurado ni documentado explicitamente. Funciona, pero queda implcito en la libreria.
- Todas las esperas usan el mismo timeout de 15 segundos. Es simple, pero no muy flexible.
- Algunas preguntas capturan `Exception` generica y devuelven `false`. Eso evita fallos abruptos, pero puede esconder problemas reales de localizador o de pagina.
- Encadenar varias esperas de 15 segundos puede producir tiempos de fallo relativamente largos si el sitio deja de responder.

## Resumen del proyecto

- Nivel de estabilidad de las pruebas: bueno. La estrategia de sincronizacion es consistente, reutilizable y ya fue verificada con tres pruebas exitosas.
- Nivel de legibilidad: bueno. La combinacion de Serenity Screenplay y una clase dedicada a esperas hace que el flujo se lea casi como una historia de negocio.
- Riesgo principal actual: la suite depende por completo de una unica abstraccion de espera y no documenta internamente detalles como polling o diferenciacion de timeouts.

## Recomendaciones generales

1. Mantener `WaitUntil` como estrategia principal, porque esta bien abstraida y es facil de mantener.
2. Llevar el timeout de 15 segundos a configuracion externa o a constantes por tipo de espera.
3. Documentar en el equipo que el polling lo maneja Serenity/Selenium y no esta sobreescrito en el proyecto.
4. Evitar capturar `Exception` generica en las preguntas cuando se necesite diagnostico mas fino.
5. Si el objetivo del ejemplo es didactico, agregar un test equivalente con `WebDriverWait` y `ExpectedConditions` para mostrar la diferencia con Serenity BDD.

## Evidencia de ejecucion revisada

La suite fue ejecutada con `./gradlew clean test --console=plain` y las tres pruebas pasaron satisfactoriamente. Esto respalda que la implementacion actual de esperas explicitas es funcional para los escenarios cubiertos.
