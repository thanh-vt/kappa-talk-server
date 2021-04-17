package com.kappa.repositories;

import com.kappa.model.entity.Conversation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ConversationRepository extends MongoRepository<Conversation, String>, ConversationRepositoryCustom {

    @Query("{ 'users' : ?0 }")
    List<Conversation> getConversationByUser(String user);

    @Query("{ 'users' : { $all: ?0 } }")
    Optional<Conversation> getConversationByUsers(String[] users);

    @Query("{ 'users' : ?0 }")
    Optional<Conversation> getConversationBySelf(String[] users);

}
