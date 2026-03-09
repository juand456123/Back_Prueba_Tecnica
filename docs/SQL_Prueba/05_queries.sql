SET search_path TO btg;
-- sin producto
SELECT DISTINCT
    c.nombre,
    c.apellidos
FROM cliente c
JOIN inscripcion i
    ON i.id_cliente = c.id
WHERE EXISTS (
    SELECT 1
    FROM disponibilidad d
    WHERE d.id_producto = i.id_producto
)
AND NOT EXISTS (
    SELECT 1
    FROM disponibilidad d
    WHERE d.id_producto = i.id_producto
      AND NOT EXISTS (
          SELECT 1
          FROM visitan v
          WHERE v.id_cliente = c.id
            AND v.id_sucursal = d.id_sucursal
      )
);

--Con productos
SELECT DISTINCT
    c.id AS cliente_id,
    c.nombre,
    c.apellidos,
    p.id AS producto_id,
    p.nombre AS producto
FROM cliente c
JOIN inscripcion i
    ON i.id_cliente = c.id
JOIN producto p
    ON p.id = i.id_producto
WHERE NOT EXISTS (
    SELECT 1
    FROM disponibilidad d
    WHERE d.id_producto = p.id
      AND NOT EXISTS (
          SELECT 1
          FROM visitan v
          WHERE v.id_cliente = c.id
            AND v.id_sucursal = d.id_sucursal
      )
);