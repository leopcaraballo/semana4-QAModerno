# Dynamic Loading con Serenity Screenplay

Proyecto de automatizacion en Java para demostrar el uso de esperas explicitas con Selenium WebDriver y Serenity BDD sobre el sitio `https://the-internet.herokuapp.com/dynamic_loading`.

## Estructura del proyecto

```text
.
|-- build.gradle
|-- serenity.properties
|-- settings.gradle
|-- docs/
|   |-- architecture/
|   |   `-- dynamic-loading-framework-audit.md
|   `-- business/
|       `-- dynamic-loading-business.md
`-- src/
    `-- test/
        |-- java/
        |   `-- com/sofka/dynamicloading/
        |       |-- interactions/
        |       |   `-- DynamicLoadingWaits.java
        |       |-- models/
        |       |   `-- DynamicLoadingExample.java
        |       |-- questions/
        |       |   |-- HelloWorldText.java
        |       |   `-- LoaderVisibility.java
        |       |-- tasks/
        |       |   |-- OpenDynamicLoadingExample.java
        |       |   `-- StartDynamicLoading.java
        |       |-- tests/
        |       |   `-- DynamicLoadingTests.java
        |       `-- ui/
        |           `-- DynamicLoadingPage.java
        `-- resources/
            `-- features/
                `-- dynamic_loading_documentation.feature
```

## Tecnologias

- Java 21
- Gradle 9.4.0 Wrapper
- Serenity BDD
- Selenium WebDriver
- Screenplay Pattern
- JUnit 5

## Ejecucion

1. Verifica que tengas Google Chrome instalado.
2. Usa un JDK completo con `javac` disponible. La validacion del proyecto se realizo con `JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64`.
3. Desde VS Code ejecuta la tarea `gradle-test` o corre `./gradlew clean test` en la raiz del proyecto.
4. Abre el reporte HTML en `target/site/serenity/index.html`.

## Que valida cada prueba

- Test 1: espera a que el mensaje `Hello World!` sea visible en el ejemplo 1.
- Test 2: espera a que el contenido dinamico del ejemplo 2 termine de renderizarse.
- Test 3: espera a que el loader desaparezca antes de validar el mensaje final.

## Practicas aplicadas

- Uso de `Target` para locators mantenibles.
- Uso de `WaitUntil` encapsulado en interacciones reutilizables para sincronizacion explicita.
- Sin `Thread.sleep()`.
- Separacion por `ui`, `interactions`, `tasks`, `questions`, `models` y `tests`.
- Apertura parametrizada de ejemplos para evitar duplicacion.

## Arquitectura mejorada

- `tasks`: representan acciones de negocio como abrir un ejemplo o iniciar la carga.
- `interactions`: encapsulan detalles tecnicos de sincronizacion explicita.
- `questions`: consultan el estado visible de la aplicacion sin mezclar acciones.
- `models`: concentran datos del dominio de prueba, como las variantes del ejercicio.

La evaluacion completa del framework y el plan de mejora estan documentados en `docs/architecture/dynamic-loading-framework-audit.md`.

## Notas operativas

- El proyecto compila con compatibilidad Java 21.
- En este entorno, las rutas Java 21 instaladas no incluian `javac`, por eso la ejecucion validada usa un JDK completo Java 25.
- El wrapper de Gradle ya esta versionado en el proyecto para evitar dependencia del Gradle global.
