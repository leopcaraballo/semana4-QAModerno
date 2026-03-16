# Informe tecnico de estructura del proyecto

## Objetivo

Este documento describe la estructura completa del proyecto, el proposito de cada directorio y archivo principal, la forma en que se relacionan entre si y el motivo de su existencia dentro de la solucion.

El proyecto no es una aplicacion Java tradicional con codigo de negocio en `src/main`. Es una suite de automatizacion UI construida con Gradle, Serenity BDD, Selenium WebDriver, Screenplay y JUnit 5.

## Tree general del proyecto

```text
.
|- .gitignore
|- .classpath
|- .project
|- .settings/
|  |- org.eclipse.buildship.core.prefs
|  `- org.eclipse.jdt.core.prefs
|- .vscode/
|  |- settings.json
|  `- tasks.json
|- .gradle/
|  |- 9.2.0/
|  |- 9.4.0/
|  |- buildOutputCleanup/
|  |- file-system.probe
|  `- vcs-1/
|- README.md
|- build.gradle
|- settings.gradle
|- serenity.properties
|- gradlew
|- gradlew.bat
|- gradle/
|  `- wrapper/
|     |- gradle-wrapper.jar
|     `- gradle-wrapper.properties
|- docs/
|  |- architecture/
|  |  |- dynamic-loading-framework-audit.md
|  |  `- project-structure-analysis.md
|  `- business/
|     `- dynamic-loading-business.md
|- src/
|  `- test/
|     |- java/com/sofka/dynamicloading/
|     |  |- interactions/DynamicLoadingWaits.java
|     |  |- models/DynamicLoadingExample.java
|     |  |- questions/HelloWorldText.java
|     |  |- questions/LoaderVisibility.java
|     |  |- tasks/OpenDynamicLoadingExample.java
|     |  |- tasks/StartDynamicLoading.java
|     |  |- tests/DynamicLoadingTests.java
|     |  `- ui/DynamicLoadingPage.java
|     `- resources/features/dynamic_loading_documentation.feature
|- bin/
|  `- test/
|     |- com/sofka/dynamicloading/...
|     `- features/dynamic_loading_documentation.feature
|- build/
|  |- classes/
|  |- generated/
|  |- reports/
|  |- resources/
|  |- test-results/
|  `- tmp/
|- target/
|  `- site/
|     `- serenity/
`- history/
```

## Vision general de la arquitectura

La estructura sigue este flujo:

1. Gradle define como compilar y ejecutar la suite.
2. Serenity y Selenium controlan el navegador y generan evidencias.
3. Screenplay separa responsabilidades entre localizadores, acciones, esperas y consultas.
4. JUnit 5 orquesta la ejecucion de los escenarios automatizados.
5. Gradle y Serenity producen artefactos de salida en `build/`, `bin/` y `target/`.

## Archivos raiz

### `build.gradle`

Es el archivo central del build.

- Declara los plugins `java`, `idea` y `net.serenity-bdd.serenity-gradle-plugin`.
- Define `group`, `version` y compatibilidad Java.
- Resuelve dependencias desde `mavenCentral()`.
- Declara dependencias de Serenity, Screenplay, Selenium y JUnit 5.
- Configura la tarea `test` para usar JUnit Platform.

Por que existe:

- Sin este archivo, Gradle no sabria como compilar, ejecutar ni de donde descargar dependencias.

Como se relaciona:

- Controla la compilacion del codigo en `src/test/java`.
- Alimenta la ejecucion que luego genera `build/` y `target/site/serenity/`.

### `settings.gradle`

Define el nombre logico del proyecto: `dynamic-loading-serenity`.

Por que existe:

- Permite a Gradle identificar el build y estructurar la importacion del proyecto.

### `serenity.properties`

Configura Serenity y WebDriver.

- Navegador: `chrome`
- Autodescarga de driver: `true`
- Nombre del proyecto para reportes
- Capturas de pantalla despues de cada paso

Por que existe:

- Separa configuracion operativa del codigo Java.

Como se relaciona:

- Influye en la ejecucion de pruebas y en el contenido del reporte Serenity.

### `README.md`

Es la puerta de entrada documental del proyecto.

- Resume estructura, tecnologias, ejecucion y objetivos de prueba.

Por que existe:

- Permite a cualquier miembro del equipo entender rapido el contexto del repositorio.

### `gradlew` y `gradlew.bat`

Son scripts del Gradle Wrapper para Linux/macOS y Windows.

Por que existen:

- Garantizan que el proyecto use una version conocida de Gradle sin depender de una instalacion global.

Como se relacionan:

- Ejecutan el wrapper ubicado en `gradle/wrapper/`.

## Carpeta `gradle/wrapper/`

### `gradle-wrapper.jar`

Es el bootstrap que permite descargar y arrancar la version de Gradle definida.

### `gradle-wrapper.properties`

Define la distribucion a usar. En este proyecto apunta a Gradle 9.4.0.

Por que existe:

- Hace reproducible el entorno de build.

## Carpeta `docs/`

Contiene documentacion persistente del proyecto.

### `docs/architecture/`

Agrupa documentacion tecnica y de arquitectura.

#### `dynamic-loading-framework-audit.md`

Describe una auditoria del framework: cohesion, acoplamiento, SOLID, esperas explicitas, mantenibilidad y plan de refactorizacion.

#### `project-structure-analysis.md`

Este documento.

Su proposito es conservar una explicacion estable de la estructura del repositorio y sus relaciones internas.

### `docs/business/`

Agrupa documentacion funcional.

#### `dynamic-loading-business.md`

Explica las pruebas desde perspectiva de negocio, no desde detalle tecnico.

Por que existe:

- Ayuda a traducir la automatizacion a lenguaje funcional comprensible para QA, analistas o negocio.

## Carpeta `src/`

Es la fuente real del proyecto.

En este caso contiene solo `src/test/`, porque el repositorio representa una suite de pruebas y no una aplicacion productiva con codigo de dominio propio.

### `src/test/java/com/sofka/dynamicloading/`

Es el paquete principal del framework de automatizacion.

Se divide por responsabilidades siguiendo Screenplay.

#### `ui/`

##### `DynamicLoadingPage.java`

Centraliza:

- La URL base del modulo probado.
- Los `Target` que representan elementos de la UI.

Por que existe:

- Evita repetir selectores en varias clases.
- Facilita mantenimiento cuando cambia el DOM.

Como se relaciona:

- Es usado por `tasks`, `questions` e `interactions`.

#### `models/`

##### `DynamicLoadingExample.java`

Es un `enum` que representa variantes de la pagina (`EXAMPLE_1` y `EXAMPLE_2`).

Por que existe:

- Expresa el dominio de prueba como datos tipados.
- Evita usar strings dispersos para rutas o URLs.

Como se relaciona:

- `OpenDynamicLoadingExample` depende de este enum para decidir que pagina abrir.

#### `interactions/`

##### `DynamicLoadingWaits.java`

Agrupa las esperas explicitas reutilizables.

- Espera a que el boton Start sea clickeable.
- Espera a que el loader desaparezca.
- Espera a que el texto final sea visible.

Por que existe:

- Separa sincronizacion tecnica del flujo funcional.
- Evita `Thread.sleep()`.
- Reduce flaky tests.

Como se relaciona:

- `StartDynamicLoading` usa la espera previa al click.
- `DynamicLoadingTests` usa las esperas para validar el comportamiento asincrono.

#### `tasks/`

Las tareas representan acciones de negocio realizadas por el actor.

##### `OpenDynamicLoadingExample.java`

Abre la pagina del ejemplo solicitado.

Por que existe:

- Encapsula la navegacion como una accion del actor.

Como se relaciona:

- Usa `DynamicLoadingExample` para construir la URL.
- Es invocada desde `DynamicLoadingTests`.

##### `StartDynamicLoading.java`

Hace click sobre el boton Start despues de esperar que este listo.

Por que existe:

- Encapsula el inicio del proceso asincrono como una accion reusable.

Como se relaciona:

- Usa `DynamicLoadingWaits` y `DynamicLoadingPage`.
- Es invocada desde `DynamicLoadingTests`.

#### `questions/`

Las preguntas consultan el estado de la interfaz sin modificarla.

##### `HelloWorldText.java`

Obtiene el texto visible del resultado final.

Por que existe:

- Separa lectura y validacion de las acciones.

##### `LoaderVisibility.java`

Consulta si el loader sigue visible.

Por que existe:

- Permite validar el estado transitorio del proceso asincrono.

Como se relacionan las `questions`:

- Ambas usan los `Target` definidos en `DynamicLoadingPage`.
- Ambas son consumidas por `DynamicLoadingTests` mediante `asksFor(...)`.

#### `tests/`

##### `DynamicLoadingTests.java`

Es la clase orquestadora de la suite.

Responsabilidades:

- Crear el actor de Screenplay.
- Asignarle la habilidad de navegar con `BrowseTheWeb`.
- Ejecutar tareas e interacciones.
- Realizar las validaciones finales.

Por que existe:

- Es el punto de entrada ejecutable de JUnit 5.

Como se relaciona:

- Une `models`, `tasks`, `interactions` y `questions` en escenarios concretos.

### `src/test/resources/features/`

##### `dynamic_loading_documentation.feature`

Describe los escenarios en formato Gherkin.

Por que existe:

- Actua como documentacion viva del comportamiento esperado.

Importante:

- En el estado actual del proyecto no gobierna la ejecucion como un BDD ejecutable con Cucumber.
- Sirve como referencia funcional, no como runner principal.

## Carpetas generadas por el IDE y el build

### `bin/`

Contiene salida de compilacion generada por Eclipse/JDT.

- Clases compiladas bajo `bin/test/com/sofka/dynamicloading/`
- Recursos copiados bajo `bin/test/features/`

Por que existe:

- El tooling Java del IDE usa estas salidas para analisis, importacion y algunas ejecuciones locales.

Importante:

- No es la salida oficial de Gradle.

### `build/`

Es la salida oficial generada por Gradle.

#### `build/classes/`

Contiene bytecode compilado.

#### `build/resources/`

Contiene recursos copiados, como el archivo `.feature`.

#### `build/generated/`

Reserva espacio para fuentes o cabeceras generadas.

#### `build/tmp/`

Guarda estado temporal de tareas del build.

#### `build/test-results/`

Contiene resultados XML de las pruebas.

Archivo representativo:

- `TEST-com.sofka.dynamicloading.tests.DynamicLoadingTests.xml`

Que aporta:

- Numero de pruebas ejecutadas.
- Fallos, errores y tiempos.
- Salida estandar y advertencias tecnicas.

#### `build/reports/`

Contiene el reporte HTML estandar de Gradle.

Por que existe:

- Ofrece una vista minima y rapida de los resultados sin depender de Serenity.

## Carpeta `target/site/serenity/`

Contiene el reporte enriquecido de Serenity.

Es la carpeta de evidencia funcional mas importante despues de ejecutar la suite.

### Tipos de archivos que aparecen aqui

- `index.html`: dashboard principal del reporte.
- `SERENITY-JUNIT-*.xml`: resultados por prueba/escenario.
- `*.json`: detalle estructurado de pasos, tiempos, actor y evidencias.
- `*_screenshots.html`: pagina de capturas asociadas al escenario.
- `*.png`: screenshots reales.
- carpetas `css/`, `scripts/`, `icons/`, `chartjs/`, `jqueryui/`, etc.: recursos estaticos del frontend del reporte.

Por que existe:

- Permite trazabilidad visual y tecnica de lo que ocurrio en cada prueba.

Como se relaciona:

- Se alimenta de la ejecucion de `DynamicLoadingTests`.
- Toma configuracion desde `serenity.properties`.
- Convierte pasos de Screenplay en reportes navegables.

## Carpetas auxiliares

### `.vscode/`

Contiene configuracion local de VS Code.

#### `settings.json`

Fija el JDK para importacion Gradle y Java Language Server.

#### `tasks.json`

Define tareas ejecutables desde el editor para correr `./gradlew clean test` con el `JAVA_HOME` correcto.

Por que existe:

- Hace reproducible el entorno de trabajo dentro de VS Code.

### `.settings/`, `.classpath`, `.project`

Son metadatos del ecosistema Eclipse/JDT/Buildship.

Por que existen:

- Soportan la importacion Java del IDE, classpath y nivel de compilacion.

### `.gradle/`

Es cache local de Gradle.

Por que existe:

- Mejora rendimiento del build.
- Guarda historico tecnico de tareas, hashes y estado incremental.

### `history/`

Actualmente esta vacia.

Interpretacion:

- No participa en la ejecucion actual.
- Puede haber sido creada para evidencia historica futura o uso manual.

## Relacion completa entre componentes

La relacion interna del proyecto puede resumirse asi:

1. `build.gradle` define el stack tecnico y la ejecucion.
2. `serenity.properties` ajusta navegador, capturas y comportamiento del reporte.
3. `DynamicLoadingPage` expone URL y localizadores.
4. `DynamicLoadingExample` modela que variante del flujo se quiere abrir.
5. `OpenDynamicLoadingExample` navega a la pagina correcta.
6. `StartDynamicLoading` inicia la accion del usuario.
7. `DynamicLoadingWaits` sincroniza el flujo con el DOM dinamico.
8. `HelloWorldText` y `LoaderVisibility` leen el estado visible.
9. `DynamicLoadingTests` compone todo eso en pruebas JUnit 5.
10. Gradle genera artefactos de compilacion y resultados en `build/`.
11. Serenity genera evidencia navegable en `target/site/serenity/`.

## Conclusion tecnica

La estructura del proyecto esta bien separada para una suite UI pequena y mantenible.

Fortalezas principales:

- Separacion clara por responsabilidades.
- Uso correcto de Screenplay.
- Esperas explicitas centralizadas.
- Documentacion tecnica y funcional existente.
- Reporteria enriquecida con Serenity.

Limitaciones actuales:

- No existe `src/main` porque el repositorio solo representa una suite de automatizacion.
- El archivo `.feature` documenta, pero no gobierna la ejecucion como BDD ejecutable real.
- Existen artefactos duplicados por tooling (`bin/`) y por build (`build/`), lo cual es normal, pero conviene entender su diferencia.

En resumen, el proyecto esta organizado para automatizar y evidenciar el comportamiento de una pagina de carga dinamica, con foco en estabilidad, mantenibilidad y trazabilidad de pruebas.
