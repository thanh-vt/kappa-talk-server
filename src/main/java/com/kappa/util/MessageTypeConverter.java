package com.kappa.util;

import com.kappa.constant.MessageType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

public class MessageTypeConverter {

    @ReadingConverter
    public static class MessageTypeReader implements Converter<Integer, MessageType> {

        @Override
        public MessageType convert(@NonNull Integer integer) {
            return MessageType.fromValue(integer);
        }
    }

    @WritingConverter
    public static class MessageTypeWriter implements Converter<MessageType, Integer> {

        @Override
        public Integer convert(@NonNull MessageType messageType) {
            return messageType.getValue();
        }
    }

}
