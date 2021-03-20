package com.kappa.service;

import com.kappa.model.entity.ChatInfo;
import com.kappa.model.entity.Message;

public interface ConversationService {

    ChatInfo getChatInfo(String fromUser, String toUser) throws InterruptedException;

    void updateDraft(Message message) throws InterruptedException;

}
