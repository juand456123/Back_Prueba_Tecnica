package com.btg.btg_funds.repository;

import com.btg.btg_funds.document.NotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationDocument, String> {
}
