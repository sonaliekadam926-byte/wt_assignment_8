package com.cricket.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.cricket.model.MatchDocument;

public interface MatchRepository extends MongoRepository<MatchDocument, String> {
    List<MatchDocument> findByStatusOrderByCreatedAtDesc(String status);
}
