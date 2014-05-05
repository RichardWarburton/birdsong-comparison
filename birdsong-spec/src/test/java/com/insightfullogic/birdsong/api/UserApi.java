package com.insightfullogic.birdsong.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import static com.insightfullogic.birdsong.HttpAsserts.assertHttpOk;
import static org.junit.Assert.assertEquals;

/**
 * .
 */
public class UserApi {

    private final String authPrefix;
    private final String loginUrl;
    private final String registerUrl;
    private final String followUrl;
    private final String unfollowUrl;
    private final String informationUrl;

    private final CookieStore cookies;
    private final Executor executor;

    public UserApi(final String prefix) {
        authPrefix = prefix + "user/";
        loginUrl = authPrefix + "login";
        registerUrl = authPrefix + "register";
        followUrl = authPrefix + "follow";
        unfollowUrl = authPrefix + "unfollow";
        informationUrl = authPrefix + "information/";

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

    public Information information(String about) throws IOException {
        System.out.println(informationUrl + about);
        InputStream data = executor.execute(Request.Get(informationUrl + about))
                                   .returnContent()
                                   .asStream();

        return Information.parse(data);
    }

    public void assertFollows(String about, String other) throws IOException {
        assertUserRelationship(about, other, Information::getFollowers);
    }

    public void assertFollowing(String about, String other) throws IOException {
        assertUserRelationship(about, other, Information::getFollowing);
    }

    private void assertUserRelationship(String about, String other, Function<Information, List<String>> func) throws IOException {
        List<String> following = func.apply(information(about));
        assertEquals(1, following.size());
        assertEquals(other, following.get(0));
    }

}
