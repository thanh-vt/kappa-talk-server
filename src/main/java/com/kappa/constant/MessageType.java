package com.kappa.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
  * @created 25/04/2021 - 12:51:04 SA
  * @project vengeance
  * @author thanhvt
  * @description
  * @since 1.0
**/
public enum MessageType {

    TEXT(1),
    EMOJI(2),
    FILE(3),
    TYPING(4);

    private static final Map<Integer, MessageType> MESSAGE_TYPE_MAP = new HashMap<>();

    static {
        for (MessageType messageType : MessageType.values()) {
            MESSAGE_TYPE_MAP.put(messageType.value, messageType);
        }
    }

    @Getter
    @JsonValue
    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    @JsonCreator
    public static MessageType fromValue(Integer integer) {
        if (integer == null) return MessageType.TEXT;
        MessageType messageType = MESSAGE_TYPE_MAP.get(integer);
        if (messageType == null) return MessageType.TEXT;
        return messageType;
    }
}
