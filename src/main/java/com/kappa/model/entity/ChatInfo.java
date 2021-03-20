package com.kappa.model.entity;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatInfo {

    private String conversationId;
    private Date startTime;
    private List<MessageBlock> messageBlockList;
    private String[] users;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<MessageBlock> getMessageBlockList() {
        return messageBlockList;
    }

    public void setMessageBlockList(List<MessageBlock> messageBlockList) {
        this.messageBlockList = messageBlockList;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }
}
