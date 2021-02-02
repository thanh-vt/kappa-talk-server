package com.kappa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScheduledUpdatesOnTopic {

    private SimpMessagingTemplate template;

    private MessagesSupplier messagesSupplier;

    public ScheduledUpdatesOnTopic() {
    }

    @Autowired
    public ScheduledUpdatesOnTopic(SimpMessagingTemplate template,
        MessagesSupplier messagesSupplier) {
        this.template = template;
        this.messagesSupplier = messagesSupplier;
    }

//    @Scheduled(fixedDelay = 3000)
//    public void publishUpdates() {
////        template.convertAndSend("/topic/greetings", messagesSupplier.get());
//        for (Entry<String, Principal> entry : userMap.entrySet()) {
//            Principal user = entry.getValue();
//            template
//                .convertAndSendToUser(user.getName(), "/queue/greetings",
//                    messagesSupplier.get(entry.getKey()));
//        }
//    }
}