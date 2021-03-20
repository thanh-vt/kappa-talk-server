package com.kappa.config.custom;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.messaging.simp.user.UserDestinationResult;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Log4j2
//@Component
public class CustomUserDestinationResolver implements UserDestinationResolver {

    private static final Pattern PATTERN_FOR_SENDING =
        Pattern.compile("/user/(?<username>.+?)/(?<exchangeName>.+?)");

    private static final Pattern PATTERN_FOR_SUBSCRIPTION =
        Pattern.compile("/user/(?<exchangeName>.*)");

    @Override
    public UserDestinationResult resolveDestination(Message<?> message) {
        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor
            .getAccessor(message, SimpMessageHeaderAccessor.class);

        assert accessor != null;
        final String destination = accessor.getDestination();
        final String authUser = accessor.getUser() != null ? accessor.getUser().getName() : null;
        log.trace("Resolving user destination {} for authUser={}, messageType={}",
            destination, authUser, accessor.getMessageType());

        if (destination != null) {
            if (SimpMessageType.SUBSCRIBE.equals(accessor.getMessageType()) ||
                SimpMessageType.UNSUBSCRIBE.equals(accessor.getMessageType())) {
                if (authUser != null) {
                    final Matcher authMatcher = PATTERN_FOR_SUBSCRIPTION.matcher(destination);
                    if (authMatcher.matches()) {
//                        String result = String.format("/exchange/%s/%s",
//                            authMatcher.group("exchangeName"), authUser);
                        String result = String.format("/exchange/%s/%s",
                            authMatcher.group("exchangeName"), authUser);
                        UserDestinationResult userDestinationResult =
                            new UserDestinationResult(destination, Collections.singleton(result), result, authUser);
                        log.debug("Resolved {} for {} into {}", destination, authUser, userDestinationResult);
                        return userDestinationResult;
                    }
                }
            }
            else if (SimpMessageType.MESSAGE.equals(accessor.getMessageType())) {
                final Matcher prefixMatcher = PATTERN_FOR_SENDING.matcher(destination);
                if (prefixMatcher.matches()) {
                    String username = prefixMatcher.group("username");
//                    String result = String.format("/exchange/%s/%s",
//                        prefixMatcher.group("exchangeName"), username);
                    String result = String.format("/exchange/%s/%s",
                        prefixMatcher.group("exchangeName"), username);
                    UserDestinationResult userDestinationResult =
                        new UserDestinationResult(destination, Collections.singleton(result), result, username);
                    log.debug("Resolved {} into {}", destination, userDestinationResult);
                    return userDestinationResult;
                }
            }

        }
        log.trace("Destination {} is not user-based", destination);
        return null;
    }
}
