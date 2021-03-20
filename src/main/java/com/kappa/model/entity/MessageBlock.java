package com.kappa.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Transient;

public abstract class MessageBlock implements Serializable {

    @Transient
    public static final long serialVersionUID = 1L;

    protected String conversationId;
    protected String[] users;
    protected Date startTime;
    protected List<Message> messages;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
