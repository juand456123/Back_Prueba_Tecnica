
# BTG Funds API

API desarrollada como solución para la **Prueba Técnica Backend — BTG Pactual**.

> **Disclaimer**  
> “Este repositorio no corresponde a código oficial de BTG Pactual, únicamente corresponde a una prueba técnica personal.”

El sistema permite a los clientes gestionar sus fondos de inversión **sin necesidad de contactar a un asesor**, incluyendo:

- Suscribirse a fondos de inversión
- Cancelar suscripciones
- Consultar historial de transacciones
- Recibir notificaciones por **Email o SMS** según la preferencia del cliente

La solución fue construida con **Spring Boot**, desplegada en **AWS EC2**, utilizando **MongoDB Atlas** y **PostgreSQL (Supabase)**.

---

# Arquitectura de la solución

La solución implementa una arquitectura moderna basada en:

- **Arquitectura en capas (Layered Architecture)**
- **Persistencia híbrida (MongoDB + PostgreSQL)**
- **Seguridad con JWT**
- **CI/CD automatizado**
- **Infraestructura como código (Terraform)**

## Diagrama de arquitectura

![Arquitectura CI/CD](docs/architecture-cicd.png)

---

# Documentación técnica completa

Para una explicación **detallada de la arquitectura, decisiones técnicas y proceso de despliegue**, consulta el documento técnico:

📄 **Documento técnico de arquitectura y despliegue**

[Descargar documentación completa](docs/arquitectura_btg_funds_ci_cd_v2.pdf)

Este documento describe:

- Arquitectura general del sistema
- Arquitectura **n‑capas del backend**
- Pipeline **CI/CD con GitHub Actions**
- Infraestructura con **Terraform**
- Despliegue automatizado en **AWS EC2**
- Persistencia **MongoDB + PostgreSQL**
- Seguridad con **JWT y roles**
- Integraciones externas (**SendGrid y Twilio**)

---

# Scripts SQL (PostgreSQL)

La parte correspondiente a la **solución SQL de la prueba técnica** se encuentra incluida en el repositorio.

Ruta:

docs/SQL_Prueba

## Archivos incluidos

- [00_create_database.sql](docs/SQL_Prueba/00_create_database.sql) → creación de la base de datos  
- [01_create_schema.sql](docs/SQL_Prueba/01_create_schema.sql) → creación del esquema  
- [02_create_tables.sql](docs/SQL_Prueba/02_create_tables.sql) → creación de tablas  
- [03_create_constraints.sql](docs/SQL_Prueba/03_create_constraints.sql) → llaves primarias y foráneas  
- [04_seed_data.sql](docs/SQL_Prueba/04_seed_data.sql) → datos iniciales de prueba  
- [05_queries.sql](docs/SQL_Prueba/05_queries.sql) → consultas solicitadas en la prueba técnica  

## Orden de ejecución

1. [00_create_database.sql](docs/SQL_Prueba/00_create_database.sql)  
2. [01_create_schema.sql](docs/SQL_Prueba/01_create_schema.sql)  
3. [02_create_tables.sql](docs/SQL_Prueba/02_create_tables.sql)  
4. [03_create_constraints.sql](docs/SQL_Prueba/03_create_constraints.sql)  
5. [04_seed_data.sql](docs/SQL_Prueba/04_seed_data.sql)  
6. [05_queries.sql](docs/SQL_Prueba/05_queries.sql)  

Esto permite crear completamente la **estructura relacional utilizada en la solución**.

# Colección de Postman

Puedes importar la colección de Postman para probar todos los endpoints de la API.

📦 [Descargar colección Postman](docs/BTG-Funds-API.postman_collection.json)

## Uso de la colección

### Paso 1 — Generar token JWT

POST /api/auth/login

Ejemplo de request:

{
  "username": "revisar collection",
  "password": "revisar collection"
}

### Paso 2 — Guardar el token

Copiar el token devuelto y asignarlo a la variable:

token

dentro de la colección.

### Paso 3 — Probar endpoints protegidos

Una vez configurado el token podrás probar:

- fondos
- suscripciones
- cancelaciones
- historial de transacciones

### Expiración del token

El token JWT tiene una vigencia de **24 horas**.

---

# API desplegada

La aplicación se despliega automáticamente mediante **GitHub Actions**.

Debido a que el despliegue se realiza sobre instancias **AWS EC2 con IP pública dinámica**, la dirección de acceso puede cambiar cuando la instancia se reinicia.

Por esta razón **la URL del servicio no se fija en el README**.

Durante cada ejecución del **pipeline CI/CD**, el workflow imprime en los logs:

- la **IP pública actual**
- la **URL de acceso al servicio**
- la **URL de Swagger**

Esto permite conocer fácilmente la dirección actual del entorno desplegado.

Ejemplo mostrado en el pipeline:

Swagger:

http://IP_PUBLICA:PUERTO/swagger-ui/index.html#/

---

# Tecnologías utilizadas

## Backend

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data MongoDB
- Spring Data JPA
- Maven
- Swagger / OpenAPI

---

# Bases de datos

## MongoDB Atlas

Utilizada para almacenar información principal del negocio:

- fondos
- clientes
- suscripciones
- transacciones
- notificaciones
- usuarios

## PostgreSQL (Supabase)

Utilizada para la parte relacional del ejercicio SQL.

---

# Servicios externos

## SendGrid

Servicio utilizado para el envío de **notificaciones por correo electrónico**.

## Twilio

Servicio utilizado para el envío de **notificaciones SMS**.

---

# Arquitectura CI/CD

El pipeline automatiza completamente el despliegue de la aplicación.

### Flujo del pipeline

1. Push al repositorio
2. GitHub Actions ejecuta el pipeline
3. Build del proyecto con Maven
4. Ejecución de pruebas
5. Generación del artefacto `.jar`
6. Conexión SSH a la instancia EC2
7. Copia del nuevo artefacto
8. Reinicio automático del servicio

La aplicación se ejecuta como servicio Linux mediante:

systemd

Servicio:

btg-funds.service

Ubicación del artefacto:

/opt/btg-funds/btg-funds.jar

---

# Arquitectura del proyecto

El proyecto sigue una **arquitectura en capas (Layered Architecture)**.

src/main/java/com/btg/btg_funds

config → configuración de la aplicación  
controller → controladores REST  
document → modelos MongoDB  
entity → entidades JPA  
dto → objetos de transferencia de datos  
repository → acceso a datos  
service → lógica de negocio  
security → autenticación JWT  
exception → manejo global de errores  
notification → integración SendGrid y Twilio

Clase principal:

BtgFundsApplication.java

---

# Reglas de negocio implementadas

## Saldo inicial

Cada cliente inicia con:

COP $500.000

---

## Suscripción a fondos

Para suscribirse a un fondo:

- el cliente debe tener saldo suficiente
- cada fondo tiene un monto mínimo de vinculación

Si no hay saldo suficiente:

No tiene saldo disponible para vincularse al fondo <Nombre del fondo>

---

## Cancelación de suscripción

Cuando el cliente cancela su suscripción:

- el dinero invertido se retorna al saldo disponible del cliente

---

## Notificaciones

Cuando un cliente se suscribe a un fondo se envía una notificación mediante:

- Email
- SMS

según la preferencia configurada por el cliente.

---

# Endpoints principales

## Obtener fondos disponibles

GET /api/funds

---

## Suscribirse a un fondo

POST /api/subscriptions

Request:

{
  "clientId": "123",
  "fundId": "456"
}

---

## Cancelar suscripción

DELETE /api/subscriptions

---

## Consultar historial de transacciones

GET /api/subscriptions/transactions/{clientId}

---

# Ejecutar el proyecto localmente

## Clonar repositorio

git clone https://github.com/juand456123/Back_Prueba_Tecnica

## Entrar al proyecto

cd Back_Prueba_Tecnica

## Compilar

mvn clean install

## Ejecutar

mvn spring-boot:run

o

java -jar target/btg-funds-0.0.1-SNAPSHOT.jar

---

# Comandos útiles en EC2

Ver estado del servicio

sudo systemctl status btg-funds

Ver logs

sudo journalctl -u btg-funds -f

Reiniciar servicio

sudo systemctl restart btg-funds

---

# Seguridad

La API implementa:

- Spring Security
- JWT Authentication
- Roles de usuario
- Filtros de autenticación

Roles definidos:

ADMIN  
CLIENT

---

# Autor

Juan Diego Guzmán Herrera  
Backend Developer
