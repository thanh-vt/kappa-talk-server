package com.kappa.model.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "conversation")
public class Conversation {

    @Id
    private String id;

    private String[] users;

    private int[] unreadMessages;

    @Transient
    private int currentUserIndex;

    private Date startTime;

    private Date lastUpdate;

    @Transient
    private String chatterName;

    @Transient
    private boolean isChatterOnline;

    @Transient
    private List<MessageBlock> messageBlockList;

}
