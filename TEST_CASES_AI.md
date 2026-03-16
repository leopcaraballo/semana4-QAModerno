# Casos de prueba generados con IA

---

## 1. Alcance de la matriz

La matriz cubre tres flujos críticos del negocio: registro de pacientes en recepción, llamado del siguiente paciente en caja y reclamación de pacientes por consultorio. Los escenarios fueron construidos para incluir resultados esperados, comportamientos de rechazo, situaciones límite, validación de reglas de negocio y manejo de fallos operativos. La intención no fue únicamente producir una lista de pruebas, sino evidenciar cómo una propuesta inicial generada con apoyo de IA puede y debe ser sometida a revisión técnica antes de considerarse útil para QA.

## 2. Cobertura requerida por la asignación

| Tipo de cobertura | Casos incluidos | Evidencia |
| --- | --- | --- |
| Escenarios positivos | CP-01, CP-05, CP-09 | Operación exitosa del flujo principal |
| Escenarios negativos | CP-03, CP-07, CP-11 | Rechazo por incumplimiento de reglas |
| Casos límite | CP-02, CP-06, CP-10, CP-13 | Idempotencia, empate, concurrencia y cola con único paciente |
| Reglas de negocio | CP-02, CP-03, CP-05, CP-06, CP-10 | Identidad, prioridad y exclusividad |
| Manejo de errores del sistema | CP-04, CP-08, CP-12 | Falla de servicio, retraso de actualización y error de persistencia |

## 3. Matriz de casos de prueba en Gherkin

```gherkin
Característica: Registro de paciente en recepción

  Escenario: CP-01 Registro exitoso de paciente nuevo
    Dado que la recepcionista está autenticada en su estación operativa
    Y que la cola de atención está habilitada
    Cuando registra un paciente con identificador válido, nombre completo, prioridad normal y tipo de consulta general
    Entonces el sistema confirma el registro del turno
    Y el paciente queda visible en estado "En espera"

  Escenario: CP-02 Reintento idempotente con los mismos datos
    Dado que un paciente ya fue registrado con un identificador y nombre determinados
    Cuando la recepcionista registra nuevamente el mismo identificador con el mismo nombre
    Entonces el sistema no crea un registro duplicado
    Y mantiene una sola entrada operativa del paciente

  Escenario: CP-03 Conflicto de identidad del paciente
    Dado que un paciente ya fue registrado con un identificador y nombre determinados
    Cuando la recepcionista registra el mismo identificador con un nombre diferente
    Entonces el sistema rechaza la operación por conflicto de identidad

  Escenario: CP-04 Error del sistema durante el registro
    Dado que la recepcionista diligenció correctamente el formulario
    Cuando el servicio de registro no está disponible
    Entonces el sistema informa el error operativo
    Y no marca al paciente como registrado

Característica: Llamado del siguiente paciente en caja

  Escenario: CP-05 Caja llama al paciente con mayor prioridad
    Dado que existen pacientes en espera con prioridades alta y normal
    Cuando el operario de caja llama al siguiente paciente
    Entonces el sistema selecciona primero al paciente de mayor prioridad
    Y actualiza su estado a "En caja"

  Escenario: CP-06 Caja respeta el orden de llegada en igualdad de prioridad
    Dado que existen dos pacientes con la misma prioridad en espera
    Y que uno llegó antes que el otro
    Cuando el operario de caja llama al siguiente paciente
    Entonces el sistema selecciona primero al paciente con mayor antigüedad en la cola

  Escenario: CP-07 Caja intenta llamar con cola vacía
    Dado que no existen pacientes elegibles en la cola
    Cuando el operario de caja llama al siguiente paciente
    Entonces el sistema rechaza la acción
    Y muestra que no hay pacientes disponibles

  Escenario: CP-08 Falla de actualización en monitor después del llamado
    Dado que caja llamó correctamente al siguiente paciente
    Cuando ocurre un retraso temporal en la actualización del monitor
    Entonces el sistema debe conservar el estado correcto del turno en backend
    Y reintentar la propagación de la actualización visible

Característica: Reclamación de paciente por consultorio

  Escenario: CP-09 Reclamo exitoso desde consultorio activo
    Dado que existe un paciente elegible para consulta
    Y que el consultorio está activo
    Cuando el médico reclama el siguiente paciente
    Entonces el sistema asigna el turno a ese consultorio
    Y el paciente deja de estar disponible para otros consultorios

  Escenario: CP-10 Conflicto por doble reclamación concurrente
    Dado que existe un único paciente elegible para consulta
    Y que dos consultorios activos intentan reclamarlo simultáneamente
    Cuando ambos ejecutan la operación de reclamación
    Entonces solo uno obtiene el turno
    Y el otro recibe un conflicto de concurrencia

  Escenario: CP-11 Consultorio inactivo intenta reclamar
    Dado que existe un paciente elegible para consulta
    Y que el consultorio está inactivo
    Cuando el médico intenta reclamar el siguiente paciente
    Entonces el sistema rechaza la operación
    Y conserva el paciente disponible para una estación válida

  Escenario: CP-12 Error de persistencia durante la reclamación
    Dado que un consultorio activo intenta reclamar un paciente elegible
    Cuando ocurre un error de persistencia del evento
    Entonces el sistema informa la falla
    Y no deja el turno en un estado parcial o inconsistente

  Escenario: CP-13 Caja opera con un único paciente elegible en cola
    Dado que existe un solo paciente elegible en la cola de caja
    Cuando el operario de caja llama al siguiente paciente
    Entonces el sistema asigna ese único paciente sin ambigüedad
    Y la cola queda sin pacientes elegibles para una segunda llamada inmediata
```

## 4. Ajustes realizados por el probador sobre la salida de IA

Los ajustes documentados en esta sección surgieron de tres fuentes complementarias: las ambigüedades identificadas por SKAI en su análisis de cada historia (que revelaron reglas de negocio no cubiertas en la propuesta inicial de pruebas), las brechas de coherencia detectadas por SKAI respecto al contexto del proyecto (que evidenciaron escenarios de error y casos límite no contemplados), y la revisión directa del equipo contra el comportamiento observable del dominio funcional. Los casos CP-02 y CP-03 surgieron directamente de las reglas de idempotencia y conflicto de identidad destacadas por SKAI; CP-08 y CP-12 se derivan de las brechas de coherencia en el manejo de fallos operativos; y CP-13 fue adicionado por el equipo como caso límite operativo que la propuesta inicial de IA no había contemplado.

| ID | Caso generado por IA | Ajuste del probador | Justificación técnica |
| --- | --- | --- | --- |
| CP-01 | Registrar paciente exitosamente | Se añadió precondición de cola habilitada y visibilidad del estado final | Evita un caso demasiado genérico y confirma efecto observable |
| CP-02 | Repetir registro del mismo paciente | Se ajustó a idempotencia explícita, no a error por duplicado | El dominio real acepta reintentos con la misma identidad |
| CP-03 | Validar documento duplicado | Se reemplazó por conflicto de identidad con mismo identificador y nombre diferente | La regla crítica del sistema no es duplicado simple, sino inconsistencia de identidad |
| CP-04 | Error al guardar registro | Se aclaró que el paciente no debe quedar marcado como registrado | Evita falso positivo de interfaz frente a una falla del servicio |
| CP-05 | Caja llama siguiente paciente | Se añadió criterio de prioridad clínica | La cola no es estrictamente FIFO para todos los casos |
| CP-06 | Llamar siguiente por orden | Se acotó a empate de prioridad | Sin ese ajuste, el caso contradice la regla de priorización |
| CP-08 | Monitor se actualiza después del llamado | Se reformuló como resiliencia ante retraso de actualización | El backend debe conservar consistencia aunque falle la visualización inmediata |
| CP-10 | Dos médicos reclaman el mismo paciente | Se cambió a conflicto de concurrencia verificable | La validación correcta es exclusividad de asignación y respuesta de conflicto |
| CP-11 | Consultorio reclama un paciente | Se añadió la precondición de consultorio inactivo | Sin esa condición no se valida una restricción operacional real |
| CP-12 | Error al reclamar paciente | Se especificó que no puede quedar un estado parcial | El mayor riesgo es la inconsistencia transaccional del turno |
| CP-13 | Caja llama al siguiente paciente | Se agregó el caso de un único paciente elegible en cola | Hace explícito un caso límite operativo que la IA no había considerado |

## 5. Conclusiones de la revisión humana

1. La experiencia de trabajo con IA mostró que su mayor aporte está en acelerar la generación de una primera propuesta de pruebas, no en sustituir el análisis del probador.
2. Varios escenarios iniciales resultaban demasiado amplios, poco precisos o insuficientemente conectados con el comportamiento real del sistema, por lo que fue necesario depurarlos.
3. La revisión humana permitió incorporar reglas del dominio que son determinantes para la calidad del producto, como idempotencia, conflicto de identidad, priorización clínica y control de concurrencia.
4. En consecuencia, la matriz final no debe entenderse como una salida automática de IA, sino como un artefacto consolidado por el equipo después de evaluar riesgos, ajustar supuestos y traducirlos en casos verificables.
