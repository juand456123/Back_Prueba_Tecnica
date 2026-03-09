SET search_path TO btg;

ALTER TABLE inscripcion
    ADD CONSTRAINT fk_inscripcion_producto
        FOREIGN KEY (id_producto) REFERENCES producto(id),
    ADD CONSTRAINT fk_inscripcion_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id);

ALTER TABLE disponibilidad
    ADD CONSTRAINT fk_disponibilidad_sucursal
        FOREIGN KEY (id_sucursal) REFERENCES sucursal(id),
    ADD CONSTRAINT fk_disponibilidad_producto
        FOREIGN KEY (id_producto) REFERENCES producto(id);

ALTER TABLE visitan
    ADD CONSTRAINT fk_visitan_sucursal
        FOREIGN KEY (id_sucursal) REFERENCES sucursal(id),
    ADD CONSTRAINT fk_visitan_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id);

ALTER TABLE producto
    ADD CONSTRAINT ck_producto_tipo
        CHECK (tipo_producto IN ('FPV', 'FIC', 'OTRO'));