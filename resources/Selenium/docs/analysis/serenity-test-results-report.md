**Informe de Resultados de Pruebas — Serenity BDD + Selenium**

- **Resumen**: La suite de UI ejecutó 3 pruebas (DynamicLoadingTests) usando Serenity Screenplay y Selenium en Chrome. Todas pasaron (3/3) y el reporte generado está en `target/site/serenity/index.html`.

- **Dónde ver el resultado**: Abra el reporte HTML generado en:
  - Archivo: [target/site/serenity/index.html](target/site/serenity/index.html)
  - Comando para regenerar: `./gradlew clean test` (desde la raíz del proyecto).

- **Duración de las pruebas (ejecución registrada)**:
  - Total: 50s
  - Promedio: 16s
  - Más rápida: 15s
  - Más lenta: 17s
  (Estos valores están en el encabezado del reporte HTML y en la sección "Key Statistics".)

- **Cobertura frente a requisitos de negocio**:
  - Caso 1 (Example 1): validado — el mensaje existe en DOM pero está oculto inicialmente y luego se muestra.
  - Caso 2 (Example 2): validado — el mensaje no existe en DOM antes de iniciar y se renderiza después.
  - Caso 3: validado — el loader se muestra durante la carga y desaparece antes de la validación final.
  - Documento de negocio: [docs/business/dynamic-loading-business.md](docs/business/dynamic-loading-business.md)
  - Documentación viva alineada: [src/test/resources/features/dynamic_loading_documentation.feature](src/test/resources/features/dynamic_loading_documentation.feature)

- **Comprobación de las explicit waits**:
  - Implementación centralizada en: [src/test/java/com/sofka/dynamicloading/interactions/DynamicLoadingWaits.java](src/test/java/com/sofka/dynamicloading/interactions/DynamicLoadingWaits.java) — contiene `untilStartButtonIsReady()`, `untilLoaderIsVisible()`, `untilLoaderDisappears()` y `untilHelloWorldIsVisible()`.
  - Uso en tareas/tests:
    - La acción de inicio usa `StartDynamicLoading.process()` (click protegido por `untilStartButtonIsReady()`). [src/test/java/com/sofka/dynamicloading/tasks/StartDynamicLoading.java](src/test/java/com/sofka/dynamicloading/tasks/StartDynamicLoading.java)
    - Los tests usan las esperas reutilizables y además nuevas preguntas para validar presencia/visibilidad (`HelloWorldPresence`, `HelloWorldVisibility`).
      - [src/test/java/com/sofka/dynamicloading/questions/HelloWorldPresence.java](src/test/java/com/sofka/dynamicloading/questions/HelloWorldPresence.java)
      - [src/test/java/com/sofka/dynamicloading/questions/HelloWorldVisibility.java](src/test/java/com/sofka/dynamicloading/questions/HelloWorldVisibility.java)
    - Pruebas agregadas/reforzadas en: [src/test/java/com/sofka/dynamicloading/tests/DynamicLoadingTests.java](src/test/java/com/sofka/dynamicloading/tests/DynamicLoadingTests.java)

- **Interpretación práctica del uso de explicit waits**:
  - Patrón: las condiciones de sincronización se encapsularon en `interactions/DynamicLoadingWaits`, evitando `Thread.sleep()` y duplicación.
  - Robustez: las esperas cubren el ciclo completo — botón clickable, loader visible, loader desaparece, contenido visible — reduciendo flakiness.
  - Recomendación: considerar un polling interval o tiempo máximo por escenario configurable si los tiempos del entorno varían (ser útil para CI lento).

- **Acciones sugeridas / próximos pasos**:
  1. Publicar el reporte HTML en un servidor de artefactos o en CI para acceso centralizado.
  2. Añadir un escenario negativo (timeout) que valide el comportamiento cuando la carga no completa dentro del timeout.
  3. Convertir el `.feature` en BDD ejecutable (Cucumber) si se necesita trazabilidad directa de negocio ↔ ejecución.

- **Archivos clave para revisar**:
  - [src/test/java/com/sofka/dynamicloading/interactions/DynamicLoadingWaits.java](src/test/java/com/sofka/dynamicloading/interactions/DynamicLoadingWaits.java)
  - [src/test/java/com/sofka/dynamicloading/tests/DynamicLoadingTests.java](src/test/java/com/sofka/dynamicloading/tests/DynamicLoadingTests.java)
  - [src/test/java/com/sofka/dynamicloading/questions/HelloWorldPresence.java](src/test/java/com/sofka/dynamicloading/questions/HelloWorldPresence.java)
  - [src/test/java/com/sofka/dynamicloading/questions/HelloWorldVisibility.java](src/test/java/com/sofka/dynamicloading/questions/HelloWorldVisibility.java)
  - Reporte generado: [target/site/serenity/index.html](target/site/serenity/index.html)

---

Generado automáticamente: si quieres, puedo:

- Adjuntar este informe al README o crear una entrada en `docs/` con más detalles.
- Añadir un test negativo de timeout y ejecutar nuevamente la suite.
