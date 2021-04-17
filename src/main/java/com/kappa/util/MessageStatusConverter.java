package com.kappa.util;

import com.kappa.constant.MessageStatus;
import com.kappa.constant.MessageType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

public class MessageStatusConverter {

    @ReadingConverter
    public static class MessageStatusReader implements Converter<Integer, MessageStatus> {

        @Override
        public MessageStatus convert(@NonNull Integer integer) {
            return MessageStatus.fromValue(integer);
        }
    }

    @WritingConverter
    public static class MessageStatusWriter implements Converter<MessageStatus, Integer> {

        @Override
        public Integer convert(@NonNull MessageStatus messageType) {
            return messageType.getValue();
        }
    }
}
