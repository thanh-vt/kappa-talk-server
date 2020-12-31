package com.vengeance.controller;

import com.vengeance.model.Message;
import com.vengeance.model.HelloMessage;
import com.vengeance.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {

    private final SimpMessageSendingOperations template;

    private final ConversationService conversationService;

    @Autowired
    public ChatController(SimpMessagingTemplate template, ConversationService conversationService) {
        this.template = template;
        this.conversationService = conversationService;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Message greeting(@Payload HelloMessage message, StompHeaderAccessor stompHeaderAccessor)
        throws Exception {
//        String host = stompHeaderAccessor.getHost();
        Thread.sleep(1000); // simulated delay
        return new Message("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/to-self")
    @SendToUser("/queue/greetings")
    public Message messageToSelf(@Payload Message message) {
        message.setTo(message.getFrom());
        return message;
    }

    @MessageMapping("/to-someone")
    public void messageToUser(@Payload Message message, @Header("simpSessionId") String sessionId)
        throws InterruptedException {
        this.conversationService.updateDraft(message);
        this.template.convertAndSendToUser(message.getFrom(), "/queue/greetings", message);
        this.template.convertAndSendToUser(message.getTo(), "/queue/greetings", message);
    }
}
