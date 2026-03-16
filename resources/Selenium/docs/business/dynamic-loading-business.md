# Casos de Uso de Prueba – Carga Dinámica

## Caso de Uso 1: Visualización del mensaje final (Ejemplo 1)

**Escenario de negocio**

Un usuario ingresa al ejemplo 1 de la página de carga dinámica y solicita que el sistema muestre un mensaje que inicialmente está oculto.
El sistema necesita realizar una carga dinámica antes de mostrar el resultado.

**Objetivo de la prueba**

Validar que el sistema finalmente muestre el mensaje esperado una vez que la carga termina.

**Flujo de la prueba**

1. El usuario accede al ejemplo **“Example 1: Element on page that is hidden”**.
2. El usuario presiona el botón **Start** para iniciar el proceso.
3. El sistema muestra un indicador de carga mientras procesa la información.
4. Después de completar la carga, el sistema muestra el mensaje final.

**Resultado esperado**

El mensaje **“Hello World!”** aparece visible en la página después de finalizar el proceso.

**Valor para negocio**

Se garantiza que el sistema puede **mostrar correctamente información que inicialmente está oculta y se habilita después de un proceso interno**.

---

# Caso de Uso 2: Generación del mensaje final (Ejemplo 2)

**Escenario de negocio**

Un usuario inicia el proceso en el ejemplo 2, donde el contenido **no existe inicialmente en la página** y debe ser generado dinámicamente por el sistema.

**Objetivo de la prueba**

Validar que el sistema crea y muestra el mensaje final después de completar la carga dinámica.

**Flujo de la prueba**

1. El usuario accede al ejemplo **“Example 2: Element rendered after the fact”**.
2. El usuario presiona el botón **Start**.
3. El sistema inicia un proceso de carga en segundo plano.
4. Cuando el proceso finaliza, el sistema genera y muestra el mensaje.

**Resultado esperado**

El mensaje **“Hello World!”** aparece correctamente en la interfaz.

**Valor para negocio**

Se asegura que **la información generada dinámicamente por el sistema se muestra correctamente al usuario**, incluso cuando el elemento **no existe inicialmente en la página**.

---

# Caso de Uso 3: Desaparición del indicador de carga

**Escenario de negocio**

Durante el proceso de carga, el sistema muestra un indicador visual para informar al usuario que el contenido está siendo procesado.

**Objetivo de la prueba**

Validar que el indicador de carga desaparece antes de mostrar el mensaje final.

**Flujo de la prueba**

1. El usuario inicia el proceso presionando **Start**.
2. El sistema muestra un indicador de carga.
3. El sistema termina el procesamiento.
4. El indicador desaparece.
5. El mensaje final se muestra al usuario.

**Resultado esperado**

* El indicador de carga **ya no es visible**.
* El mensaje **“Hello World!”** aparece en la pantalla.

**Valor para negocio**

Esto garantiza una **experiencia de usuario clara**, evitando que el sistema muestre al mismo tiempo un **resultado final y un estado de carga activo**.
