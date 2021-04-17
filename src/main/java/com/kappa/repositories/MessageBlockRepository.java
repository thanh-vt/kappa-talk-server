package com.kappa.repositories;

import com.kappa.model.entity.MessageBlock;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageBlockRepository extends MongoRepository<MessageBlock, String>,
    MessageBlockRepositoryCustom {

    Optional<MessageBlock> findFirstByIsDraft(boolean isDraft);

    Optional<MessageBlock> findFirstByConversationIdAndIsDraftOrderByStartTimeDesc(String conversationId, boolean isDraft);



//    List<HistoryMessageBlock> findAllByConversationIdInOrderByStartTimeDesc(
//        Set<String> conversationIds);
}
