Feature: Validar cargas dinámicas con esperas explícitas en Selenium
  Como equipo de calidad
  Quiero automatizar la carga dinámica del sitio the-internet
  Para demostrar que las esperas explícitas estabilizan la sincronización en Selenium

  # Documentacion viva del comportamiento esperado.

  Scenario: Mostrar Hello World en Dynamic Loading Example 1
    Given el actor abre la página Dynamic Loading Example 1
    And el mensaje Hello World existe en el DOM pero permanece oculto
    When hace clic sobre el botón Start
    And el loader se hace visible mientras se procesa la información
    And el loader desaparece al finalizar la carga
    And espera explícitamente a que el elemento Hello World sea visible
    Then valida que el texto mostrado sea exactamente Hello World!

  Scenario: Renderizar Hello World en Dynamic Loading Example 2
    Given el actor abre la página Dynamic Loading Example 2
    And el mensaje Hello World aún no existe en el DOM
    When hace clic sobre el botón Start
    And el loader se hace visible mientras se genera el contenido
    And el loader desaparece al finalizar la carga
    And espera explícitamente a que el contenido se renderice en el DOM
    Then valida que el texto visible sea Hello World!

  Scenario: Ocultar el loader antes de mostrar el resultado
    Given el actor abre la página Dynamic Loading Example 1
    When hace clic sobre el botón Start
    And el loader se hace visible mientras se procesa la información
    And espera explícitamente a que el spinner desaparezca
    And espera explícitamente a que Hello World sea visible
    Then confirma que el loader ya no está visible
    And valida que el texto final sea Hello World!
