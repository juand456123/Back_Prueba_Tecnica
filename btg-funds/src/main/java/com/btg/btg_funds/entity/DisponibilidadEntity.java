package com.btg.btg_funds.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "disponibilidad", schema = "btg")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadEntity {

    @EmbeddedId
    private DisponibilidadId id;
}
