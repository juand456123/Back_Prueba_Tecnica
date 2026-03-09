# BTG Funds API

API desarrollada como solución para la **Prueba Técnica Backend -- BTG
Pactual**.\
El sistema permite a los clientes gestionar sus fondos de inversión sin
necesidad de contactar a un asesor.

La aplicación permite:

-   Suscribirse a fondos de inversión
-   Cancelar suscripciones
-   Consultar historial de transacciones
-   Enviar notificaciones por **Email o SMS** según la preferencia del
    cliente

La solución está construida con **Spring Boot**, desplegada en **AWS
EC2**, utilizando **MongoDB Atlas** y **PostgreSQL (Supabase)**.

------------------------------------------------------------------------

# API desplegada

La aplicación se encuentra desplegada en AWS EC2:

http://3.143.228.168:8080

Ejemplo de endpoint:

http://3.143.228.168:8080/api/funds

Puedes probar la API directamente desde **Postman** o **curl**.

------------------------------------------------------------------------

# Repositorio

Código fuente disponible en:

https://github.com/juand456123/Back_Prueba_Tecnica

------------------------------------------------------------------------

# Tecnologías utilizadas

## Backend

-   Java 21
-   Spring Boot 4
-   Spring Security
-   JWT Authentication
-   Spring Data MongoDB
-   Spring Data JPA
-   Maven

## Bases de datos

### MongoDB Atlas

Utilizada para almacenar:

-   clientes
-   fondos
-   suscripciones
-   transacciones
-   notificaciones

### PostgreSQL (Supabase)

Utilizada para persistencia relacional.

------------------------------------------------------------------------

# Servicios externos

### SendGrid

Servicio utilizado para el envío de **notificaciones por correo
electrónico**.

### Twilio

Servicio utilizado para el envío de **notificaciones SMS**.

------------------------------------------------------------------------

# Infraestructura

La aplicación se encuentra desplegada en **AWS EC2**.

La infraestructura fue aprovisionada utilizando:

-   AWS EC2
-   Terraform
-   Linux (Amazon Linux)
-   systemd

La aplicación se ejecuta como servicio del sistema:

btg-funds.service

El artefacto de la aplicación se encuentra en:

/opt/btg-funds/btg-funds.jar

------------------------------------------------------------------------

# Arquitectura del proyecto

El proyecto sigue una arquitectura basada en **capas (Layered
Architecture)** separando responsabilidades.

src/main/java/com/btg/btg_funds

config → Configuración de la aplicación\
controller → Controladores REST\
document → Modelos MongoDB\
entity → Entidades JPA\
dto → Objetos de transferencia de datos\
repository → Acceso a datos\
service → Lógica de negocio\
security → Configuración JWT y seguridad\
exception → Manejo global de errores\
notification → Integración con SendGrid y Twilio

Clase principal de arranque:

BtgFundsApplication.java

------------------------------------------------------------------------

# Estructura del proyecto

BTG-FUNDS │ ├── infra │ ├── src │ └── main │ └── java/com/btg/btg_funds
│ ├── config │ ├── controller │ ├── document │ ├── dto │ ├── entity │
├── exception │ ├── notification │ ├── repository │ ├── security │ ├──
service │ └── BtgFundsApplication.java │ ├── pom.xml └── README.md

------------------------------------------------------------------------

# Reglas de negocio implementadas

## Saldo inicial

Cada cliente inicia con un saldo disponible de:

COP \$500.000

------------------------------------------------------------------------

## Suscripción a fondos

Para suscribirse a un fondo:

-   el cliente debe tener saldo suficiente
-   cada fondo tiene un monto mínimo de vinculación

Si el cliente no tiene saldo suficiente se retorna el mensaje:

No tiene saldo disponible para vincularse al fondo
`<Nombre del fondo>`{=html}

------------------------------------------------------------------------

## Cancelación de suscripción

Cuando el cliente cancela su suscripción:

-   el dinero invertido se retorna al saldo disponible del cliente

------------------------------------------------------------------------

## Notificaciones

Cuando un cliente se suscribe a un fondo se envía una notificación
mediante:

-   Email (SendGrid)
-   SMS (Twilio)

dependiendo de la preferencia configurada por el cliente.

------------------------------------------------------------------------

# Endpoints principales

## Obtener fondos disponibles

GET /api/funds

Ejemplo:

GET http://3.143.228.168:8080/api/funds

------------------------------------------------------------------------

## Suscribirse a un fondo

POST /api/subscriptions

Request:

{ "clientId": "123", "fundId": "456" }

------------------------------------------------------------------------

## Cancelar suscripción

DELETE /api/subscriptions

Request:

{ "clientId": "123", "fundId": "456" }

------------------------------------------------------------------------

## Consultar historial de transacciones

GET /api/subscriptions/transactions/{clientId}

Ejemplo:

GET /api/subscriptions/transactions/123

------------------------------------------------------------------------

# Ejecutar el proyecto localmente

## 1. Clonar el repositorio

git clone https://github.com/juand456123/Back_Prueba_Tecnica

------------------------------------------------------------------------

## 2. Entrar al proyecto

cd Back_Prueba_Tecnica

------------------------------------------------------------------------

## 3. Compilar el proyecto

mvn clean install

------------------------------------------------------------------------

## 4. Ejecutar la aplicación

mvn spring-boot:run

o ejecutando el jar:

java -jar target/btg-funds-0.0.1-SNAPSHOT.jar

------------------------------------------------------------------------

# Comandos útiles en la instancia EC2

## Ver estado del servicio

sudo systemctl status btg-funds

------------------------------------------------------------------------

## Ver logs de la aplicación

sudo journalctl -u btg-funds -f

------------------------------------------------------------------------

## Reiniciar el servicio

sudo systemctl restart btg-funds

------------------------------------------------------------------------

# Seguridad

La API implementa seguridad basada en:

-   Spring Security
-   JWT Authentication
-   Filtros de autenticación

------------------------------------------------------------------------

# Autor

Juan Diego Guzmán Herrera\
Backend Developer
