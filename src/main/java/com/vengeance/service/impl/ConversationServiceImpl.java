package com.vengeance.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.vengeance.model.ChatInfo;
import com.vengeance.model.Conversation;
import com.vengeance.model.DraftMessageBlock;
import com.vengeance.model.HistoryMessageBlock;
import com.vengeance.model.Message;
import com.vengeance.repository.ConversationRepository;
import com.vengeance.repository.HistoryMessageBlockRepository;
import com.vengeance.service.ConversationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationServiceImpl implements ConversationService {

    private static final Logger logger = LogManager.getLogger(ConversationServiceImpl.class);

    private final ConversationRepository conversationRepository;

    private final HistoryMessageBlockRepository historyMessageBlockRepository;

    private final HazelcastInstance hazelcastClientInstance;

    @Autowired
    public ConversationServiceImpl(ConversationRepository conversationRepository,
        HistoryMessageBlockRepository historyMessageBlockRepository,
        HazelcastInstance hazelcastClientInstance) {
        this.conversationRepository = conversationRepository;
        this.historyMessageBlockRepository = historyMessageBlockRepository;
        this.hazelcastClientInstance = hazelcastClientInstance;
    }

    @Override
    public ChatInfo getChatInfo(String fromUser, String toUser) throws InterruptedException {

        String[] users = new String[]{fromUser, toUser};
        Conversation conversation = this.getConversation(users);
        long conversationId = conversation.getId().longValue();
        IMap<Long, Object> conversationMap = this.hazelcastClientInstance.getMap("draft");
        DraftMessageBlock draft = this.getLatestBlock(users, conversationId, conversationMap);
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setConversationId(conversationId);
        chatInfo.setStartTime(conversation.getStartTime());
        chatInfo.setUsers(users);
        if (draft.getMessages().isEmpty()) {
            Optional<HistoryMessageBlock> historyMessageBlockOptional = this.historyMessageBlockRepository
                .findFirstByConversationIdOrderByStartTimeDesc(conversationId);
            if (historyMessageBlockOptional.isPresent()) {
                chatInfo.setMessageBlockList(
                    Collections.singletonList(historyMessageBlockOptional.get()));
            } else {
                chatInfo.setMessageBlockList(new ArrayList<>());
            }
        } else {
            chatInfo.setMessageBlockList(Collections.singletonList(draft));
        }
        return chatInfo;
    }

    @Override
    public synchronized void updateDraft(Message message) throws InterruptedException {

        String[] users = new String[]{message.getFrom(), message.getTo()};
        Conversation conversation = this.getConversation(users);

        long conversationId = conversation.getId().longValue();

        IMap<Long, Object> conversationMap = this.hazelcastClientInstance.getMap("draft");
        DraftMessageBlock draft = getLatestBlock(users, conversationId, conversationMap);
        draft.getMessages().add(message);
        if (conversationMap.isLocked(conversationId)) {
            wait(10000);
        } else {
            conversationMap.lock(conversationId);
            conversationMap.replace(conversationId, draft);
            conversationMap.unlock(conversationId);
            notify();
        }
        for (Message message1 : draft.getMessages()) {
            logger.info(message1.getContent());
        }
    }

    private synchronized DraftMessageBlock getLatestBlock(String[] users, long conversationId,
        IMap<Long, Object> conversationMap) throws InterruptedException {
        DraftMessageBlock draft = (DraftMessageBlock) conversationMap.get(conversationId);
        if (draft == null) {
            draft = new DraftMessageBlock();
            draft.setConversationId(conversationId);
            draft.setMessages(Collections.synchronizedList(new ArrayList<>()));
            draft.setUsers(users);
            draft.setStartTime(new Date());
            if (conversationMap.isLocked(conversationId)) {
                wait(10000);
            } else {
                conversationMap.lock(conversationId);
                conversationMap.put(conversationId, draft);
                conversationMap.unlock(conversationId);
                notify();
            }
        } else if (draft.getMessages().size() >= 20) {
            historyMessageBlockRepository.save(draft.toHistory());
            draft = new DraftMessageBlock();
            draft.setConversationId(conversationId);
            draft.setMessages(Collections.synchronizedList(new ArrayList<>()));
            draft.setUsers(users);
            draft.setStartTime(new Date());
            if (conversationMap.isLocked(conversationId)) {
                wait(10000);
            } else {
                conversationMap.lock(conversationId);
                conversationMap.replace(conversationId, draft);
                conversationMap.unlock(conversationId);
                notify();
            }
        }
        return draft;
    }

    private Conversation getConversation(String[] users) {
        Optional<Conversation> conversationOptional;
        if (users[0].equals(users[1])) {
            conversationOptional = this.conversationRepository.getConversationBySelf(users);
        } else {
            conversationOptional = this.conversationRepository.getConversationByUsers(users);
        }
        if (!conversationOptional.isPresent()) {
            Conversation newConversation = new Conversation();
            newConversation.setStartTime(new Date());
            newConversation.setUsers(users);
            this.conversationRepository.save(newConversation);
            return newConversation;
        } else {
            return conversationOptional.get();
        }

    }
}
