package com.insightfullogic.birdsong.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * .
 */
public class UserApi {

    public static final String user = "richard";
    public static final String pass = "Gau1suph";

    private final String prefix;
    private final String authPrefix;
    private final String loginUrl;
    private final String registerUrl;

    public UserApi(final String prefix) {
        this.prefix = prefix;
        authPrefix = prefix + "user/";
        loginUrl = authPrefix + "login";
        registerUrl = authPrefix + "register";
    }

    public HttpResponse login(final String username, final String password) throws IOException {
        return Request.Post(loginUrl)
                .addHeader("username", username)
                .addHeader("password", password)
                .execute().returnResponse();
    }

    public void defaultLogin() throws IOException {
        login(user, pass);
    }

    public HttpResponse register(final String username, final String password) throws IOException {
        return Request.Post(registerUrl)
                .addHeader("username", username)
                .addHeader("password", password)
                .execute().returnResponse();
    }

}
