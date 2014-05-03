package com.insightfullogic.birdsong.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.IOException;

import static com.insightfullogic.birdsong.HttpAsserts.assertHttpOk;

/**
 * .
 */
public class UserApi {

    private final String authPrefix;
    private final String loginUrl;
    private final String registerUrl;
    private final String followUrl;
    private final String unfollowUrl;

    private final CookieStore cookies;
    private final Executor executor;

    public UserApi(final String prefix) {
        authPrefix = prefix + "user/";
        loginUrl = authPrefix + "login";
        registerUrl = authPrefix + "register";
        followUrl = authPrefix + "follow";
        unfollowUrl = authPrefix + "unfollow";

        cookies = new BasicCookieStore();
        executor = Executor.newInstance().cookieStore(cookies);
    }

    public Executor getExecutor() {
        return executor;
    }

    public void login(final String username, final String password) throws IOException {
        assertHttpOk(tryLogin(username, password));
    }

    public HttpResponse tryLogin(String username, String password) throws IOException {
        return postCredentials(username, password, loginUrl);
    }

    public HttpResponse register(final String username, final String password) throws IOException {
        return postCredentials(username, password, registerUrl);
    }

    public void follow(final String who) throws IOException {
        postUsernameTo(who, followUrl);
    }

    private void postUsernameTo(String who, String url) throws IOException {
        final Request request = Request.Post(url)
                                       .bodyForm(Form.form()
                                               .add("username", who)
                                               .build());

        executeOk(request);
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

    private void executeOk(Request request) throws IOException {
        assertHttpOk(executor.execute(request).returnResponse());
    }

    public void unfollow(String who) throws IOException {
        postUsernameTo(who, unfollowUrl);
    }

}
