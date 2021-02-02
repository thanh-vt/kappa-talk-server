package com.kappa.service;

import com.kappa.model.ChatInfo;
import com.kappa.model.Message;

public interface ConversationService {

    ChatInfo getChatInfo(String fromUser, String toUser) throws InterruptedException;

    void updateDraft(Message message) throws InterruptedException;

}
