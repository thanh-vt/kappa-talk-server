package com.kappa.util;

import com.kappa.model.entity.Conversation;
import com.kappa.model.entity.MessageBlock;

public final class Visualizer {

    public static void customConversation(Conversation conversation) {
        if (conversation.getMessageBlockList() != null) {
            conversation.getMessageBlockList().forEach(messageBlock -> {
                if (messageBlock.getMessages() != null) {
                    messageBlock.getMessages().forEach(message -> {
                        message.setIndex(messageBlock.getMessages().indexOf(message));
                        message.setBlockId(messageBlock.getId());
                    });
                }
            });
        }
    }

    public static void customMessageBlock(MessageBlock messageBlock) {
        if (messageBlock.getMessages() != null) {
            messageBlock.getMessages().forEach(message -> {
                message.setIndex(messageBlock.getMessages().indexOf(message));
                message.setBlockId(messageBlock.getId());
            });
        }
    }

}
