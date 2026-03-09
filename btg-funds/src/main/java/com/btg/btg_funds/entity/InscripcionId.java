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
public class InscripcionId implements Serializable {

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "id_cliente")
    private Long idCliente;
}
