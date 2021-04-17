package com.kappa.service.impl;

import com.kappa.constant.CommonConstant;
import com.kappa.constant.MessageCommand;
import com.kappa.constant.MessageStatus;
import com.kappa.model.entity.Message;
import com.kappa.model.entity.MessageBlock;
import com.kappa.repositories.MessageBlockRepository;
import com.kappa.service.DraftStoreService;
import com.kappa.service.MessageService;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageBlockRepository messageBlockRepository;

    private final DraftStoreService draftStoreService;

    @Autowired
    public MessageServiceImpl(MessageBlockRepository messageBlockRepository,
        DraftStoreService draftStoreService) {
        this.messageBlockRepository = messageBlockRepository;
        this.draftStoreService = draftStoreService;
    }

    @Override
    public void updateMessage(Message message) {
        message.setCommand(MessageCommand.UPDATE);
        Optional<MessageBlock> optionalMessageBlock = this.messageBlockRepository
            .findById(message.getBlockId());
        if (optionalMessageBlock.isPresent()) {
            if (optionalMessageBlock.get().isDraft()) {
                String conversationId = optionalMessageBlock.get().getConversationId();
                Map<String, Object> draftMap = this.draftStoreService
                    .getMap(CommonConstant.DRAFT_MAP_NAME);
                MessageBlock draft = (MessageBlock) draftMap.get(conversationId);
                draft.getMessages().set(message.getIndex(), message);
                this.draftStoreService.updateDraft(draftMap, conversationId, draft);
            } else {
                optionalMessageBlock.get().getMessages().set(message.getIndex(), message);
                this.messageBlockRepository.save(optionalMessageBlock.get());
            }
        } else {
            log.error("Nothing to update!");
        }
    }
}
