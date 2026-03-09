SET search_path TO btg;

CREATE TABLE cliente (
    id              BIGINT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    ciudad          VARCHAR(100) NOT NULL
);

CREATE TABLE producto (
    id              BIGINT PRIMARY KEY,
    nombre          VARCHAR(150) NOT NULL,
    tipo_producto   VARCHAR(50) NOT NULL
);

CREATE TABLE sucursal (
    id              BIGINT PRIMARY KEY,
    nombre          VARCHAR(150) NOT NULL,
    ciudad          VARCHAR(100) NOT NULL
);

CREATE TABLE inscripcion (
    id_producto     BIGINT NOT NULL,
    id_cliente      BIGINT NOT NULL,
    PRIMARY KEY (id_producto, id_cliente)
);

CREATE TABLE disponibilidad (
    id_sucursal     BIGINT NOT NULL,
    id_producto     BIGINT NOT NULL,
    PRIMARY KEY (id_sucursal, id_producto)
);

CREATE TABLE visitan (
    id_sucursal     BIGINT NOT NULL,
    id_cliente      BIGINT NOT NULL,
    fecha_visita    DATE NOT NULL,
    PRIMARY KEY (id_sucursal, id_cliente, fecha_visita)
);