# Prueba Técnica Java

> ⚠️ **Disclaimer:**\
> Debido a que el desarrollo de esta prueba técnica coincidió parcialmente con mi horario laboral, no pude dedicarle el 100% del tiempo que me hubiera gustado.\
> Aun así, me aseguré de plantear una solución sólida, bien estructurada y con una arquitectura técnica coherente y justificada, priorizando la claridad, el orden lógico y la aplicabilidad real del sistema.

---

## 🏗️ Arquitectura General y Diseño Técnico

El sistema se basa en una arquitectura de microservicios desacoplados, orquestados por un API Gateway (NGINX) y desplegados con Docker Compose. Cada microservicio tiene su propia base de datos PostgreSQL, lo cual permite aislamiento total, desarrollo independiente, despliegue modular y persistencia garantizada.

### 🔧 Componentes principales (según el diagrama de arquitectura)

- **API Gateway (NGINX)**: expone las rutas públicas, protege el acceso mediante Basic Auth, enruta peticiones a los microservicios adecuados y permite escalar horizontalmente.
- **Microservicio de Productos**: contiene información detallada del catálogo. Se accede únicamente si hay disponibilidad en inventario, evitando sobrecarga.
- **Microservicio de Inventario**: maneja el stock, orquesta el flujo de compra y actualiza cantidades disponibles.
- **Historial de Compras**: persistencia de transacciones exitosas. Puede mantenerse embebido en Inventario o escalarse a un servicio propio.
- **Bases de datos PostgreSQL (una por microservicio)**: ofrecen integridad transaccional, consultas SQL optimizadas y separación de responsabilidades.
- **Docker Compose**: define servicios, redes privadas y volúmenes para persistencia de datos.

🧠 **Justificación**: Esta arquitectura favorece el desacoplamiento, la escalabilidad, la trazabilidad de errores, la resiliencia ante fallos parciales y la adaptabilidad futura.

📦 **Contenedores Docker**: Cada servicio corre de forma aislada. La arquitectura actual permite transición fluida a entornos como Kubernetes, mejorando la orquestación, balanceo, reinicios automáticos y métricas. Las bases de datos cuentan con volúmenes mapeados para garantizar persistencia ante reinicios.

![Diagrama de Arquitectura](./Docs/Arquitectura.svg)

---

## ⚙️ Decisiones Técnicas y Justificaciones

### • Ubicación del endpoint `/compra`

El endpoint `/compra` fue implementado en el microservicio de Inventario, ya que este es el responsable de verificar y modificar el stock. Esta decisión permite mantener una alta cohesión y bajo acoplamiento: Inventario actúa como orquestador y consulta al microservicio de Productos solo para validar la existencia de un producto. Colocar este endpoint en Productos violaría el principio de responsabilidad única.

### • Patrón de arquitectura utilizado

Se utilizó una arquitectura basada en microservicios desacoplados comunicados por HTTP. Cada microservicio es dueño de sus datos, y el servicio de Inventario coordina el proceso de compra consultando al servicio de Productos. Este patrón favorece la escalabilidad, el mantenimiento y el aislamiento de responsabilidades.

### • Mejoras previstas para escalabilidad futura

- Migrar la comunicación interna a eventos asincrónicos (Kafka, RabbitMQ).
- Extraer el historial de compras a un microservicio dedicado.
- Cache distribuido (Redis) para evitar consultas repetitivas.
- Circuit breakers y backoff para manejo de errores intermitentes.
- Autenticación centralizada con OAuth2/JWT y un MS de autenticación.

### • Uso de API Gateway (NGINX)

- Centraliza las rutas.
- Agrega Basic Auth a cada petición entrante.
- Permite trazabilidad, escalabilidad y desacoplamiento entre cliente y backend.
- Soporta fácilmente futuras reglas de reintento, headers, rate limiting, etc.

### • Separación de servicios y bases de datos

- Cada servicio tiene su propio contenedor.
- Cada servicio tiene su propia base de datos y volumen Docker para persistencia.
- Se elimina la dependencia entre esquemas compartidos y se facilita el versionamiento de datos.

### • Elección de base de datos

- PostgreSQL, por su robustez, ACID, compatibilidad y rendimiento.
- No se usó NoSQL ya que:
  - La estructura de los datos es relacional.
  - La consistencia fuerte es fundamental.
  - No existen cargas polimórficas ni jerárquicas sin estructurar.

---

## 🧾 Documentación de la API y Colección Postman

### 🧪 Swagger UI (incluido por microservicio)

- [http://localhost:8080/ms-productos/swagger-ui.html](http://localhost:8080/ms-productos/swagger-ui.html)
- [http://localhost:8080/ms-inventario/swagger-ui.html](http://localhost:8080/ms-inventario/swagger-ui.html)

🔍 Algunas respuestas no son idempotentes, ya que dependen del estado del sistema (por ejemplo, cuando no hay stock o cuando una compra ya fue registrada).

### 📬 Postman

- Colección separada por carpetas (una por microservicio).
- Cada endpoint tiene **una única petición** y múltiples ejemplos guardados por tipo de respuesta (200, 400, 404, 500).
- Incluye entorno local y entorno Docker (con variables como `{{URL}}` y credenciales).

---

## 🤖 Uso de Inteligencia Artificial

Se utilizó IA (ChatGPT) como asistente para:

- Generar esqueletos de código y pruebas unitarias.
- Redactar documentación técnica inicial.
- Establecer convenciones de código y configuración Swagger.

🧠 Sin embargo, cada resultado fue **validado, editado y adaptado manualmente**. La IA suele cometer errores arquitectónicos (mal uso de dependencias, ambigüedad en responsabilidades) y eso fue corregido. El resultado final refleja decisiones fundamentadas y estilizadas de forma coherente con el resto del sistema.

---

## 🧱 Estructura del Proyecto y Enfoque Arquitectónico

El proyecto adopta una **arquitectura mixta**, combinando el enfoque tradicional por capas con principios de arquitectura **hexagonal**:

- `controller/`: adapta entradas HTTP (puerto de entrada).
- `service/`: lógica de negocio principal (capa de aplicación).
- `model/`: entidades y DTOs (dominio).
- `repository/`: capa de acceso a datos.
- `config/`: configuración de seguridad, rutas, CORS, etc.

📌 **Ventaja**: Esta separación permite acelerar el desarrollo inicial, pero también facilita migrar hacia una arquitectura hexagonal completa, simplemente reorganizando las dependencias y controlando mejor los puertos de entrada/salida. No se pierde trabajo, solo se refina.

---

## 🧭 Versionado Semántico y Pull Requests

El repositorio sigue un **versionado semántico** basado en el esquema `MAJOR.MINOR.PATCH` (ej: `2.1.0`), donde:

- `MAJOR` se incrementa al introducir cambios incompatibles o nuevas funcionalidades globales (como un nuevo flujo o patrón).
- `MINOR` indica mejoras internas, ampliaciones no disruptivas o cambios estructurales.
- `PATCH` se reserva para correcciones menores, ajustes o mejoras sin impacto funcional.

📁 Cada Pull Request fue creado de forma clara y coherente con el propósito de reflejar estos cambios, permitiendo revisar la evolución del sistema paso a paso y facilitando el mantenimiento, la revisión de código y la trazabilidad técnica.

---

## 🔐 Seguridad y Autenticación

- Cada servicio expuesto requiere **Basic Auth** con:

```
usuario: admin
contraseña: admin123
```

- La decisión fue tomada por simplicidad, pero se dejó preparado el gateway para redirigir a un MS de autenticación futuro.

🔐 Mejora futura:

- Autenticación robusta con JWT o OAuth2.
- Control de roles, permisos y scopes.

---

## 🔄 Timeout y Retry

Se aplicó manejo de timeout y reintentos al consumir el MS de Productos desde Inventario:

- Evita que un error temporal cause un fallo total.
- Protege el flujo de compra y permite reintentos limitados controlados.

---

## ❌ Estructura clara de errores

Todas las respuestas, incluso las de error, siguen esta estructura:

```json
{
  "mensaje": "Error de formato en los campos",
    "data": {
        "nombre": "El nombre es obligatorio"
    }
}
```

Esto permite parseo automático en clientes, pruebas automatizadas y respuesta consistente a nivel de logs.

---

## 📦 CI/CD, Linters y Variables de Entorno

Por restricciones de tiempo **no se incluyó CI/CD automático**, ni linters como Checkstyle, PMD o SpotBugs. Sin embargo, se reconoce su valor y se planea así:

- **CI/CD**: GitHub Actions o GitLab CI con tests, build y deploy.
- **Linters**: Checkstyle + reglas personalizadas.
- **Secrets/env**: uso de `.env`, Vault o Secret Manager.

Actualmente las variables están hardcodeadas para facilitar pruebas rápidas, pero el código fue escrito con separación clara que permite moverlas a entornos.

---

## 🧪 Pruebas Unitarias y de Integración (Planeadas)

Aunque **no se implementaron pruebas unitarias ni de integración** en esta versión, esto **fue una decisión estratégica basada en los fuertes límites de tiempo de la prueba**. El enfoque fue construir una arquitectura sólida, modular, sin errores funcionales y completamente documentada.

🧠 **Sin embargo, se dejó preparado todo el entorno para facilitar su inclusión**, y se definieron las herramientas que habrían sido usadas:

### 🔬 Pruebas unitarias

- Herramienta: `JUnit 5`
- Scope: Servicios (`service`) y lógica interna desacoplada.
- Validaciones de negocio y control de excepciones.

### 🔗 Pruebas de integración

- Herramienta: `Spring Boot Test` con `@SpringBootTest`
- DB embebida: `H2` para simular PostgreSQL sin impacto real.
- Pruebas de endpoints reales con mock de dependencias y estados.

### ✅ CI/CD automático

Estas pruebas se integrarían en un pipeline de CI usando **GitHub Actions**, el cual:

- Ejecutaría tests en cada `push` y `pull request`.
- Mostrará el porcentaje de cobertura usando `JaCoCo`.
- Rechazará cambios que introduzcan fallas o regresiones.

🔒 Este flujo automatizado es esencial para garantizar calidad, estabilidad y escalabilidad del sistema, especialmente cuando se trabaja en equipo o en entornos productivos

---

## ✅ Buenas Prácticas Aplicadas

- Separación clara de capas y responsabilidades.
- Uso de DTOs para desacoplar el dominio del transporte.
- Logging uniforme y detallado con timestamps.
- Configuración centralizada.
- Validación de entradas y manejo de excepciones.
- Modularización de lógica para testing fácil.

---

## 🛒 Flujo de Compra - Patrón Saga Orquestado

Este flujo inicia en el endpoint `/compra`, ubicado en el microservicio de Inventario. Se eligió así porque es el servicio que gestiona el stock y puede actuar como orquestador, lo cual garantiza bajo acoplamiento, alta cohesión y control del flujo completo.

### 🔄 Paso 1: Verificación local de stock

El MS de Inventario consulta su propia base de datos para verificar:

- Que el producto existe
- Que hay unidades suficientes

**Justificación**: Esta validación local es extremadamente rápida y evita tráfico innecesario hacia otros servicios si no hay stock disponible. Ahorra red y procesamiento.

### 📡 Paso 2: Consulta al MS de Productos

Si el stock es suficiente, se hace una llamada HTTP al MS de Productos para obtener detalles como nombre, precio y descripción.

**Justificación**: Se evita consultar productos si no hay stock, priorizando eficiencia. Además, se valida que el producto no haya sido eliminado o modificado.

Esta llamada incluye Basic Auth y está sujeta a:

- Timeout para evitar cuelgues
- Reintentos configurados para fallos temporales

### 🧾 Paso 3: Persistencia en Historial de Compras

El MS de Inventario inserta los datos consolidados de la compra en la base de datos correspondiente al historial.

**Justificación**: Persistir primero garantiza que, si luego ocurre un error en el ajuste de inventario, el registro de que hubo una compra todavía existe y puede servir para alertas o auditoría.

Si esta operación falla, se aborta el proceso sin modificar el stock.

### 📉 Paso 4: Ajuste del Inventario

Se descuentan las unidades adquiridas del stock disponible en la base de datos de Inventario.

**Justificación**: Esto se realiza después de que la compra esté registrada. Si esta operación falla, se ejecuta una **compensación** eliminando el registro del historial (rollback lógico).

### 🛑 Paso 5: Manejo de Fallos (Compensaciones)

Si cualquier paso falla después del paso 2:

- Si falla la inserción del historial → no se toca el inventario
- Si falla el ajuste del stock → se elimina la compra registrada
- Si falla la comunicación entre MS → se retorna error al cliente y se loguea el fallo

**Justificación del patrón Saga Orquestado**: Este patrón fue elegido porque:

- Permite un control centralizado desde el MS de Inventario
- Es más fácil de implementar y monitorear que el patrón coreografiado
- Aporta claridad en los pasos y en los puntos de falla
- Las compensaciones están bien definidas

🚫 **No se usó una transacción distribuida** porque no existe un coordinador global entre bases de datos separadas, y habría requerido herramientas externas como 2PC, lo cual era innecesariamente complejo para esta prueba.

---

## ⚔️ ¿Por qué se eligió el Patrón Saga Orquestado?

El flujo de compra fue implementado usando el **Patrón Saga Orquestado**, y esta elección fue **deliberada y justificada** tras evaluar alternativas como:

- **Saga Coreografiada**: aunque elimina al orquestador central y distribuye la responsabilidad entre microservicios, tiende a generar flujos opacos y difíciles de rastrear, especialmente cuando los pasos de la transacción son secuenciales y dependen del éxito del anterior. También complica la implementación de rollback o compensaciones, dado que cada servicio debe saber cómo responder a fallas previas.

- **Transacciones distribuidas (2PC)**: garantizan atomicidad, pero son **difíciles de implementar en sistemas de microservicios desacoplados**, ya que exigen coordinadores externos, bloquean recursos y afectan el rendimiento general. Además, muchos motores de base de datos y sistemas modernos evitan este enfoque por su complejidad.

🔍 Por lo tanto, se optó por **Saga Orquestado** porque:

- Permite control centralizado del flujo.
- Define claramente los pasos y puntos de fallo.
- Facilita implementar compensaciones lógicas (rollback) ante errores.
- Evita dependencias entre microservicios para manejar lógica de negocio.
- Es ideal para procesos secuenciales como un flujo de compra con verificación, persistencia y ajuste.

Esta decisión no solo se aplicó en el código, sino que se explicó detalladamente en la documentación, permitiendo trazabilidad total del proceso.

---

## 🖥️ Instrucciones de Instalación y Ejecución

1. Clonar el repositorio:

```bash
git clone <URL_DEL_REPO>
cd <nombre-repo>
```

2. Levantar los contenedores:

```bash
docker-compose up --build
```

3. Esperar logs de inicio exitoso para:

- productos\_app
- inventario\_app
- productos\_db
- inventario\_db
- api\_gateway

4. Acceder desde navegador o Postman:

- [http://localhost:8080/ms-productos](http://localhost:8080/ms-productos)
- [http://localhost:8080/ms-inventario](http://localhost:8080/ms-inventario)
- [http://localhost:8080/ms-productos/swagger-ui.html](http://localhost:8080/ms-productos/swagger-ui.html)
- [http://localhost:8080/ms-inventario/swagger-ui.html](http://localhost:8080/ms-inventario/swagger-ui.html)

📬 **Importante:** Usar las credenciales `admin / admin123` para cada request.

5. Importar la colección de Postman y ejecutar pruebas desde los entornos definidos.

---


