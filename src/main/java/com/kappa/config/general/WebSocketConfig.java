package com.kappa.config.general;

import com.kappa.config.security.CustomHandshakeHandler;
import com.kappa.config.security.CustomHandshakeInterceptor;
import com.kappa.config.security.CustomUserDestinationResolver;
import com.kappa.config.security.CustomUserInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Log4j2
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${custom.enable-external-broker:false}")
    private boolean enableRabbitMq;

    @Value("${spring.rabbitmq.host:unknown}")
    private String rabbitmqHost;

    @Value("${spring.rabbitmq.port:61613}")
    private int rabbitmqStompPort;

    @Value("${spring.rabbitmq.username:unknown}")
    private String rabbitmqUserName;

    @Value("${spring.rabbitmq.password:unknown}")
    private String rabbitmqPassword;

    private final CustomHandshakeHandler customHandshakeHandler;

    private final CustomHandshakeInterceptor customHandshakeInterceptor;

    private final CustomUserInterceptor customUserInterceptor;

    @Autowired
    public WebSocketConfig(CustomHandshakeHandler customHandshakeHandler,
        CustomHandshakeInterceptor customHandshakeInterceptor,
        CustomUserInterceptor customUserInterceptor) {
        this.customHandshakeHandler = customHandshakeHandler;
        this.customHandshakeInterceptor = customHandshakeInterceptor;
        this.customUserInterceptor = customUserInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic/", "/queue/");
        if (this.enableRabbitMq) {
            log.info("RabbitMQ info: {}, {}, {}, {}", rabbitmqHost, rabbitmqStompPort, rabbitmqUserName, rabbitmqPassword);
            config.enableStompBrokerRelay("/topic/", "/queue/", "/exchange/", "/amq/queue")
                .setRelayHost(rabbitmqHost)
                .setRelayPort(rabbitmqStompPort)
                .setSystemLogin(rabbitmqUserName)
                .setSystemPasscode(rabbitmqPassword)
                .setClientLogin(rabbitmqUserName)
                .setClientPasscode(rabbitmqPassword);
        } else {
            config.enableSimpleBroker("/topic/", "/queue/", "/exchange/", "/amq/queue");
        }
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setHandshakeHandler(this.customHandshakeHandler)
            .setAllowedOriginPatterns("*")
//            .setAllowedOrigins("*", "chrome-extension://ggnhohnkfcpcanfekomdkjffnfcjnjam")
            .addInterceptors(customHandshakeInterceptor)
            .withSockJS()
        ;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(customUserInterceptor);
    }

    @Bean
    public UserDestinationResolver userDestinationResolver() {
        return new CustomUserDestinationResolver();
    }
}
