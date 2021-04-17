package com.kappa.service;

import java.util.Map;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

public interface UserService {

    OAuth2AuthenticatedPrincipal getCurrentUser();

    Map<String, Object> getCurrentUserShortInfo();

}
