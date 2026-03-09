package com.btg.btg_funds.repository;

import com.btg.btg_funds.document.FundDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundRepository extends MongoRepository<FundDocument, String> {

    List<FundDocument> findByActiveTrue();

}
