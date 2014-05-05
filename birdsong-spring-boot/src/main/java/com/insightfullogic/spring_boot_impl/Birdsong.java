package com.insightfullogic.spring_boot_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping;

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
        repo.addInitialData();
    }

}
