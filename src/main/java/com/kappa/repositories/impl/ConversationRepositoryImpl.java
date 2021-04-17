package com.kappa.repositories.impl;

import com.kappa.model.entity.Conversation;
import com.kappa.repositories.ConversationRepositoryCustom;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class ConversationRepositoryImpl implements ConversationRepositoryCustom {

    private final MongoOperations mongoOperations;

    @Autowired
    public ConversationRepositoryImpl(
        MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void updateConversation(String conversationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(conversationId));
        Update update = new Update();
        update.set("lastUpdate", new Date());
        this.mongoOperations.updateMulti(query, update, Conversation.class);
    }
}
