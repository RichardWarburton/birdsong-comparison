package com.insightfullogic.spring_boot_impl;

import com.insightfullogic.birdsong.Users;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * .
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource("classpath:/spring-interceptors.xml")
public class Birdsong {

    public static void main(String[] args) throws Exception {
        UserRepository repo = SpringApplication.run(Birdsong.class, args).getBean(UserRepository.class);
        repo.addUsers(
                new User(Users.richard, Users.richardsPass),
                new User(Users.bob, Users.bobsPass)
        );
    }

}
