package com.kappa.service;

import com.kappa.model.entity.Conversation;
import com.kappa.model.entity.Message;
import com.kappa.model.entity.MessageBlock;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface ConversationService {

    Map<String, Conversation> getConversations(Pageable pageable);

    Conversation getConversation(String id, boolean isFetchMessage, boolean updateStatus);

    Conversation getConversation(String fromUser, String toUser, boolean isFetchMessage, boolean updateStatus);

    MessageBlock getDraft(String conversationId, String[] users);

    void updateDraft(Message message);

    List<String> getOnlineUsers();
}
