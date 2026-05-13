Comunicación entre Microservicios — OpenFeign + Eureka
Opción A — Comunicación Síncrona | Spring Boot 3.3.x | Java 17 | Maven

¿Qué se hizo?
Se implementaron 3 proyectos Spring Boot que forman una arquitectura de microservicios real:
ProyectoPuertoRoleureka-server8761Servidor de registro y descubrimientomicroservicio-usuarios8081Expone datos de usuariosmicroservicio-pedidos8082Consume a usuarios vía OpenFeign
La comunicación es síncrona: cuando alguien consulta un pedido, el microservicio de pedidos llama automáticamente al microservicio de usuarios usando OpenFeign, espera la respuesta y devuelve un JSON combinado.

¿Cómo se comunican?
Cliente (Postman)
      |
      v
microservicio-pedidos (8082)
      |
      |  OpenFeign llama automáticamente
      v
microservicio-usuarios (8081)
      |
      v
Devuelve datos del usuario
      |
      v
pedidos combina y responde al cliente
Eureka actúa como "directorio": cada microservicio se registra ahí con su nombre, y OpenFeign usa ese nombre para encontrarlos sin necesidad de escribir URLs fijas.

¿Por qué esto es comunicación síncrona?
Porque microservicio-pedidos espera la respuesta de microservicio-usuarios antes de continuar. Si usuarios no responde, pedidos tampoco responde. El cliente queda bloqueado hasta obtener resultado.
Esto contrasta con la comunicación asíncrona (RabbitMQ, Kafka), donde el servicio envía un mensaje y continúa sin esperar respuesta.

Estructura de archivos
eureka-server/
└── src/main/
    ├── java/com/demo/eurekaserver/
    │   └── EurekaServerApplication.java
    └── resources/
        └── application.properties

microservicio-usuarios/
└── src/main/
    ├── java/com/demo/microserviciousuarios/
    │   ├── controller/UsuarioController.java
    │   ├── service/UsuarioService.java
    │   ├── model/Usuario.java
    │   └── MicroservicioUsuariosApplication.java
    └── resources/
        └── application.properties

microservicio-pedidos/
└── src/main/
    ├── java/com/demo/microserviciopedidos/
    │   ├── controller/PedidoController.java
    │   ├── service/PedidoService.java
    │   ├── model/Pedido.java
    │   ├── model/Usuario.java
    │   ├── client/UsuarioClient.java
    │   └── MicroservicioPedidosApplication.java
    └── resources/
        └── application.properties

Cómo correr el proyecto
Requisitos previos

Java 17
Maven
VS Code o cualquier IDE

Paso 1 — Levantar Eureka Server
bashcd eureka-server
mvn spring-boot:run
Verificar en: http://localhost:8761
Paso 2 — Levantar microservicio-usuarios
bashcd microservicio-usuarios
mvn spring-boot:run
Paso 3 — Levantar microservicio-pedidos
bashcd microservicio-pedidos
mvn spring-boot:run

⚠️ El orden importa. Siempre primero Eureka, luego los microservicios.


Endpoints disponibles
microservicio-usuarios — puerto 8081
Obtener todos los usuarios
GET http://localhost:8081/usuarios
Respuesta:
json[
  { "id": 1, "nombre": "Carlos Pérez", "email": "carlos@email.com" },
  { "id": 2, "nombre": "María López", "email": "maria@email.com" },
  { "id": 3, "nombre": "Juan Torres", "email": "juan@email.com" }
]
Obtener usuario por ID
GET http://localhost:8081/usuarios/1
Respuesta:
json{ "id": 1, "nombre": "Carlos Pérez", "email": "carlos@email.com" }

microservicio-pedidos — puerto 8082
Obtener pedido con datos del usuario (comunicación OpenFeign)
GET http://localhost:8082/pedidos/1/usuario/2
Respuesta:
json{
  "id": 1,
  "producto": "Laptop Dell",
  "precio": 1200.0,
  "usuario": {
    "id": 2,
    "nombre": "María López",
    "email": "maria@email.com"
  }
}

Este endpoint demuestra la comunicación entre microservicios. Pedidos llama a usuarios internamente usando OpenFeign. El cliente no sabe que hubo una segunda llamada.


Tecnologías utilizadas
TecnologíaVersiónUsoSpring Boot3.3.xFramework baseSpring Cloud OpenFeign2023.0.xComunicación entre serviciosSpring Cloud Netflix Eureka2023.0.xRegistro y descubrimientoJava17LenguajeMaven3.xGestión de dependencias

Qué cumple del deber
RequisitoCumplidoSpring Boot✅Maven✅Controller en ambos microservicios✅Service en ambos microservicios✅application.properties configurado✅2 microservicios que se comunican✅Comunicación síncrona con OpenFeign✅Descubrimiento de servicios con Eureka✅Evidencias de funcionamiento✅

Comprensión del escenario — Observación del profesor
¿Cómo se comunican los microservicios aquí?
El microservicio de pedidos no tiene la URL de usuarios escrita en el código. En cambio, usa OpenFeign con el nombre del servicio (microservicio-usuarios), y Eureka resuelve automáticamente en qué puerto está corriendo. Esto permite que la arquitectura sea flexible y escalable.
¿Por qué comunicación síncrona y no asíncrona?
En este escenario, pedidos necesita los datos del usuario en el mismo momento para armar la respuesta. No puede "enviar un mensaje y continuar" porque el cliente está esperando la respuesta completa. Por eso la comunicación síncrona es la correcta aquí.
¿Cuándo usarías asíncrona?
Si el escenario fuera: "cuando se crea un pedido, notificar al usuario por email", ahí no necesitas esperar — puedes enviar el mensaje a una cola (RabbitMQ o Kafka) y el servicio de notificaciones lo procesa cuando pueda.

Autor
Trabajo práctico — Comunicación entre Microservicios en Spring Boot
Materia: Arquitectura de Microservicios
