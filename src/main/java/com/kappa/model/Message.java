package com.kappa.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

  @Transient
  private static final long serialVersionUID = 1L;

  public Message(String content) {
    this.content = content;
  }

  public Message(String content, Date timestamp) {
    this.content = content;
    this.timestamp = timestamp;
  }

  @Transient
  private Long conversationId;

  private String content;

  private Date timestamp;

  private String from;

  private String to;
}
