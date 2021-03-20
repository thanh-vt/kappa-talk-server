package com.kappa.repositories;

import com.kappa.model.entity.HistoryMessageBlock;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryMessageBlockRepository extends MongoRepository<HistoryMessageBlock, String> {

    Optional<HistoryMessageBlock> findFirstByConversationIdOrderByStartTimeDesc(String conversationId);
}
