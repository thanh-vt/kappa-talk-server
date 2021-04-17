package com.kappa.config.scheduler;

import com.kappa.constant.MessageType;
import com.kappa.model.entity.Message;
import java.util.Date;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class MessagesSupplier {

    public Message get(String username) {
        return new Message(null, "Hello " + username + ", " + this.generateRandomString(),
            new Date(), username, "system", MessageType.TEXT);
    }

    private String generateRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
}
