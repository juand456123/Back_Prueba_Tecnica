package com.btg.btg_funds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitanId implements Serializable {

    @Column(name = "id_sucursal")
    private Long idSucursal;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "fecha_visita")
    private LocalDate fechaVisita;
}
