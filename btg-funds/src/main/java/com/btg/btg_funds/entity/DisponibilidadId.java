package com.btg.btg_funds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadId implements Serializable {

    @Column(name = "id_sucursal")
    private Long idSucursal;

    @Column(name = "id_producto")
    private Long idProducto;
}
