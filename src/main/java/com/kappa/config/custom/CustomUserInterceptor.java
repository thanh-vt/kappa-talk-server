package com.kappa.config.custom;

import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@SuppressWarnings("deprecation")
public class CustomUserInterceptor implements ChannelInterceptor {

    @Override
    @SuppressWarnings("unchecked")
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message
                .getHeaders()
                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
//                Object userObject = ((Map<String, Object>) raw).get("login");
//                if (userObject instanceof List) {
//                    String username = ((List<Object>) userObject).get(0).toString();
//                    accessor.setUser(new StompPrincipal(username, new HashSet<>()));
//                }
//                Object tokenObj = ((Map<String, Object>) raw).get("token");
//                if (tokenObj instanceof List) {
//                    String token = ((List<Object>) tokenObj).get(0).toString();
//                    OAuth2Authentication authentication;
//                    try {
//                        authentication = this.resourceServerTokenServices.loadAuthentication(token);
//                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
//                            = (UsernamePasswordAuthenticationToken) authentication.getUserAuthentication();
//                        accessor.setUser(new StompPrincipal(
//                            ((UserDTO) usernamePasswordAuthenticationToken.getPrincipal()).getUsername(),
//                            usernamePasswordAuthenticationToken.getAuthorities()));
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    } catch (InvalidTokenException ex) {
//                        logger.error("Invalid token: ", ex);
//                        Principal principal = new StompPrincipal("anonymous", new HashSet<>());
//                        accessor.setUser(principal);
//                        AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken("anonymous", principal, new HashSet<>());
//                        SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);
//                    }
//
//                }
            }
        }
        return message;
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return false;
    }
}
