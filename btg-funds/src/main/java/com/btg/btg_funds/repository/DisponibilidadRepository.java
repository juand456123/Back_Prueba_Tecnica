package com.btg.btg_funds.repository;

import com.btg.btg_funds.entity.DisponibilidadEntity;
import com.btg.btg_funds.entity.DisponibilidadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadRepository extends JpaRepository<DisponibilidadEntity, DisponibilidadId> {
}
