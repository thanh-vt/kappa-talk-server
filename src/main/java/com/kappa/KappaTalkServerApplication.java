package com.kappa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.kappa")
@EnableMongoRepositories(basePackages = {"com.kappa.repositories"})
public class KappaTalkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KappaTalkServerApplication.class, args);
    }

}
