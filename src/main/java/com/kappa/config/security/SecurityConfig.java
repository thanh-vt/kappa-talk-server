package com.kappa.config.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

@Log4j2
@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true, order = Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Autowired
    public SecurityConfig(
        OpaqueTokenIntrospector opaqueTokenIntrospector) {
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
                DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
                bearerTokenResolver.setAllowUriQueryParameter(true);
                httpSecurityOAuth2ResourceServerConfigurer.opaqueToken(opaqueTokenConfigurer ->
                    opaqueTokenConfigurer.introspector(opaqueTokenIntrospector)).bearerTokenResolver(bearerTokenResolver);
            })
            .headers()
            .frameOptions().sameOrigin().disable()
            .authorizeRequests().anyRequest().permitAll().and();
    }
}
