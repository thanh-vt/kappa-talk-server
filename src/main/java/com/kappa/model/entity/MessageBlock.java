package com.kappa.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "block")
public class MessageBlock implements Serializable {

    @Transient
    public static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String conversationId;

    private String[] users;

    private Date startTime;

    private List<Message> messages;

    @Field(name = "isDraft")
    private boolean isDraft;

    public static MessageBlock from(String[] users, String conversationId, String id) {
        MessageBlock messageBlock = new MessageBlock();
        messageBlock.setConversationId(conversationId);
        messageBlock.setMessages(Collections.synchronizedList(new ArrayList<>()));
        messageBlock.setUsers(users);
        messageBlock.setStartTime(new Date());
        messageBlock.setDraft(true);
        messageBlock.setId(id);
        return messageBlock;
    }
}
