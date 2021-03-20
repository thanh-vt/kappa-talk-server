package com.kappa.config.custom;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
        Map<String, Object> attributes) {
        Principal principal = request.getPrincipal();
        if (principal == null) {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(
                "ANONYMOUS"));
            principal = new AnonymousAuthenticationToken(
                "anonymous", "anonymous",
                authorities);
        }
        return principal;
    }
}
