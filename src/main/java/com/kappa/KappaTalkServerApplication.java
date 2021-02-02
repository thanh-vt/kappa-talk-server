package com.kappa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.kappa")
@EnableMongoRepositories(basePackages = {"com.kappa.repository"})
public class KappaTalkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KappaTalkServerApplication.class, args);
    }

}
