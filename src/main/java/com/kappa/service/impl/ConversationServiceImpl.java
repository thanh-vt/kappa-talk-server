package com.kappa.service.impl;

import com.kappa.constant.CommonConstant;
import com.kappa.model.entity.Conversation;
import com.kappa.model.entity.Message;
import com.kappa.model.entity.MessageBlock;
import com.kappa.repositories.ConversationRepository;
import com.kappa.repositories.MessageBlockRepository;
import com.kappa.service.ConversationService;
import com.kappa.service.DraftStoreService;
import com.kappa.util.Visualizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ConversationServiceImpl implements ConversationService {

    private final SimpUserRegistry simpUserRegistry;

    private final ConversationRepository conversationRepository;

    private final MessageBlockRepository messageBlockRepository;

    private final DraftStoreService draftStoreService;

    @Autowired
    public ConversationServiceImpl(
        SimpUserRegistry simpUserRegistry,
        ConversationRepository conversationRepository,
        MessageBlockRepository messageBlockRepository,
        DraftStoreService draftStoreService) {
        this.simpUserRegistry = simpUserRegistry;
        this.conversationRepository = conversationRepository;
        this.messageBlockRepository = messageBlockRepository;
        this.draftStoreService = draftStoreService;
    }

    @Override
    public Map<String, Conversation> getConversations(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Conversation> conversationList =
            this.conversationRepository.getConversationByUser(username)
                .stream()
                .peek(e -> {
                    String chatter;
                    if (username.equals(e.getUsers()[0])) {
                        chatter = e.getUsers()[1];
                    } else {
                        chatter = e.getUsers()[0];
                    }
                    e.setChatterName(chatter);
                    e.setChatterOnline(this.simpUserRegistry.getUsers().stream()
                        .anyMatch(x -> x.getName().equals(chatter)));
                })
                .collect(Collectors.toMap(Conversation::getId, e -> e));
        Map<String, List<MessageBlock>> messageBlockMap = this.messageBlockRepository
            .findLastMessageBlocks(conversationList.keySet(), false);
        Map<String, Object> conversationMap = this.draftStoreService
            .getMap(CommonConstant.DRAFT_MAP_NAME);
        Map<String, Conversation> orderedConversation = new LinkedHashMap<>();
        conversationList.entrySet().stream().sorted((o1, o2) ->
            (o1.getValue().getLastUpdate() != null && o2.getValue().getLastUpdate() != null)
                ? (int) (o1.getValue().getLastUpdate().getTime() - o2.getValue()
                .getLastUpdate().getTime()) : 0)
            .peek(e -> {
                List<MessageBlock> messageBlocks = messageBlockMap.get(e.getKey());
                MessageBlock persistedDraft;
                if (messageBlocks != null && !messageBlocks.isEmpty()) {
                    persistedDraft = messageBlocks.stream()
                        .filter(MessageBlock::isDraft).findFirst().orElse(null);
                } else {
                    persistedDraft = MessageBlock.from(e.getValue().getUsers(), e.getKey(), null);
                    this.messageBlockRepository.save(persistedDraft);
                    messageBlocks = Collections.singletonList(persistedDraft);
                }
                MessageBlock draft = this.getLatestBlock(e.getKey(),
                    conversationMap);
                if (draft != null && persistedDraft != null) {
                    Visualizer.customMessageBlock(draft);
                    messageBlocks.set(messageBlocks.indexOf(persistedDraft), draft);
                }
                e.getValue().setMessageBlockList(messageBlocks);
            })
            .forEachOrdered(x -> orderedConversation.put(x.getKey(), x.getValue()));
        return orderedConversation;
    }

    @Override
    public Conversation getConversation(String id, boolean isFetchMessage,
        boolean updateStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Conversation conversation = this.getConversation(id);
        if (username.equals(conversation.getUsers()[0])) {
            conversation.setChatterName(conversation.getUsers()[1]);
        } else {
            conversation.setChatterName(conversation.getUsers()[0]);
        }
        conversation.setChatterOnline(updateStatus);
        if (isFetchMessage) {
            return conversation;
        }
        String conversationId = conversation.getId();
        Map<String, Object> conversationMap = this.draftStoreService
            .getMap(CommonConstant.DRAFT_MAP_NAME);
        List<MessageBlock> messageBlocks = this.messageBlockRepository.findLastMessageBlocks(conversationId, false);
        MessageBlock persistedDraft;
        if (messageBlocks != null && !messageBlocks.isEmpty()) {
            persistedDraft = messageBlocks.stream()
                .filter(MessageBlock::isDraft).findFirst().orElse(null);
        } else {
            persistedDraft = MessageBlock.from(conversation.getUsers(), conversationId, null);
            this.messageBlockRepository.save(persistedDraft);
            messageBlocks = Collections.singletonList(persistedDraft);
        }
        MessageBlock draft = this
            .getLatestBlock(conversationId, conversationMap);
        if (draft != null && persistedDraft != null) {
            Visualizer.customMessageBlock(draft);
            messageBlocks.set(messageBlocks.indexOf(persistedDraft), draft);
        }
        conversation.setMessageBlockList(messageBlocks);
        return conversation;
    }

    @Override
    public Conversation getConversation(String fromUser, String toUser, boolean isFetchMessage,
        boolean updateStatus) {
        if (toUser == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            toUser = authentication.getName();
        }
        String[] users = new String[]{fromUser, toUser};
        Conversation conversation = this.getConversation(users);
        conversation.setChatterName(fromUser);
        conversation.setChatterOnline(updateStatus);

        if (isFetchMessage) {
            return conversation;
        }
        String conversationId = conversation.getId();
        Map<String, Object> conversationMap = this.draftStoreService
            .getMap(CommonConstant.DRAFT_MAP_NAME);
        List<MessageBlock> messageBlocks = this.messageBlockRepository.findLastMessageBlocks(conversationId, false);
        MessageBlock persistedDraft;
        if (messageBlocks != null && !messageBlocks.isEmpty()) {
            persistedDraft = messageBlocks.stream()
                .filter(MessageBlock::isDraft).findFirst().orElse(null);
        } else {
            persistedDraft = MessageBlock.from(conversation.getUsers(), conversationId, null);
            this.messageBlockRepository.save(persistedDraft);
            messageBlocks = Collections.singletonList(persistedDraft);
        }
        MessageBlock draft = this
            .getLatestBlock(conversationId, conversationMap);
        if (draft != null && persistedDraft != null) {
            Visualizer.customMessageBlock(draft);
            messageBlocks.set(messageBlocks.indexOf(persistedDraft), draft);
        }
        conversation.setMessageBlockList(messageBlocks);
        return conversation;
    }

    @Override
    public MessageBlock getDraft(String conversationId, String[] users) {
        Map<String, Object> conversationMap = this.draftStoreService
            .getMap(CommonConstant.DRAFT_MAP_NAME);
        MessageBlock draft = getLatestBlock(conversationId, conversationMap);
        this.conversationRepository.updateConversation(conversationId);
        return draft;
    }

    @Override
    public synchronized void updateDraft(Message message) {
        String[] users = new String[]{message.getFrom(), message.getTo()};
        String conversationId = message.getConversationId();
        Map<String, Object> conversationMap = this.draftStoreService
            .getMap(CommonConstant.DRAFT_MAP_NAME);
        MessageBlock draft = this.getAndUpdateLatestBlock(users, conversationId, conversationMap);
        log.debug("Block id: {}", draft.getId());
        draft.getMessages().add(message);
        this.draftStoreService.updateDraft(conversationMap, conversationId, draft);
        Visualizer.customMessageBlock(draft);
    }

    private synchronized MessageBlock getAndUpdateLatestBlock(String[] users,
        String conversationId, Map<String, Object> conversationMap) {
        MessageBlock draft = (MessageBlock) conversationMap.get(conversationId);
        if (draft == null) {
            MessageBlock persistedBlock = this.messageBlockRepository.findFirstByIsDraft(true).orElse(null);
            String id = persistedBlock != null ? persistedBlock.getId() : null;
            draft = MessageBlock.from(users, conversationId, id);
            this.messageBlockRepository.save(draft);
            this.conversationRepository.updateConversation(conversationId);
        } else if (draft.getMessages().size() >= 20) {
            // save old block
            draft.setDraft(false);
            this.messageBlockRepository.save(draft);
            // create new block
            draft = MessageBlock.from(users, conversationId, null);
            this.messageBlockRepository.save(draft);
            this.conversationRepository.updateConversation(conversationId);
        }
        return draft;
    }

    private synchronized MessageBlock getLatestBlock(
        String conversationId,
        Map<String, Object> conversationMap) {
        return (MessageBlock) conversationMap.get(conversationId);
    }

    private Conversation getConversation(String id) {
        Optional<Conversation> conversationOptional = this.conversationRepository.findById(id);
        return conversationOptional.orElse(null);
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
            newConversation.setLastUpdate(new Date());
            newConversation.setUsers(users);
            newConversation.setUnreadMessages(new int[2]);
            newConversation.setMessageBlockList(new ArrayList<>());
            this.conversationRepository.save(newConversation);
            return newConversation;
        } else {
            return conversationOptional.get();
        }
    }

    @Override
    public List<String> getOnlineUsers() {
        return this.simpUserRegistry.getUsers()
            .stream()
            .map(SimpUser::getName)
            .collect(Collectors.toList());
    }
}
