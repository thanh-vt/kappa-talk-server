package com.kappa.config;

import com.kappa.interceptor.CustomHandshakeInterceptor;
import com.kappa.interceptor.UserInterceptor;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final CustomHandshakeInterceptor customHandshakeInterceptor;

    private final UserInterceptor userInterceptor;

    @Autowired
    public WebSocketConfig(CustomHandshakeInterceptor customHandshakeInterceptor,
        UserInterceptor userInterceptor) {
        this.customHandshakeInterceptor = customHandshakeInterceptor;
        this.userInterceptor = userInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
//            .setHandshakeHandler(new DefaultHandshakeHandler() {
//                @Override
//                protected Principal determineUser(
//                    ServerHttpRequest request,
//                    WebSocketHandler wsHandler,
//                    Map<String, Object> attributes) {
//                    Principal principal = request.getPrincipal();
//                    if (principal == null) {
//                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//                        authorities.add(new SimpleGrantedAuthority(
//                            "ANONYMOUS"));
//                        principal = new AnonymousAuthenticationToken(
//                            "anonymous", "anonymous",
//                            authorities);
//                    }
//                    return principal;
//                }
//            })
            .setAllowedOriginPatterns("*")
//            .setAllowedOrigins("*", "chrome-extension://ggnhohnkfcpcanfekomdkjffnfcjnjam")
            .addInterceptors(customHandshakeInterceptor)
            .withSockJS()
        ;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(userInterceptor);
    }
}
