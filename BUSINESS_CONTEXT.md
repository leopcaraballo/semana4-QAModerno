# Contexto de negocio

> Contexto de negocio estructurado para alimentar herramientas SKAI, en el análisis de historias de usuario y generación de casos de prueba del sistema de turnos médicos de la clínica.

---

## 1. Descripción del Proyecto

### 1.1 Nombre del Proyecto

Sistema de agendamiento y gestión de turnos médicos de una clínica.

### 1.2 Objetivo del Proyecto

Permitir la gestión operativa de la sala de espera clínica desde el registro del paciente hasta la finalización de la atención médica, asegurando trazabilidad del turno, visibilidad en tiempo real del estado de cada paciente y continuidad del flujo entre recepción, caja y consulta médica.

## 2. Flujos Críticos del Negocio

### 2.1 Principales Flujos de Trabajo

1. Recepción registra al paciente en la cola de atención con identificación, nombre, prioridad y tipo de consulta.
2. Caja llama al siguiente paciente elegible y gestiona el estado administrativo del turno, incluyendo validación de pago, pago pendiente, cancelación o ausencia.
3. El personal médico reclama al siguiente paciente disponible para consulta y evita que dos consultorios tomen el mismo turno.
4. El médico inicia la consulta, finaliza la atención o registra la ausencia del paciente.
5. Los monitores y paneles consultan en tiempo real el estado de la cola, el siguiente turno, el historial reciente y métricas operativas.

### 2.2 Módulos o Funcionalidades Críticas

1. Registro y check-in de pacientes.
2. Gestión de cola en recepción.
3. Gestión operativa de caja.
4. Asignación y reclamación de pacientes en consultorios.
5. Monitoreo en tiempo real del estado de la sala de espera.
6. Persistencia de eventos clínico-operativos y publicación confiable de cambios de estado.

## 3. Reglas de Negocio y Restricciones

### 3.1 Reglas de Negocio Relevantes

1. Un paciente se identifica operacionalmente por su identificador y su nombre completo.
2. Si llega el mismo identificador de paciente con el mismo nombre, el sistema debe comportarse de forma idempotente y no duplicar el registro.
3. Si llega el mismo identificador de paciente con un nombre distinto, el sistema debe rechazar la operación por conflicto de identidad y exigir validación humana.
4. La cola debe respetar la prioridad clínica y el orden de llegada cuando varios pacientes compiten por atención.
5. Caja no debe llamar pacientes si la cola no tiene pacientes elegibles para esa operación.
6. Un paciente no puede ser reclamado simultáneamente por dos consultorios; ante concurrencia, solo una operación debe confirmar el cambio de estado.
7. Los cambios de estado del turno deben ser trazables para reconstrucción operativa y auditoría interna.
8. Los paneles de monitoreo deben reflejar los cambios del flujo con actualización casi en tiempo real para evitar descoordinación entre estaciones.

### 3.2 Regulaciones o Normativas

1. Deben considerarse principios de protección de datos personales aplicables a información clínica y de identificación del paciente.
2. Debe preservarse la confidencialidad de la información operativa expuesta en pantallas y estaciones según el rol del usuario.
3. La clínica debe contar con trazabilidad de eventos relevantes para auditoría interna de atención y operación.

## 4. Perfiles de Usuario y Roles

### 4.1 Perfiles o Roles de Usuario en el Sistema

1. Recepcionista.
2. Operario de caja.
3. Médico o personal de consultorio.
4. Usuario observador de monitor o pantalla pública de turnos.

### 4.2 Permisos y Limitaciones de Cada Perfil

| Perfil | Puede hacer | No debe hacer |
| --- | --- | --- |
| Recepcionista | Registrar pacientes y consultar estado inicial de la cola | Validar pagos o iniciar consultas |
| Operario de caja | Llamar siguiente paciente y gestionar estados administrativos del pago | Registrar check-in clínico o cerrar consultas médicas |
| Médico | Reclamar paciente, iniciar consulta, finalizar atención y registrar ausencia | Registrar pacientes en recepción o validar pagos |
| Observador de monitor | Consultar estado visible de turnos y métricas operativas | Ejecutar cambios de estado del flujo |

## 5. Condiciones del Entorno Técnico

### 5.1 Plataformas Soportadas

1. Aplicación web para estaciones operativas internas.
2. Pantallas web para monitoreo en sala de espera.
3. Entorno dockerizado para ejecución integrada del sistema.

### 5.2 Tecnologías o Integraciones Clave

1. Backend en .NET y ASP.NET Core.
2. Frontend web en Next.js y React.
3. PostgreSQL como base de datos para event store y outbox.
4. RabbitMQ para mensajería y propagación de eventos.
5. SignalR para actualización en tiempo real, con polling como respaldo.
6. Docker Compose para orquestación local de servicios.

## 6. Casos Especiales o Excepciones

### 6.1 Escenarios Alternos o Excepciones que Deben Considerarse

1. Intento de registrar un paciente con identidad inconsistente respecto al registro previo.
2. Cola sin pacientes elegibles cuando caja o consultorio solicita el siguiente turno.
3. Reclamación concurrente del mismo paciente por dos consultorios.
4. Fallo temporal de mensajería o retraso de actualización en monitores, con necesidad de mantener consistencia operativa.
5. Registro de ausencia del paciente después de haber sido llamado, evitando transiciones de estado inválidas.
6. Reinicio del servicio con necesidad de reconstruir vistas operativas a partir de eventos persistidos.
