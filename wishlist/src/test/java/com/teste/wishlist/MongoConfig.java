package com.teste.wishlist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.WriteConcern;

@Configuration
public class MongoConfig {

    @Bean
    public MongoDatabaseFactory factory() {
        return new SimpleMongoClientDatabaseFactory("mongodb://localhost:27018/test");
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        MongoTemplate template = new MongoTemplate(mongoDbFactory);
        template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return template;
    }

}
