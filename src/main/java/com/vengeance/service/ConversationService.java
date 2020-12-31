package com.vengeance.service;

import com.vengeance.model.ChatInfo;
import com.vengeance.model.Message;

public interface ConversationService {

    ChatInfo getChatInfo(String fromUser, String toUser) throws InterruptedException;

    void updateDraft(Message message) throws InterruptedException;

}
