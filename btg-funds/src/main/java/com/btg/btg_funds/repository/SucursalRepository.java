package com.btg.btg_funds.repository;

import com.btg.btg_funds.entity.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<SucursalEntity, Long> {
}
