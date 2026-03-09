package com.btg.btg_funds.repository;

import com.btg.btg_funds.entity.InscripcionEntity;
import com.btg.btg_funds.entity.InscripcionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepository extends JpaRepository<InscripcionEntity, InscripcionId> {
}
