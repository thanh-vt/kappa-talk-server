package com.kappa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "draft_block")
public class DraftMessageBlock extends MessageBlock {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HistoryMessageBlock toHistory() {
        HistoryMessageBlock historyMessageBlock = new HistoryMessageBlock();
        historyMessageBlock.setId(this.id);
        historyMessageBlock.setConversationId(this.conversationId);
        historyMessageBlock.setUsers(this.users);
        historyMessageBlock.setStartTime(this.startTime);
        historyMessageBlock.setMessages(this.messages);
        return historyMessageBlock;
    }
}
