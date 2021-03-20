package com.kappa.config.security;

import static org.springframework.messaging.simp.SimpMessageType.CONNECT;
import static org.springframework.messaging.simp.SimpMessageType.DISCONNECT;
import static org.springframework.messaging.simp.SimpMessageType.HEARTBEAT;
import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;
import static org.springframework.messaging.simp.SimpMessageType.UNSUBSCRIBE;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .simpTypeMatchers(CONNECT, HEARTBEAT, UNSUBSCRIBE, DISCONNECT).permitAll()
//            .nullDestMatcher().authenticated()
            .simpDestMatchers("/topic/**", "/user/**", "/exchange/**").authenticated()
            .simpTypeMatchers(MESSAGE, SUBSCRIBE).authenticated()
            // catch all
            .anyMessage().denyAll();
    }



    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}
