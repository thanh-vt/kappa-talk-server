package com.kappa.controller;

import com.kappa.constant.ChatDestinationName;
import com.kappa.model.entity.Message;
import com.kappa.service.ConversationService;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

@Log4j2
@Controller
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*",
    exposedHeaders = {HttpHeaders.SET_COOKIE})
public class ChatController {

    private final SimpMessageSendingOperations template;

    private final ConversationService conversationService;

    @Autowired
    public ChatController(SimpMessagingTemplate template, ConversationService conversationService) {
        this.template = template;
        this.conversationService = conversationService;
    }

    @MessageMapping("/hello")
    @SendTo(ChatDestinationName.GREETINGS)
    public Message greeting(@Payload Message message, StompHeaderAccessor stompHeaderAccessor)
        throws Exception {
        log.debug(stompHeaderAccessor.getHost());
        Thread.sleep(1000); // simulated delay
        return new Message("Hello, " + HtmlUtils.htmlEscape(message.getTo()) + "!");
    }

    @MessageMapping("/to-self")
    @SendToUser(destinations = ChatDestinationName.PRIVATE_CHAT, broadcast = true)
    public Message messageToSelf(@Payload Message message) throws InterruptedException {
        message.setTo(message.getFrom());
        this.conversationService.updateDraft(message);
        return message;
    }

    @MessageMapping("/to-someone")
    public void messageToUser(@Payload Message message, @Header("simpSessionId") String sessionId)
        throws InterruptedException {
        log.debug("Message from session: {}", sessionId);
        this.conversationService.updateDraft(message);
        this.template.convertAndSendToUser(message.getFrom(), ChatDestinationName.PRIVATE_CHAT, message);
        this.template.convertAndSendToUser(message.getTo(), ChatDestinationName.PRIVATE_CHAT, message);
    }

    @MessageExceptionHandler
    @SendToUser(destinations = ChatDestinationName.ERROR, broadcast = true)
    public Message handleException(Exception ex) {
        log.error(ex);
        Message message = new Message();
        message.setFrom("system");
        message.setContent("Got error: " + ex.getMessage());
        message.setTimestamp(new Date());
        return message;
    }
}
