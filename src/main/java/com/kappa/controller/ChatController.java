package com.kappa.controller;

import com.kappa.constant.ChatDestinationName;
import com.kappa.model.entity.Attachment;
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

/**
 * Controller handling chat message via websocket
 *
 * @created 25/04/2021 - 01:56:12 SA
 * @project vengeance
 * @author thanhvt
 * @since 1.0
**/
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

    /**
     * @created 25/04/2021 - 01:00:00 SA
     * @author thanhvt
     * @description handle message in "hello" queue
     * @param message inbound message
     * @param stompHeaderAccessor header accessor
     * @return outbound message
     * @throws Exception when sleeping thread is interrupted
     */
    @MessageMapping("/hello")
    @SendTo(ChatDestinationName.GREETINGS)
    public Message greeting(@Payload Message message, StompHeaderAccessor stompHeaderAccessor)
        throws Exception {
        log.debug(stompHeaderAccessor.getHost());
        Thread.sleep(1000); // simulated delay
        return new Message(
            String.format("Welcome to chat room, %s!", HtmlUtils.htmlEscape(message.getTo())));
    }

    /**
     * @created 25/04/2021 - 01:03:39 SA
     * @author thanhvt
     * @description self chat send message
     * @param message inbound message
     * @return outbound message
     */
    @MessageMapping("/message/to-self")
    @SendToUser(destinations = ChatDestinationName.PRIVATE_CHAT, broadcast = true)
    public Message messageToSelf(@Payload Message message) {
        message.setTo(message.getFrom());
        this.conversationService.updateDraft(message);
        return message;
    }

    /**
     * @created 25/04/2021 - 01:03:39 SA
     * @author thanhvt
     * @description self chat send file
     * @param message inbound message
     * @return outbound message
     */
    @MessageMapping("/file/to-self")
    @SendToUser(destinations = ChatDestinationName.PRIVATE_CHAT, broadcast = true)
    public Attachment fileToSelf(@Payload Attachment message) {
        message.setTo(message.getFrom());
        this.conversationService.updateDraft(message);
        return message;
    }

    /**
     * @created 25/04/2021 - 01:03:39 SA
     * @author thanhvt
     * @description friend chat send message
     * @param message inbound message
     */
    @MessageMapping("/message/to-someone")
    public void messageToUser(@Payload Message message, @Header("simpSessionId") String sessionId) {
        log.debug("Message from session: {}", sessionId);
        this.conversationService.updateDraft(message);
        this.template.convertAndSendToUser(message.getFrom(), ChatDestinationName.PRIVATE_CHAT, message);
        this.template.convertAndSendToUser(message.getTo(), ChatDestinationName.PRIVATE_CHAT, message);
    }

    /**
     * @created 25/04/2021 - 01:03:39 SA
     * @author thanhvt
     * @description friend chat send file
     * @param message inbound message
     */
    @MessageMapping("/file/to-someone")
    public void fileToUser(@Payload Attachment message, @Header("simpSessionId") String sessionId) {
        log.debug("Message from session: {}", sessionId);
        this.conversationService.updateDraft(message);
        this.template.convertAndSendToUser(message.getFrom(), ChatDestinationName.PRIVATE_CHAT, message);
        this.template.convertAndSendToUser(message.getTo(), ChatDestinationName.PRIVATE_CHAT, message);
    }

    /**
     * @created 25/04/2021 - 01:02:08 SA
     * @author thanhvt
     * @description handle socket error
     * @param ex socket general exception
     * @return notification message when error occur
     */
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
