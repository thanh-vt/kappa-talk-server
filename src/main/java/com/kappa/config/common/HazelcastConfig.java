package com.kappa.config.common;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class HazelcastConfig {

    private static final Logger logger = LogManager.getLogger(HazelcastConfig.class);

    @Value("${custom.hazelcast-hosts}")
    private String[] hazelcastHosts;

    @Bean
    @Profile({"default", "poweredge"})
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        logger.info("Hazelcast host: ");
        for (String host: hazelcastHosts) {
            logger.info("- {}", host);
        }
        networkConfig.addAddress(hazelcastHosts)
            .setSmartRouting(true)
            .addOutboundPortDefinition("34700-34710")
            .setRedoOperation(true)
            .setConnectionTimeout(5000);
        return clientConfig;
    }

    @Bean(name = "hazelcastClientInstance")
    @Profile({"default", "poweredge"})
    public HazelcastInstance hazelcastClientInstance(ClientConfig clientConfig) {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

}
