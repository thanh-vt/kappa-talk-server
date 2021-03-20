package com.kappa.service.impl;

import com.kappa.constant.CommonConstant;
import com.kappa.service.DraftStoreService;
import com.kappa.model.entity.ChatInfo;
import com.kappa.model.entity.Conversation;
import com.kappa.model.entity.DraftMessageBlock;
import com.kappa.model.entity.HistoryMessageBlock;
import com.kappa.model.entity.Message;
import com.kappa.repositories.ConversationRepository;
import com.kappa.repositories.HistoryMessageBlockRepository;
import com.kappa.service.ConversationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
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

    private final DraftStoreService draftStoreService;

    @Autowired
    public ConversationServiceImpl(ConversationRepository conversationRepository,
        HistoryMessageBlockRepository historyMessageBlockRepository,
        DraftStoreService draftStoreService) {
        this.conversationRepository = conversationRepository;
        this.historyMessageBlockRepository = historyMessageBlockRepository;
        this.draftStoreService = draftStoreService;
    }

    @Override
    public ChatInfo getChatInfo(String fromUser, String toUser) throws InterruptedException {

        String[] users = new String[]{fromUser, toUser};
        Conversation conversation = this.getConversation(users);
        String conversationId = conversation.getId();
        Map<String, Object> conversationMap = this.draftStoreService.getMap(CommonConstant.DRAFT_MAP_NAME);
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
                    Arrays.asList(historyMessageBlockOptional.get(), draft));
            } else {
                chatInfo.setMessageBlockList(Collections.singletonList(draft));
            }
        } else {
            chatInfo.setMessageBlockList(Collections.singletonList(draft));
        }
        return chatInfo;
    }

    @Override
    public synchronized void updateDraft(Message message) throws InterruptedException {
        String[] users = new String[]{message.getFrom(), message.getTo()};
        String conversationId = message.getConversationId();
        Map<String, Object> conversationMap = this.draftStoreService.getMap(CommonConstant.DRAFT_MAP_NAME);
        DraftMessageBlock draft = getLatestBlock(users, conversationId, conversationMap);
        draft.getMessages().add(message);
        this.draftStoreService.updateDraft(conversationMap, conversationId, draft);
    }

    private synchronized DraftMessageBlock getLatestBlock(String[] users, String conversationId,
        Map<String, Object> conversationMap) throws InterruptedException {
        DraftMessageBlock draft = (DraftMessageBlock) conversationMap.get(conversationId);
        if (draft == null) {
            draft = createNewDraft(users, conversationId, conversationMap);
        } else if (draft.getMessages().size() >= 20) {
            historyMessageBlockRepository.save(draft.toHistory());
            draft = createNewDraft(users, conversationId, conversationMap);
        }
        return draft;
    }

    private DraftMessageBlock createNewDraft(String[] users, String conversationId,
        Map<String, Object> conversationMap) throws InterruptedException {
        DraftMessageBlock draft;
        draft = new DraftMessageBlock();
        draft.setConversationId(conversationId);
        draft.setMessages(Collections.synchronizedList(new ArrayList<>()));
        draft.setUsers(users);
        draft.setStartTime(new Date());
        this.draftStoreService.updateDraft(conversationMap, conversationId, draft);
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
