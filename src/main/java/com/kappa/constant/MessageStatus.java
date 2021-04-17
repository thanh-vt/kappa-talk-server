package com.kappa.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum MessageStatus {

    ACTIVE(1),
    INACTIVE(0);

    private static final Map<Integer, MessageStatus> MESSAGE_STATUS_MAP = new HashMap<>();

    static {
        for (MessageStatus messageStatus : MessageStatus.values()) {
            MESSAGE_STATUS_MAP.put(messageStatus.value, messageStatus);
        }
    }

    @JsonValue
    private final int value;

    MessageStatus(int value) {
        this.value = value;
    }

    @JsonCreator
    public static MessageStatus fromValue(Integer integer) {
        if (integer == null) return MessageStatus.ACTIVE;
        MessageStatus messageStatus = MESSAGE_STATUS_MAP.get(integer);
        if (messageStatus == null) return MessageStatus.ACTIVE;
        return messageStatus;
    }

    public int getValue() {
        return value;
    }
}
