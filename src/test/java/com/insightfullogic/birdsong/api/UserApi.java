package com.insightfullogic.birdsong.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * .
 */
public class UserApi {

    public static final String richard = "richard";
    public static final String richardsPass = "Gau1suph";

    public static final String bob = "bob";
    public static final String bobsPass = "Gau1suph";

    private final String prefix;
    private final String authPrefix;
    private final String loginUrl;
    private final String registerUrl;
    private final String followUrl;

    public UserApi(final String prefix) {
        this.prefix = prefix;
        authPrefix = prefix + "richard/";
        loginUrl = authPrefix + "login";
        registerUrl = authPrefix + "register";
        followUrl = authPrefix + "follow";
    }

    public HttpResponse login(final String username, final String password) throws IOException {
        return postCredentials(username, password, loginUrl);
    }

    public HttpResponse register(final String username, final String password) throws IOException {
        return postCredentials(username, password, registerUrl);
    }

    public HttpResponse follow(final String who) throws IOException {
        return Request.Post(followUrl)
                      .bodyForm(Form.form()
                                    .add("username", who)
                                    .build())
                      .execute()
                      .returnResponse();
    }

    private HttpResponse postCredentials(final String username, final String password, final String url)
            throws IOException {

        return Request.Post(url)
                      .bodyForm(Form.form()
                                    .add("username", username)
                                    .add("password", password)
                                    .build())
                      .execute()
                      .returnResponse();
    }

}
