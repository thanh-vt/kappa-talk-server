package com.kappa.config.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.client.RestOperations;

@Log4j2
@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true, order = Ordered.HIGHEST_PRECEDENCE)
@SuppressWarnings("deprecation")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    private final Environment env;

    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final RestOperations restOperations;

    @Autowired
    public SecurityConfig(
        OpaqueTokenIntrospector opaqueTokenIntrospector,
        Environment env,
        AccessDeniedHandler accessDeniedHandler,
        AuthenticationEntryPoint authenticationEntryPoint,
        RestOperations restOperations) {
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
        this.env = env;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.restOperations = restOperations;
    }

    @Bean
    @Primary
    public JwtDecoder customJwtDecoder() {
        NimbusJwtDecoderJwkSupport nimbusJwtDecoderJwkSupport = new NimbusJwtDecoderJwkSupport(
            this.jwkSetUri);
        nimbusJwtDecoderJwkSupport.setRestOperations(this.restOperations);
        return nimbusJwtDecoderJwkSupport;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
                DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
                bearerTokenResolver.setAllowUriQueryParameter(true);
                if (CloudPlatform.HEROKU.isActive(this.env)) {
                    httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer ->
                        jwtConfigurer.jwkSetUri(this.jwkSetUri));
                } else {
                    httpSecurityOAuth2ResourceServerConfigurer.opaqueToken(opaqueTokenConfigurer ->
                        opaqueTokenConfigurer.introspector(opaqueTokenIntrospector));
                }
                httpSecurityOAuth2ResourceServerConfigurer.bearerTokenResolver(bearerTokenResolver);
            })
            .headers()
            .frameOptions().sameOrigin()
            .httpStrictTransportSecurity().disable()
            .and()
            .authorizeRequests().anyRequest().permitAll().and();
    }
}
