package com.btg.btg_funds.repository;

import com.btg.btg_funds.entity.VisitanEntity;
import com.btg.btg_funds.entity.VisitanId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitanRepository extends JpaRepository<VisitanEntity, VisitanId> {
    
}
