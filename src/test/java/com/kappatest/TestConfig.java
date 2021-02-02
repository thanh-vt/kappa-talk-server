package com.kappatest;

import com.mongodb.WriteConcern;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@TestConfiguration
public class TestConfig implements InitializingBean, DisposableBean {

//    @MockBean
//    public DataSeedingListener dataSeedingListener;

    public MongodExecutable executable;

    @Override
    public void afterPropertiesSet() throws Exception {
        String host = "localhost";
        int port = 27019;

        IMongodConfig mongoDbConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
            .net(new Net(host, port, Network.localhostIsIPv6()))
            .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        executable = starter.prepare(mongoDbConfig);
        executable.start();
    }

    @MockBean
    public JwtAccessTokenConverter jwtAccessTokenConverter;


    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        // also possible to connect to a remote or real MongoDB instance
        return new SimpleMongoClientDatabaseFactory(
            "mongodb://localhost:27019/test_db");
    }


    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        MongoTemplate template = new MongoTemplate(mongoDbFactory);
        template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return template;
    }

    @Override
    public void destroy() {
        executable.stop();
    }
}
