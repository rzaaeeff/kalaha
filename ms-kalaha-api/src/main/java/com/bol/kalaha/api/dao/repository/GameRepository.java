package com.bol.kalaha.api.dao.repository;

import com.bol.kalaha.api.dao.document.GameDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<GameDocument, String> {
}
