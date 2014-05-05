package com.insightfullogic.spring_boot_impl;

import com.insightfullogic.birdsong.BirdsongApplicationRunner;
import com.insightfullogic.birdsong.Users;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * .
 */
public class SpringBootApplicationRunner implements BirdsongApplicationRunner {

    private ConfigurableApplicationContext context;

    @Override
    public void start() throws Exception {
        context = SpringApplication.run(Birdsong.class);
        addUsers();
    }

    private void addUsers() {
        UserRepository repo = context.getBean(UserRepository.class);
        repo.addUsers(
                new User(Users.richard, Users.richardsPass),
                new User(Users.bob, Users.bobsPass)
        );
    }

    @Override
    public void stop() throws Exception {
        SpringApplication.exit(context);
    }

    @Override
    public void reset() {

    }

    @Override
    public long getStartupPauseInMilliseconds() {
        return 0;
    }

}
