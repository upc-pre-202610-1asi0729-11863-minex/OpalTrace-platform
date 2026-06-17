package com.opaltrace.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OpalTracePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpalTracePlatformApplication.class, args);
    }

}
