# Refinamiento de historias de usuario

---

## 1. Contexto y trazabilidad de las historias seleccionadas

Las tres historias incluidas en este documento fueron seleccionadas a partir de flujos reales del sistema de gestión de turnos médicos de la clínica. Para este ejercicio, el equipo tomó como base la evidencia funcional disponible en el repositorio y reconstruyó una versión inicial de cada historia tal como suele aparecer en etapas tempranas del análisis: comprensible desde el negocio, pero todavía insuficiente para automatización, validación técnica y diseño riguroso de pruebas.

Posteriormente, dichas historias fueron revisadas con apoyo de IA para fortalecer su estructura y completitud. La versión final presentada aquí no corresponde a una aceptación automática de esa salida, sino al resultado de un proceso de análisis, contraste con el dominio y depuración manual por parte del equipo.

## 2. Historia 1: Registro de paciente en recepción

### 1.1 Historia de usuario original

Como recepcionista, quiero registrar pacientes en el sistema para que puedan ser atendidos.

### 1.2 Historia de usuario refinada

Como recepcionista de la clínica, quiero registrar a un paciente en la cola de atención con su identificador, nombre, prioridad y tipo de consulta para que el flujo de recepción continúe sin duplicados ni conflictos de identidad.

### 1.3 Criterios de aceptación en Gherkin

```gherkin
Característica: Registro de paciente en recepción

  Escenario: Registro exitoso de un paciente nuevo
    Dado que la recepcionista está en la estación de recepción
    Y que la cola de atención está disponible
    Cuando registra un paciente con identificador válido, nombre completo, prioridad y tipo de consulta
    Entonces el sistema registra el turno exitosamente
    Y el paciente aparece en estado "En espera"
    Y el monitor de la cola refleja el nuevo paciente

  Escenario: Reintento idempotente con la misma identidad
    Dado que un paciente ya fue registrado con un identificador y nombre determinados
    Cuando la recepcionista intenta registrar nuevamente el mismo identificador con el mismo nombre
    Entonces el sistema no crea un duplicado
    Y conserva un único registro operativo del paciente

  Escenario: Conflicto de identidad del paciente
    Dado que un paciente ya fue registrado con un identificador y nombre determinados
    Cuando la recepcionista intenta registrar el mismo identificador con un nombre diferente
    Entonces el sistema rechaza la operación
    Y informa un conflicto de identidad del paciente
```

### 1.4 Tabla comparativa de diferencias detectadas

| Aspecto | Historia original | Historia refinada |
| --- | --- | --- |
| Actor | Solo menciona recepcionista | Define rol y contexto clínico |
| Datos requeridos | No especifica datos | Incluye identificador, nombre, prioridad y tipo |
| Valor de negocio | Muy general | Garantiza continuidad y control de duplicados |
| Regla crítica | No menciona identidad | Incorpora idempotencia y conflicto de identidad |
| Testabilidad | Baja | Alta, con reglas observables |

### 1.5 Aplicación explícita de INVEST

| Principio | Cumplimiento en la historia refinada |
| --- | --- |
| Independent | Puede implementarse y validarse sin depender del cierre completo del flujo de caja o consulta. |
| Negotiable | Permite discutir formato de entrada, validaciones y mensajes de error sin perder el objetivo funcional. |
| Valuable | Aporta valor directo al negocio porque inicia formalmente la atención del paciente. |
| Estimable | El alcance es claro: formulario, validaciones, control de identidad y actualización de la cola. |
| Small | Se enfoca en un solo objetivo operacional: registrar correctamente el paciente. |
| Testable | Los criterios permiten validar éxito, idempotencia y conflicto de identidad. |

### 1.6 Hallazgos del análisis con SKAI

SKAI identificó las siguientes debilidades en la historia original antes de proponer su refinamiento:

**Ambigüedades detectadas:**

- La acción "registrar" no especificaba qué datos debían ingresarse ni si incluía prioridad o tipo de consulta.
- La frase "para que puedan ser atendidos" era ambigua respecto al destinatario: ¿gestión en caja, consulta médica o ambos canales?
- No se indicaba ningún mecanismo de validación de identidad, control de duplicados ni respuesta ante conflictos de registro.

**Brechas en criterios INVEST:**

- *Estimable:* Sin reglas de negocio explícitas —idempotencia, conflicto de identidad, prioridad— el equipo no podía dimensionar el esfuerzo de implementación.
- *Testeable:* Sin criterios de aceptación no era posible definir cuándo la historia estaría correctamente implementada.

**Coherencia con el proyecto:**

- La historia omitía reglas críticas del dominio: comportamiento idempotente ante registros repetidos y rechazo ante conflicto de identidad entre identificador y nombre.
- No especificaba que el paciente debía quedar visible en la cola de atención y en los monitores inmediatamente tras el registro.
- No contemplaba la confidencialidad de los datos del paciente ni su trazabilidad operativa para auditoría interna.

### 1.7 Preguntas de refinamiento con el Product Owner

SKAI propuso las siguientes preguntas para desambiguar la historia en una sesión de refinamiento con el Product Owner:

1. ¿Qué datos son obligatorios para el registro del paciente? ¿Identificación, nombre, prioridad y tipo de consulta?
2. ¿Cómo debe comportarse el sistema si se registra un paciente ya existente con la misma identificación y nombre?
3. ¿Qué ocurre si la identificación ya existe pero el nombre es diferente? ¿Debe bloquearse el registro y exigirse validación manual?
4. ¿Qué retroalimentación debe recibir el recepcionista tras un registro exitoso o fallido?
5. ¿El registro debe activar automáticamente la visualización del paciente en la cola y en los monitores?
6. ¿Se requiere algún tratamiento especial en el flujo de registro para pacientes con alta prioridad clínica?
7. ¿Cómo se garantiza la confidencialidad de los datos ingresados durante el registro?
8. ¿Qué trazabilidad debe quedar registrada por cada nuevo paciente creado en el sistema?

### 1.8 Evaluación crítica del equipo sobre la propuesta de SKAI

| Elemento analizado | Propuesta de SKAI | Decisión del equipo |
| --- | --- | --- |
| Datos obligatorios | Identificador, nombre, prioridad y tipo de consulta | Aceptada: coincide con los datos mínimos del dominio funcional |
| Idempotencia | No crear duplicado cuando la identidad es idéntica | Aceptada: alineada con la regla de negocio documentada en el contexto |
| Conflicto de identidad | Rechazar la operación si el nombre difiere para el mismo identificador | Aceptada: es la regla crítica explícita del proyecto |
| Confidencialidad | Información visible únicamente para el rol de recepcionista | Incorporada en los criterios Gherkin como restricción observable |
| Auditoría | Registrar el evento de creación del paciente para trazabilidad | Incluida como efecto esperado en los escenarios de aceptación |
| Historia propuesta por SKAI | Incluía título formal, datos mínimos, idempotencia, conflicto y confidencialidad | Adoptada como base; el equipo ajustó el lenguaje al dominio técnico del proyecto |

## 3. Historia 2: Llamado del siguiente paciente en caja

### 2.1 Historia de usuario original

Como operario de caja, quiero llamar al siguiente paciente para atenderlo más rápido.

Fuente de la historia original: flujo operativo de caja, llamado del siguiente paciente y gestión del turno descritos en la auditoría técnica del backend.

### 2.2 Historia de usuario refinada

Como operario de caja, quiero llamar al siguiente paciente elegible según la prioridad y el orden de llegada para atenderlo sin saltos de turno ni llamadas inválidas cuando la cola esté vacía.

### 2.3 Criterios de aceptación en Gherkin

```gherkin
Característica: Llamado del siguiente paciente en caja

  Escenario: Caja llama al siguiente paciente prioritario
    Dado que existen pacientes en espera con distinta prioridad
    Cuando el operario de caja selecciona la opción "Llamar siguiente"
    Entonces el sistema asigna el turno al paciente elegible con mayor prioridad
    Y cambia su estado a "En caja"
    Y actualiza el monitor operativo

  Escenario: Caja respeta el orden de llegada entre pacientes de igual prioridad
    Dado que existen varios pacientes con la misma prioridad en espera
    Cuando el operario de caja llama al siguiente paciente
    Entonces el sistema selecciona primero al paciente que llegó antes

  Escenario: Caja intenta llamar con cola vacía
    Dado que no existen pacientes elegibles en la cola
    Cuando el operario de caja intenta llamar al siguiente paciente
    Entonces el sistema rechaza la operación
    Y muestra un mensaje indicando que no hay pacientes disponibles
```

### 2.4 Tabla comparativa de diferencias detectadas

| Aspecto | Historia original | Historia refinada |
| --- | --- | --- |
| Objetivo | Solo rapidez | Atención ordenada y sin errores |
| Regla de negocio | No existe | Incluye prioridad y orden de llegada |
| Restricción | No existe | Controla el caso de cola vacía |
| Resultado esperado | Ambiguo | Define cambio de estado y actualización visible |
| Testabilidad | Limitada | Cubre positivo, regla y negativo |

### 2.5 Aplicación explícita de INVEST

| Principio | Cumplimiento en la historia refinada |
| --- | --- |
| Independent | Se puede trabajar de forma separada del inicio o cierre de consulta médica. |
| Negotiable | Permite acordar mensajes, indicadores visuales y detalles del algoritmo de prioridad. |
| Valuable | Optimiza el flujo de caja y reduce errores operativos en la atención de pacientes. |
| Estimable | El comportamiento esperado está delimitado por reglas claras y pocos estados. |
| Small | Se limita a una operación de caja: seleccionar y llamar al siguiente paciente. |
| Testable | Puede probarse con escenarios de prioridad, empate y cola vacía. |

### 2.6 Hallazgos del análisis con SKAI

SKAI identificó las siguientes debilidades en la historia original antes de proponer su refinamiento:

**Ambigüedades detectadas:**

- La expresión "siguiente paciente" no definía si la selección era por prioridad clínica, orden de llegada o una combinación de ambos criterios.
- La justificación "para atenderlo más rápido" era subjetiva y no representaba un valor funcional ni un resultado verificable.
- No se especificaba qué implicaba operativamente "llamar": ¿cambio de estado del turno, notificación al paciente, actualización del monitor?
- Faltaba definir el comportamiento del sistema cuando no hubiera pacientes elegibles en la cola.

**Brechas en criterios INVEST:**

- *Estimable:* Sin criterios claros de selección del paciente y sin manejo explícito de excepciones, el esfuerzo no podía dimensionarse con precisión.
- *Testeable:* La ausencia de criterios de aceptación impedía definir cuándo la historia estaría correctamente validada.

**Coherencia con el proyecto:**

- La historia omitía la combinación de prioridad clínica y orden de llegada como criterios de selección, requeridos explícitamente en las reglas de negocio del dominio.
- No incluía consideraciones de trazabilidad del evento de llamado ni restricciones de confidencialidad por rol operativo.
- No contemplaba el escenario de cola sin pacientes elegibles, identificado como excepción crítica en los flujos del negocio.

### 2.7 Preguntas de refinamiento con el Product Owner

SKAI propuso las siguientes preguntas para desambiguar la historia en una sesión de refinamiento con el Product Owner:

1. ¿Qué criterio debe usar el sistema para determinar al "siguiente paciente"? ¿Prioridad clínica, orden de llegada, ambos?
2. ¿Qué ocurre exactamente al "llamar" al siguiente paciente? ¿Se actualiza el estado del turno, se notifica al paciente, aparece en una pantalla?
3. ¿Existen validaciones previas antes de llamar a un paciente, por ejemplo verificación de pago pendiente?
4. ¿Qué debe ocurrir si no hay pacientes elegibles en la cola? ¿Se muestra un mensaje de advertencia?
5. ¿El sistema debe registrar el evento de llamado para trazabilidad y auditoría interna?
6. ¿Existe diferencia entre "llamar" y "atender"? El llamado, ¿implica inicio automático de la atención?
7. ¿Cómo se asegura que la información del paciente sea visible únicamente para el operario de caja autorizado?
8. ¿Debe el sistema manejar la concurrencia si dos estaciones de caja intentan llamar al mismo paciente simultáneamente?

### 2.8 Evaluación crítica del equipo sobre la propuesta de SKAI

| Elemento analizado | Propuesta de SKAI | Decisión del equipo |
| --- | --- | --- |
| Criterio de selección | Prioridad clínica + orden de llegada | Aceptada: alineada con las reglas de negocio del dominio |
| Comportamiento ante cola vacía | Mensaje de advertencia, sin acción sobre el flujo | Aceptada: previene llamadas inválidas y mantiene consistencia operativa |
| Cambio de estado al llamar | Turno pasa a "en atención en caja" | Aceptada con ajuste terminológico al lenguaje técnico del dominio |
| Trazabilidad | Registrar el evento de llamado para auditoría | Incorporada como efecto esperado en los escenarios Gherkin |
| Actualización de monitores | Mostrar el cambio en paneles autorizados | Incluida como estado observable en los criterios de aceptación |

## 4. Historia 3: Reclamación de paciente por consultorio

### 3.1 Historia de usuario original

Como médico, quiero tomar el siguiente paciente para comenzar la consulta.

Fuente de la historia original: flujo de reclamación de paciente para consulta y control de concurrencia entre consultorios observado en el dominio funcional del proyecto.

### 3.2 Historia de usuario refinada

Como médico en un consultorio activo, quiero reclamar al siguiente paciente elegible para consulta de manera exclusiva para iniciar la atención sin que otro consultorio tome el mismo turno al mismo tiempo.

### 3.3 Criterios de aceptación en Gherkin

```gherkin
Característica: Reclamación de paciente por consultorio

  Escenario: Consultorio reclama un paciente elegible
    Dado que existe un paciente disponible para pasar a consulta
    Y que el consultorio se encuentra activo
    Cuando el médico reclama el siguiente paciente
    Entonces el sistema asigna el turno a ese consultorio
    Y el paciente deja de estar disponible para otros consultorios

  Escenario: Dos consultorios intentan reclamar el mismo paciente
    Dado que existe un único paciente elegible para consulta
    Y que dos consultorios activos intentan reclamarlo al mismo tiempo
    Cuando ambos ejecutan la operación de reclamación
    Entonces solo un consultorio obtiene el turno
    Y el otro recibe una respuesta de conflicto

  Escenario: Consultorio inactivo intenta reclamar un paciente
    Dado que existe un paciente elegible para consulta
    Y que el consultorio está inactivo
    Cuando el médico intenta reclamar el siguiente paciente
    Entonces el sistema rechaza la operación
    Y mantiene el turno disponible para un consultorio válido
```

### 3.4 Tabla comparativa de diferencias detectadas

| Aspecto | Historia original | Historia refinada |
| --- | --- | --- |
| Precondición | No existe | Exige consultorio activo |
| Exclusividad | No existe | Evita doble reclamación |
| Riesgo operacional | No visible | Controla concurrencia entre consultorios |
| Resultado | Iniciar consulta | Asignación exclusiva y consistente |
| Testabilidad | Parcial | Alta con escenarios de conflicto |

### 3.5 Aplicación explícita de INVEST

| Principio | Cumplimiento en la historia refinada |
| --- | --- |
| Independent | Se puede desarrollar de manera separada de la lógica de pago o del dashboard. |
| Negotiable | Permite ajustar precondiciones, mensajes de conflicto y forma de activación del consultorio. |
| Valuable | Protege el flujo clínico y evita inconsistencias que afectarían la atención médica. |
| Estimable | El alcance está acotado a disponibilidad, activación y control de concurrencia. |
| Small | Se centra en una sola acción de negocio: reclamar un turno para consulta. |
| Testable | Puede verificarse con escenarios de éxito, conflicto e invalidez del consultorio. |

### 3.6 Hallazgos del análisis con SKAI

SKAI identificó las siguientes debilidades en la historia original antes de proponer su refinamiento:

**Ambigüedades detectadas:**

- La acción "tomar" no distinguía entre reservar exclusivamente el turno para el consultorio e iniciar la consulta; posiblemente son dos eventos con estados distintos en el flujo.
- No se definían las condiciones que debía cumplir el "siguiente paciente": ¿solo orden de llegada, prioridad clínica, pago validado o una combinación?
- No se contemplaba qué ocurriría si dos médicos intentaban "tomar" simultáneamente al mismo paciente, escenario de alta relevancia en el dominio.
- Faltaba indicar el comportamiento del sistema cuando no hubiera pacientes disponibles para consulta.

**Brechas en criterios INVEST:**

- *Estimable:* Las ambigüedades en el alcance de "tomar" y en las condiciones de elegibilidad del paciente impedían una estimación precisa del esfuerzo.
- *Testeable:* Sin criterios de aceptación explícitos ni definición de escenarios de concurrencia, la historia no era completamente verificable.

**Coherencia con el proyecto:**

- La historia no incorporaba la regla de negocio más crítica del flujo: un paciente solo puede ser reclamado por un consultorio a la vez; ante concurrencia, solo una operación debe confirmar el cambio de estado.
- Omitía el impacto esperado en los paneles de monitoreo en tiempo real y los requisitos de auditoría del evento de reclamación.
- No distinguía entre "reclamar" (asignación exclusiva del turno al consultorio) e "iniciar consulta" (acción clínica posterior), lo cual tiene implicaciones directas en el modelo de estados del turno.

### 3.7 Preguntas de refinamiento con el Product Owner

SKAI propuso las siguientes preguntas para desambiguar la historia en una sesión de refinamiento con el Product Owner:

1. ¿"Tomar" al paciente implica solo reservar el turno para el consultorio, o también inicia automáticamente la consulta?
2. ¿Qué condiciones debe cumplir un paciente para ser considerado "siguiente"? ¿Solo orden, prioridad clínica, pago validado, u otras?
3. ¿Debe el sistema registrar un evento de auditoría al reclamar un paciente? ¿Qué datos debe incluir ese evento?
4. ¿Qué ocurre si dos médicos intentan tomar el mismo paciente simultáneamente? ¿Cómo debe resolverse el conflicto?
5. ¿Qué debe mostrarse en el monitor o panel tras reclamar un paciente? ¿Requiere actualización inmediata?
6. ¿Qué mensaje o notificación recibe el médico si no hay pacientes disponibles para consulta?
7. ¿Se requiere distinguir formalmente entre "reclamar un paciente" y "comenzar la consulta", o es una sola acción desde la interfaz?
8. ¿Qué restricciones de confidencialidad aplican al mostrar los datos del paciente al médico durante y después de la reclamación?

### 3.8 Evaluación crítica del equipo sobre la propuesta de SKAI

| Elemento analizado | Propuesta de SKAI | Decisión del equipo |
| --- | --- | --- |
| Alcance de "tomar" | Posible necesidad de dividir en "reclamar" e "iniciar consulta" | El equipo acotó la historia a "reclamar"; "iniciar consulta" se manejará en una historia separada del mismo épico |
| Control de concurrencia | Solo un consultorio confirma el turno ante solicitudes simultáneas | Aceptada: es la regla de negocio más crítica del dominio para este flujo |
| Condición de consultorio activo | Requerida como precondición explícita antes de la reclamación | Aceptada: incorporada como Given en los escenarios Gherkin de la historia refinada |
| Comportamiento ante cola sin pacientes | Mensaje informativo al médico, sin asignación | Aceptada: cubierta por escenario de excepción en los criterios de aceptación |
| Trazabilidad | Registrar evento de reclamación con datos del consultorio y el paciente | Incluida como efecto observable en los criterios de aceptación |

## 5. Conclusiones del refinamiento

1. Las historias originales reflejaban la intención del negocio, pero todavía presentaban ambigüedades típicas de un requerimiento inicial.
2. El apoyo de IA fue útil para proponer una estructura más completa y para hacer visibles vacíos que, en una revisión superficial, podían pasar desapercibidos.
3. Sin embargo, el valor real del ejercicio estuvo en la revisión del equipo, que ajustó cada historia al comportamiento concreto del sistema y a sus reglas de negocio.
4. Como resultado, las historias refinadas presentan mejor nivel de independencia, claridad, estimabilidad y testabilidad, por lo que son más adecuadas para planificación, desarrollo y diseño de pruebas.
