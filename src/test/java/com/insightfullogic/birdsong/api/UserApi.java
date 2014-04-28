package com.insightfullogic.birdsong.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * .
 */
public class UserApi {

    public static final String user = "richard";
    public static final String pass = "Gau1suph";

    public static final String otherUser = "bob";
    public static final String otherPass = "Gau1suph";

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

    public void defaultLogin() throws IOException {
        login(user, pass);
    }

    public HttpResponse login(final String username, final String password) throws IOException {
        return postCredentials(username, password, loginUrl);
    }

    public HttpResponse register(final String username, final String password) throws IOException {
        return postCredentials(username, password, registerUrl);
    }

    private HttpResponse postCredentials(String username, String password, String url) throws IOException {
        return Request.Post(url)
                      .bodyForm(Form.form()
                                    .add("username", username)
                                    .add("password", password)
                                    .build())
                      .execute().returnResponse();
    }

}
