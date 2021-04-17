package com.kappa.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum MessageCommand {

    PUSH(1),
    UPDATE(2);

    private static final Map<Integer, MessageCommand> MESSAGE_COMMAND_MAP = new HashMap<>();

    static {
        for (MessageCommand messageCommand : MessageCommand.values()) {
            MESSAGE_COMMAND_MAP.put(messageCommand.value, messageCommand);
        }
    }

    @JsonValue
    private final int value;

    MessageCommand(int value) {
        this.value = value;
    }

    @JsonCreator
    public static MessageCommand fromValue(Integer integer) {
        if (integer == null) return MessageCommand.PUSH;
        MessageCommand messageCommand = MESSAGE_COMMAND_MAP.get(integer);
        if (messageCommand == null) return MessageCommand.PUSH;
        return messageCommand;
    }

    public int getValue() {
        return value;
    }
}
