package com.kappa.interceptor;

import com.kappa.model.StompPrincipal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public class UserInterceptor implements ChannelInterceptor {

    private static final Logger logger = LogManager.getLogger(UserInterceptor.class);

    private final ResourceServerTokenServices resourceServerTokenServices;

    @Autowired
    public UserInterceptor(
        ResourceServerTokenServices resourceServerTokenServices) {
        this.resourceServerTokenServices = resourceServerTokenServices;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;
        if (StompCommand.MESSAGE.equals(accessor.getCommand())) {
            if (accessor.getUser() != null && accessor.getUser().getName().equals("anonymous")) {
                channel.send(new Message<com.kappa.model.Message>() {

                    @Override
                    public com.kappa.model.Message getPayload() {
                        return new com.kappa.model.Message("Invalid token");
                    }

                    @Override
                    public MessageHeaders getHeaders() {
                        return new MessageHeaders(new HashMap<>());
                    }
                });
            }
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message
                .getHeaders()
                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
//                Object userObject = ((Map<String, Object>) raw).get("login");
//                if (userObject instanceof List) {
//                    String username = ((List<Object>) userObject).get(0).toString();
//                    accessor.setUser(new StompPrincipal(username));
//                }
                Object tokenObj = ((Map<String, Object>) raw).get("token");
                if (tokenObj instanceof List) {
                    String token = ((List<Object>) tokenObj).get(0).toString();
                    OAuth2Authentication authentication;
                    try {
                        authentication = this.resourceServerTokenServices.loadAuthentication(token);
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = (UsernamePasswordAuthenticationToken) authentication.getUserAuthentication();
                        accessor.setUser(new StompPrincipal(
                            (String) usernamePasswordAuthenticationToken.getPrincipal(),
                            usernamePasswordAuthenticationToken.getAuthorities()));
                    } catch (InvalidTokenException ex) {
                        logger.error("Invalid token: ", ex);
                        accessor.setUser(new StompPrincipal("anonymous", new HashSet<>()));
                    }

                }

            }
        }
        return message;
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return false;
    }
}
