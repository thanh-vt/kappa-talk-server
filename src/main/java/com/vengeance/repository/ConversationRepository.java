package com.vengeance.repository;

import com.vengeance.model.Conversation;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, Long> {

    @Query("{ 'users' : { $all: ?0 } }")
    Optional<Conversation> getConversationByUsers(String[] users);

    @Query("{ 'users' : ?0 }")
    Optional<Conversation> getConversationBySelf(String[] users);

}
