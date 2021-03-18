package com.kappa.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kappa.model.UserDTO;
import com.kappa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final ObjectMapper objectMapper;

    @Autowired
    public UserServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal) {
            OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            return this.objectMapper.convertValue(principal.getAttributes(), UserDTO.class);
        }
        return null;
    }

}
