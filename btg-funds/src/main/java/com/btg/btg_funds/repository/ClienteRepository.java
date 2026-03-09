package com.btg.btg_funds.repository;

import com.btg.btg_funds.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    @Query(value = """
            SELECT DISTINCT c.*
            FROM btg.cliente c
            JOIN btg.inscripcion i ON c.id = i.id_cliente
            WHERE NOT EXISTS (
                SELECT 1
                FROM btg.disponibilidad d
                WHERE d.id_producto = i.id_producto
                AND NOT EXISTS (
                    SELECT 1
                    FROM btg.visitan v
                    WHERE v.id_cliente = c.id
                    AND v.id_sucursal = d.id_sucursal
                )
            )
            """, nativeQuery = true)
    List<ClienteEntity> findClientesConProductosDisponiblesEnSucursalesQueVisitan();
}
