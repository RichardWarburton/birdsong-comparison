package com.insightfullogic.birdsong.api;

import org.junit.rules.ExternalResource;

import java.io.IOException;

public class Api extends ExternalResource {

    private final String username;
    private final String password;

    public final UserApi users;
    public final SingingApi singing;

    public Api(String username, String password, String prefix) {
        this.username = username;
        this.password = password;
        users = new UserApi(prefix);
        singing = new SingingApi(prefix, users);
    }

    @Override
    protected void before() throws IOException {
        users.login(username, password);
    }

}
