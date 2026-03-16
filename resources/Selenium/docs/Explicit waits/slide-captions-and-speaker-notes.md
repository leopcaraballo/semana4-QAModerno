# Captions y Notas del Ponente — Esperas Explícitas

Duración recomendada: 10 slides + demo (≈10–12 min talk + 3–5 min demo). Cada slide: 60–90s; demo: 3–5 min.

---

## Slide 1 — Título

- Caption: "Esperas Explícitas en Selenium con Java y Serenity BDD"
- Nota del ponente: Presentación rápida del tema y objetivos: entender por qué usar explicit waits, cómo funcionan y cómo aplicarlos con Serenity + Screenplay.
- Imagen sugerida: `docs/Explicit waits/slides/slide1.png`

---

## Slide 2 — Stack tecnológico

- Caption: "Java · Selenium · Serenity · Screenplay · Gradle · VS Code"
- Nota del ponente: Explicar brevemente el rol de cada componente (Serenity abstrae WebDriver y ofrece reporting; Screenplay organiza la lógica de pruebas).
- Imagen sugerida: `docs/Explicit waits/slides/slide2.png`

---

## Slide 3 — El problema de sincronización en UI

- Caption: "Loaders, DOM dinámico y errores de sincronía"
- Nota del ponente: Mostrar un ejemplo real (Start → loader → elemento aparece). Enumerar síntomas: NoSuchElementException, elementos invisibles.
- Imagen sugerida: `docs/Explicit waits/slides/slide3.png`

---

## Slide 4 — Por qué no usar `Thread.sleep()`

- Caption: "Sleep = tiempo desperdiciado, tests frágiles"
- Nota del ponente: Comparar tiempo fijo vs. tiempo necesario. Ejemplificar impacto en CI (miles de segundos desperdiciados si se abusa de sleeps).
- Imagen sugerida: `docs/Explicit waits/slides/slide4.png`

---

## Slide 5 — Métodos Java que no aplican a UI

- Caption: "wait()/notify(), join(), yield() — para hilos, no UI"
- Nota del ponente: Aclarar la diferencia entre sincronización entre hilos y sincronización con el DOM. Evitar confusión conceptual.
- Imagen sugerida: `docs/Explicit waits/slides/slide5.png`

---

## Slide 6 — ¿Qué es una espera explícita?

- Caption: "Condición + timeout → esperar solo lo necesario"
- Nota del ponente: Definir los elementos: condición, máximo tiempo, polling. Beneficios: velocidad, estabilidad, menor fragilidad.
- Imagen sugerida: `docs/Explicit waits/slides/slide6.png`

---

## Slide 7 — Polling y comportamiento interno

- Caption: "Polling: comprobar periódicamente hasta timeout (default ≈500ms)"
- Nota del ponente: Explicar trade-offs (frecuencia de polling vs. coste CPU). Mostrar pseudocódigo conceptual del bucle de espera.
- Imagen sugerida: `docs/Explicit waits/slides/slide7.png`

---

## Slide 8 — Selenium puro: WebDriverWait + ExpectedConditions

- Caption: "WebDriverWait + ExpectedConditions (ejemplo práctico)"
- Nota del ponente: Mostrar el snippet corto y comentar opciones avanzadas (FluentWait para custom polling y ignoring exceptions).
- Código breve a mostrar: ``WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));``
- Imagen sugerida: `docs/Explicit waits/slides/slide8.png`

---

## Slide 9 — Serenity BDD: `WaitUntil` y Screenplay

- Caption: "WaitUntil.the(...).forNoMoreThan(...).seconds() — legible y reusable"
- Nota del ponente: Explicar la abstracción: `WaitUntil` encapsula `WebDriverWait`. Mostrar cómo centralizar waits en `interactions/DynamicLoadingWaits` y uso en tests con `tester.attemptsTo(...)`.
- Código breve a mostrar: ``WaitUntil.the(DynamicLoadingPage.FINISH_TEXT, isVisible()).forNoMoreThan(10).seconds();``
- Imagen sugerida: `docs/Explicit waits/slides/slide9.png`

---

## Slide 10 — Buenas prácticas y checklist

- Caption: "Checklist: centralizar waits, evitar sleeps, diferenciar DOM vs visibilidad"
- Nota del ponente: Lista rápida de acciones para aplicar en repositorios: centralizar waits, usar preguntas (`resolveAllFor` vs `resolveFor`), añadir test negativo de timeout, ajustar timeouts para CI.
- Imagen sugerida: `docs/Explicit waits/slides/slide10.png`

---

## Slide Demo (opcional) — Ejecutar y mostrar reporte

- Caption: "Demo: ejecutar suite y abrir Serenity report"
- Nota del ponente: Comandos a mostrar y qué mirar en el reporte (Key Statistics, Automated Scenarios, duración por test, screenshots/evidencias).
- Comandos sugeridos para pantalla:

```bash
./gradlew clean test
# abrir reporte (Linux):
xdg-open target/site/serenity/index.html
```

---

## Recomendaciones para las imágenes

- Mantener imágenes limpias y con poco texto. Preferir gifs cortos para loaders y PNG para diagramas.
- Nombres sugeridos: `slide1.png` .. `slide10.png` dentro de `docs/Explicit waits/slides/`.

---

## Archivo de notas del presentador listo

- Este archivo contiene las captions y notas para cada una de las 10 imágenes.
- Si deseas, puedo:
  - Generar un PDF/MD para la "presenter view".
  - Reemplazar las imágenes en la carpeta `docs/Explicit waits/slides/` con versiones optimizadas (resize/compression) si me subes los PNG/GIF.
