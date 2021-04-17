package com.kappa.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.kappa.constant.MessageCommand;
import com.kappa.constant.MessageStatus;
import com.kappa.constant.MessageType;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

  @Transient
  private static final long serialVersionUID = 1L;

  @Transient
  private String conversationId;

  private String content;

  private Date timestamp;

  private String from;

  private String to;

  @Field
  private MessageType type;

  private MessageStatus status;

  @Transient
  private int index;

  @Transient
  private String blockId;

  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  private MessageCommand command;

  public Message(String content) {
    this.content = content;
  }

  public Message(String content, Date timestamp) {
    this.content = content;
    this.timestamp = timestamp;
  }

  public Message(String conversationId, String content,
      Date timestamp, String from, String to, MessageType type) {
    this.conversationId = conversationId;
    this.content = content;
    this.timestamp = timestamp;
    this.from = from;
    this.to = to;
    this.type = type;
  }
}
