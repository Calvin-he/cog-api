package com.cog.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class CogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CogApiApplication.class, args);
    }
}