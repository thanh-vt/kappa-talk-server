package com.kappa.util;

import com.kappa.constant.MessageCommand;
import com.kappa.constant.MessageStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

public class MessageCommandConverter {

    @ReadingConverter
    public static class MessageCommandReader implements Converter<Integer, MessageCommand> {

        @Override
        public MessageCommand convert(@NonNull Integer integer) {
            return MessageCommand.fromValue(integer);
        }
    }

    @WritingConverter
    public static class MessageCommandWriter implements Converter<MessageCommand, Integer> {

        @Override
        public Integer convert(@NonNull MessageCommand messageType) {
            return messageType.getValue();
        }
    }
}
