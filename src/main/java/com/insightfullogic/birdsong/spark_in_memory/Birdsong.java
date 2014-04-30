package com.insightfullogic.birdsong.spark_in_memory;

import com.insightfullogic.birdsong.Users;
import spark.*;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.currentTimeMillis;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Trivial, Dumb, In-memory, Ill factored implementation
 */
public class Birdsong {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String AUTH_USERNAME = "authenticatedUsername";
    public static final String AUTH_PASSWORD = "authenticatedPassword";

    private static final Pattern mentions = Pattern.compile("@([^ ]+)");

    public static void main(String[] args) {
        new Birdsong();
    }

    private final Map<String, User> users;

    public Birdsong() {
        users = new HashMap<>();
        addUser(Users.richard, Users.richardsPass);
        addUser(Users.bob, Users.bobsPass);

        post(route("/user/login", withCredentials(this::areValidCredentials)));
        post(route("/user/register", withCredentials(this::registerCredentials)));

        post(route("/user/follow", ifAuthenticated((request, response) -> {
            final QueryParamsMap params = request.queryMap();
            final User toFollow = users.get(params.get(USERNAME).value());
            final User follower = getUser(request);

            toFollow.newFollower(follower);

            response.status(200);
            return "";
        })));

        post(route("/sing", ifAuthenticated((request, response) -> {
            final User user = getUser(request);
            Song song = new Song(SongId.next(), user.getUsername(), request.body(), currentTimeMillis());
            user.sing(song);
            findMentions(song);

            response.status(200);
            return "";
        })));

        get(route("/listen/", ifAuthenticated((request, response) -> {
            try (ServletOutputStream out = response.raw().getOutputStream()) {
                final User user = getUser(request);
                final FeedGenerator generator = new FeedGenerator(user, out);
                generator.generate();
                response.status(200);
            } catch (IOException e) {
                e.printStackTrace();
                response.status(500);
            }

            return "";
        })));
    }

    private void findMentions(Song song) {
        final String lyrics = song.getLyrics();
        final Matcher matcher = mentions.matcher(lyrics);
        while (matcher.find()) {
            final String name = matcher.group(1);
            users.get(name).pushNotification(song);
        }
    }

    private void addUser(String username, String password) {
        users.put(username, new User(username, password));
    }

    private User getUser(Request request) {
        return users.get(request.cookie(AUTH_USERNAME));
    }

    private BiFunction<Request, Response, Object> ifAuthenticated(BiFunction<Request, Response, Object> function) {
        return (request, response) -> {
            final Map<String, String> cookies = request.cookies();

            if (areValidCredentials(cookies.get(AUTH_USERNAME), cookies.get(AUTH_PASSWORD))) {
                return function.apply(request, response);
            }

            response.status(403);
            return "";
        };
    }

    private BiFunction<Request, Response, Object> withCredentials(BiFunction<String, String, Boolean> function) {
        return (request, response) -> {
            final QueryParamsMap params = request.queryMap();
            String username = params.get(USERNAME).value();
            String password = params.get(PASSWORD).value();

            final boolean ok = function.apply(username, password);

            if (ok) {
                setCookie(response, username, AUTH_USERNAME);
                setCookie(response, password, AUTH_PASSWORD);
            }

            response.status(ok ? 200 : 403);
            return "";
        };
    }

    private void setCookie(Response response, String value, String key) {
        response.cookie("/", key, value, 1, false);
    }

    private boolean registerCredentials(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }

        addUser(username, password);
        return true;
    }

    private boolean areValidCredentials(String username, String password) {
        User user = users.get(username);
        return user != null && user.hasPassword(password);
    }

    private static Route route(String url, BiFunction<Request, Response, Object> handle) {
        return new Route(url) {
            public Object handle(Request request, Response response) {
                return handle.apply(request, response);
            }
        };
    }

}