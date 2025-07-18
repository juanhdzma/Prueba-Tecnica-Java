# Prueba Tecnica Java


---

## Decisiones Técnicas y Justificaciones

### • Ubicación del endpoint `/compra`
El endpoint `/compra` fue implementado en el microservicio de Inventario, ya que este es el responsable de verificar y modificar el stock. Esta decisión permite mantener una alta cohesión y bajo acoplamiento: Inventario actúa como orquestador y consulta al microservicio de Productos solo para validar la existencia de un producto. Colocar este endpoint en Productos violaría el principio de responsabilidad única.

### • Patrón de arquitectura utilizado
Se utilizó una arquitectura basada en microservicios desacoplados comunicados por HTTP. Cada microservicio es dueño de sus datos, y el servicio de Inventario coordina el proceso de compra consultando al servicio de Productos. Este patrón favorece la escalabilidad, el mantenimiento y el aislamiento de responsabilidades.

### • Mejoras previstas para escalabilidad futura
• Migrar la comunicación interna a eventos asincrónicos con mensajería (ej. Kafka o RabbitMQ)  
• Extraer el historial de compras a un microservicio dedicado  
• Usar cache distribuido (Redis) para acelerar consultas  
• Incluir tolerancia a fallos con circuit breakers y backoff en reintentos  
• Aplicar autenticación robusta con JWT u OAuth2 desde el gateway

### • Uso de API Gateway (NGINX)
Se implementó un API Gateway con NGINX para centralizar el acceso a los microservicios. Además de enrutar correctamente las solicitudes, inyecta encabezados como `X-API-KEY` y aísla la lógica interna del sistema del cliente final. También permite escalar horizontalmente los servicios detrás del gateway sin modificar el cliente.

### • Separación de servicios y bases de datos
Cada microservicio está contenido en su propio contenedor Docker y tiene su propia base de datos. Esto asegura independencia total en términos de almacenamiento y despliegue. La comunicación entre ellos se realiza mediante HTTP y sigue el estándar JSON:API con autenticación por API Key.

### • Elección de base de datos
Se eligió PostgreSQL por ser un motor relacional robusto, confiable y ampliamente adoptado en producción. La estructura de los datos es estrictamente relacional, por lo que optar por un modelo SQL garantiza integridad y consultas eficientes.

No se usó NoSQL porque:
• La relación entre entidades es clara y estructurada  
• Se requiere consistencia fuerte al momento de registrar una compra  
• No existen necesidades de esquemas dinámicos, documentos, ni grandes volúmenes no estructurados

### • Estructura del proyecto y uso de Git
Se aplicó una estrategia de Git Flow simplificada, con ramas `main` y `dev`. No se crearon ramas `feature/` individuales debido al límite de tiempo de 24 horas, priorizando agilidad y claridad. Los commits siguen una convención clara utilizando los prefijos: `feat`, `fix`, `docs`, `refactor` y `test` según el tipo de cambio realizado.

### • Uso de herramientas de IA
Se utilizó ChatGPT para acelerar tareas repetitivas como generación de pruebas, descripciones iniciales de endpoints, y esquemas de documentación. Todo el contenido generado fue revisado manualmente: se reescribió cada fragmento adaptándolo al contexto del proyecto, asegurando que el estilo, los nombres y la lógica se alinearan con el resto del código. En el caso de pruebas, se validó línea por línea su ejecución, cobertura y comportamiento esperado antes de integrarlas al repositorio.