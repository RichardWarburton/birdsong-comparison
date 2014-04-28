package com.insightfullogic.birdsong;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * .
 */
public class AuthenticationApi {

    public static final String user = "richard";
    public static final String pass = "Gau1suph";

    public static final String prefix = "http://localhost:4567/";

    private static final String authPrefix = prefix + "auth/";
    private static final String AUTH_URL = authPrefix + "login";
    private static final String REGISTER_URL = authPrefix + "register";

    public HttpResponse login(final String username, final String password) throws IOException {
        return Request.Post(AUTH_URL)
                .addHeader("username", username)
                .addHeader("password", password)
                .execute().returnResponse();
    }

    public void defaultLogin() throws IOException {
        login(user, pass);
    }

    public HttpResponse register(final String username, final String password) throws IOException {
        return Request.Post(REGISTER_URL)
                .addHeader("username", username)
                .addHeader("password", password)
                .execute().returnResponse();
    }

}
