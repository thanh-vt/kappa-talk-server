package com.kappa.repository;

import com.kappa.model.HistoryMessageBlock;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryMessageBlockRepository extends MongoRepository<HistoryMessageBlock, String> {

    Optional<HistoryMessageBlock> findFirstByConversationIdOrderByStartTimeDesc(Long conversationId);
}
