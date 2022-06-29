package com.crow.oauthex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OAuthExApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuthExApplication.class, args);
    }

}
