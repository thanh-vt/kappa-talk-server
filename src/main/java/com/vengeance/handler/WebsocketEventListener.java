package com.vengeance.handler;

import java.security.Principal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebsocketEventListener {

    private static final Logger logger = LogManager.getLogger(WebsocketEventListener.class);

    private final SimpUserRegistry simpUserRegistry;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    public WebsocketEventListener(
        SimpUserRegistry simpUserRegistry,
        SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpUserRegistry = simpUserRegistry;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent event) {
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Principal user = headers.getUser();
        if (user != null) {
            logger.info("Connected: {} - session {}", user.getName(), headers.getSessionId());
            this.simpMessageSendingOperations.convertAndSend("/topic/online-user",
                user.getName());
        }
//        LoginEvent loginEvent = new LoginEvent(username);
//        messagingTemplate.convertAndSend(loginDestination, loginEvent);
//
//        // We store the session as we need to be idempotent in the disconnect event processing
//        participantRepository.add(headers.getSessionId(), loginEvent);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Principal user = headers.getUser();
        if (user != null) {
            logger.info("Disconnected: {} - session {}", user.getName(), headers.getSessionId());
            this.simpMessageSendingOperations.convertAndSend("/topic/offline-user",
                headers.getUser().getName());
        }
    }
}
