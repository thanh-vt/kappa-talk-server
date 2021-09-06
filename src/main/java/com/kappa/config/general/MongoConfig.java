package com.kappa.config.general;

import com.kappa.util.MessageCommandConverter;
import com.kappa.util.MessageStatusConverter;
import com.kappa.util.MessageTypeConverter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory);
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setCustomConversions(mongoCustomConversions()); // tell mongodb to use the custom converters
        mongoMapping.afterPropertiesSet();
        return mongoTemplate;
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(
            Arrays.asList(
                new MessageTypeConverter.MessageTypeReader(),
                new MessageTypeConverter.MessageTypeWriter(),
                new MessageStatusConverter.MessageStatusReader(),
                new MessageStatusConverter.MessageStatusWriter(),
                new MessageCommandConverter.MessageCommandReader(),
                new MessageCommandConverter.MessageCommandWriter())
        );
    }
}
