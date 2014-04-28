package com.insightfullogic.birdsong.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * .
 */
public class UserApi {

    private final String prefix;
    private final String authPrefix;
    private final String loginUrl;
    private final String registerUrl;
    private final String followUrl;

    private final Executor executor;

    public UserApi(final String prefix) {
        this.prefix = prefix;
        authPrefix = prefix + "user/";
        loginUrl = authPrefix + "login";
        registerUrl = authPrefix + "register";
        followUrl = authPrefix + "follow";

        executor = Executor.newInstance();
    }

    public Executor getExecutor() {
        return executor;
    }

    public HttpResponse login(final String username, final String password) throws IOException {
        return postCredentials(username, password, loginUrl);
    }

    public HttpResponse register(final String username, final String password) throws IOException {
        return postCredentials(username, password, registerUrl);
    }

    public HttpResponse follow(final String who) throws IOException {
        final Request request = Request.Post(followUrl)
                                       .bodyForm(Form.form()
                                               .add("username", who)
                                               .build());

        return executor.execute(request)
                       .returnResponse();
    }

    private HttpResponse postCredentials(final String username, final String password, final String url)
            throws IOException {

        final Request request = Request.Post(url)
                .bodyForm(Form.form()
                        .add("username", username)
                        .add("password", password)
                        .build());

        return executor.execute(request)
                       .returnResponse();
    }

}
