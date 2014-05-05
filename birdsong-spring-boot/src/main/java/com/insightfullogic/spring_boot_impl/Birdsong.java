package com.insightfullogic.spring_boot_impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * .
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Birdsong {

    public static void main(String[] args) throws Exception {
        UserRepository repo = SpringApplication.run(Birdsong.class, args).getBean(UserRepository.class);
        repo.addInitialData();
    }

}
