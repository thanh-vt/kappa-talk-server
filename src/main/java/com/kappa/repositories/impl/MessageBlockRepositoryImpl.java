package com.kappa.repositories.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;

import com.kappa.model.entity.MessageBlock;
import com.kappa.repositories.MessageBlockRepositoryCustom;
import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class MessageBlockRepositoryImpl implements
    MessageBlockRepositoryCustom {

    private final MongoOperations mongoOperations;

    @Autowired
    public MessageBlockRepositoryImpl(
        MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<MessageBlock> findLastMessageBlock(
        Set<String> conversationIds, boolean isDraft) {
        MatchOperation matchOperation = Aggregation.match(new Criteria("conversationId")
            .in(conversationIds).and("isDraft").is(isDraft));
        GroupOperation groupByIdAndStartTime = group("_id").last("startTime")
            .as("startTime").last(Aggregation.ROOT).as("lastMsgBlock");
        ReplaceRootOperation projectionOperation = Aggregation.replaceRoot().withValueOf("lastMsgBlock");
        Aggregation aggregation
            = Aggregation.newAggregation(matchOperation, groupByIdAndStartTime, projectionOperation);
        AggregationResults<MessageBlock> output
            = this.mongoOperations.aggregate(aggregation, "block", MessageBlock.class);
        return output.getMappedResults();
    }

    @Override
    public Map<String, List<MessageBlock>> findLastMessageBlocks(Set<String> conversationIds,
        boolean isDraft) {
        MatchOperation matchOperation = Aggregation.match(new Criteria("conversationId")
            .in(conversationIds));
        SortOperation sortOperation = Aggregation.sort(Direction.ASC, "startTime");
        GroupOperation groupByIdAndStartTime = Aggregation.group("conversationId").push(Aggregation.ROOT).as("lastMsgBlocks");
        ProjectionOperation projectionOperation = Aggregation.project().and("_id").as("conversationId")
            .and("lastMsgBlocks").slice(-3).as("last3Blocks");
//            .andInclude("conversationId", "last3Blocks");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortOperation, groupByIdAndStartTime, projectionOperation);
        AggregationResults<BasicDBObject> output
            = this.mongoOperations.aggregate(aggregation, "block", BasicDBObject.class);
        return output.getMappedResults().stream().collect(Collectors.toMap(e -> e.getString("conversationId"), e -> {
            List<Document> docs = (List<Document>) e.get("last3Blocks");
            List<MessageBlock> messageBlocks = new ArrayList<>();
            docs.forEach(doc -> {
                MessageBlock messageBlock = this.mongoOperations.getConverter().read(MessageBlock.class, doc);
                messageBlocks.add(messageBlock);
            });
            return messageBlocks;
        }));
    }

    @Override
    public List<MessageBlock> findLastMessageBlocks(String conversationId, boolean isDraft) {
        MatchOperation matchOperation = Aggregation.match(new Criteria("conversationId")
            .is(conversationId));
        SortOperation sortOperation = Aggregation.sort(Direction.ASC, "startTime");
        GroupOperation groupByIdAndStartTime = Aggregation.group("conversationId").push(Aggregation.ROOT).as("lastMsgBlocks");
        ProjectionOperation projectionOperation = Aggregation.project().and("_id").as("conversationId")
            .and("lastMsgBlocks").slice(-3).as("last3Blocks");
//            .andInclude("conversationId", "last3Blocks");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortOperation, groupByIdAndStartTime, projectionOperation);
        AggregationResults<BasicDBObject> output
            = this.mongoOperations.aggregate(aggregation, "block", BasicDBObject.class);
        return output.getMappedResults().stream().findFirst().map(e -> {
            List<Document> docs = (List<Document>) e.get("last3Blocks");
            List<MessageBlock> messageBlocks = new ArrayList<>();
            docs.forEach(doc -> {
                MessageBlock messageBlock = this.mongoOperations.getConverter().read(MessageBlock.class, doc);
                messageBlocks.add(messageBlock);
            });
            return messageBlocks;
        }).orElse(new ArrayList<>());
    }
}
