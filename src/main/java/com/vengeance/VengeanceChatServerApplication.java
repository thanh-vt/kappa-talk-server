package com.vengeance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.vengeance")
@EnableMongoRepositories(basePackages = {"com.vengeance.repository"})
public class VengeanceChatServerApplication {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    webServerFactoryCustomizer() {
        return factory -> {
//            Ssl ssl = new Ssl();
//            ssl.setEnabled(true);
//            ssl.setProtocol("TLS");
//            ssl.setEnabledProtocols(new String[]{"TLSv1.2"});
//            ssl.setKeyAlias("pysga1996");
//            ssl.setKeyPassword("259138");
//            ssl.setKeyStorePassword("1381996");
//            ssl.setKeyStore("classpath:pysga1996.jks");
//            ssl.setKeyStoreType("JKS");
//            factory.setSsl(ssl);
//            factory.setPort(8901);
//            factory.setContextPath("/vengeance");
        };
    }

//    private Connector getHttpConnector() {
//        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//        connector.setScheme("http");
//        connector.setPort(httpPort);
//        connector.setSecure(false);
//        connector.setRedirectPort(httpsPort);
//        connector.addUpgradeProtocol(new Http2Protocol());
//        return connector;
//    }

    public static void main(String[] args) {
        SpringApplication.run(VengeanceChatServerApplication.class, args);
    }

}
