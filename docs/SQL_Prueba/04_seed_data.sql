SET search_path TO btg;

INSERT INTO cliente (id, nombre, apellidos, ciudad) VALUES
(1, 'Juan', 'Perez', 'Bogota'),
(2, 'Maria', 'Gomez', 'Medellin'),
(3, 'Carlos', 'Lopez', 'Cali');

INSERT INTO producto (id, nombre, tipo_producto) VALUES
(1, 'FPV_BTG_PACTUAL_RECAUDADORA', 'FPV'),
(2, 'FPV_BTG_PACTUAL_ECOPETROL', 'FPV'),
(3, 'DEUDAPRIVADA', 'FIC');

INSERT INTO sucursal (id, nombre, ciudad) VALUES
(1, 'Sucursal Norte', 'Bogota'),
(2, 'Sucursal Centro', 'Bogota'),
(3, 'Sucursal Sur', 'Medellin');

INSERT INTO inscripcion (id_producto, id_cliente) VALUES
(1, 1),
(2, 1),
(3, 2),
(1, 3);

INSERT INTO disponibilidad (id_sucursal, id_producto) VALUES
(1, 1),
(2, 1),
(1, 2),
(3, 3);

INSERT INTO visitan (id_sucursal, id_cliente, fecha_visita) VALUES
(1, 1, '2025-06-01'),
(2, 1, '2025-06-05'),
(3, 2, '2025-06-03'),
(1, 3, '2025-06-02');