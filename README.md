# Comunicación entre Microservicios — OpenFeign + Eureka
### Opción A — Comunicación Síncrona | Spring Boot 3.5.14 | Java 17 | Maven

---

## ¿Qué se hizo?

Se implementaron **3 proyectos Spring Boot** que forman una arquitectura de microservicios real:

| Proyecto | Puerto | Rol |
|---|---|---|
| `eureka-server` | 8761 | Servidor de registro y descubrimiento |
| `microservicio-usuarios` | 8081 | Expone datos de usuarios |
| `microservicio-pedidos` | 8082 | Consume a usuarios vía OpenFeign |

La comunicación es **síncrona**: cuando alguien consulta un pedido, el microservicio de pedidos llama automáticamente al microservicio de usuarios usando **OpenFeign**, espera la respuesta y devuelve un JSON combinado.

---

## ¿Cómo se comunican?

```
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
```

Eureka actúa como "directorio": cada microservicio se registra ahí con su nombre, y OpenFeign usa ese nombre para encontrarlos sin necesidad de escribir URLs fijas.

---

## ¿Por qué esto es comunicación síncrona?

Porque `microservicio-pedidos` **espera** la respuesta de `microservicio-usuarios` antes de continuar. Si usuarios no responde, pedidos tampoco responde. El cliente queda bloqueado hasta obtener resultado.

Esto contrasta con la comunicación **asíncrona** (RabbitMQ, Kafka), donde el servicio envía un mensaje y continúa sin esperar respuesta.

---

## Cómo correr el proyecto

### Requisitos previos
- Java 17
- Maven
- VS Code o cualquier IDE

### Paso 1 — Levantar Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Verificar en: `http://localhost:8761`

### Paso 2 — Levantar microservicio-usuarios
```bash
cd microservicio-usuarios
mvn spring-boot:run
```

### Paso 3 — Levantar microservicio-pedidos
```bash
cd microservicio-pedidos
mvn spring-boot:run
```

>  El orden importa. Siempre primero Eureka, luego los microservicios.

---

## Endpoints disponibles

### microservicio-usuarios — puerto 8081

#### Obtener todos los usuarios
```
GET http://localhost:8081/usuarios
```
Respuesta:
```json
[
  { "id": 1, "nombre": "Carlos Pérez", "email": "carlos@email.com" },
  { "id": 2, "nombre": "María López", "email": "maria@email.com" },
  { "id": 3, "nombre": "Juan Torres", "email": "juan@email.com" }
]
```

#### Obtener usuario por ID
```
GET http://localhost:8081/usuarios/1
```
Respuesta:
```json
{ "id": 1, "nombre": "Carlos Pérez", "email": "carlos@email.com" }
```

---

### microservicio-pedidos — puerto 8082

#### Obtener pedido con datos del usuario (comunicación OpenFeign)
```
GET http://localhost:8082/pedidos/1/usuario/2
```
Respuesta:
```json
{
  "id": 1,
  "producto": "Laptop Dell",
  "precio": 1200.0,
  "usuario": {
    "id": 2,
    "nombre": "María López",
    "email": "maria@email.com"
  }
}
```

> Este endpoint demuestra la comunicación entre microservicios. Pedidos llama a usuarios internamente usando OpenFeign. El cliente no sabe que hubo una segunda llamada.

---

## Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Spring Boot | 3.5.14 | Framework base |
| Spring Cloud OpenFeign | Comunicación entre servicios |
| Spring Cloud Netflix Eureka | Registro y descubrimiento |
| Java | 17 | Lenguaje |
| Maven | Gestión de dependencias |

---

## Comprensión del escenario 

**¿Cómo se comunican los microservicios aquí?**
El microservicio de pedidos no tiene la URL de usuarios escrita en el código. En cambio, usa OpenFeign con el nombre del servicio (`microservicio-usuarios`), y Eureka resuelve automáticamente en qué puerto está corriendo. Esto permite que la arquitectura sea flexible y escalable.

**¿Por qué comunicación síncrona y no asíncrona?**
En este escenario, pedidos necesita los datos del usuario **en el mismo momento** para armar la respuesta. No puede "enviar un mensaje y continuar" porque el cliente está esperando la respuesta completa. Por eso la comunicación síncrona es la correcta aquí.

**¿Cuándo usarías asíncrona?**
Si el escenario fuera: "cuando se crea un pedido, notificar al usuario por email", ahí no necesitas esperar — puedes enviar el mensaje a una cola (RabbitMQ o Kafka) y el servicio de notificaciones lo procesa cuando pueda.
