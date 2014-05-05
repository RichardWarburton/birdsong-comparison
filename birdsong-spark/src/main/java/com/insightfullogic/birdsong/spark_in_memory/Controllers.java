/*
 * Copyright 2014 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.insightfullogic.birdsong.spark_in_memory;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * .Spark framework specific controllers.
 */
public class Controllers {

    private static final Pattern mentions = Pattern.compile("@([^ ]+)");
    private static final int MAX_LENGTH_OF_LYRICS = 140;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String AUTH_USERNAME = "authenticatedUsername";
    public static final String AUTH_PASSWORD = "authenticatedPassword";

    private final UserService users;
    private final Map<SongId, User> originalUser;

    public Controllers(UserService users) {
        this.users = users;
        originalUser = new HashMap<>();

        post(route("/user/login", withCredentials(users::areValidCredentials)));
        post(route("/user/register", withCredentials(users::registerCredentials)));

        post(route("/user/follow", ifAuthenticated((request, response) -> {
            final User toFollow = getUserParam(request);
            final User follower = getUser(request);
            toFollow.newFollower(follower);
        })));

        post(route("/user/unfollow", ifAuthenticated((request, response) -> {
            final User toFollow = getUserParam(request);
            final User follower = getUser(request);
            toFollow.unfollow(follower);
        })));

        post(route("/sing", ifAuthenticated((request, response) -> {
            validateLyrics(request, response, lyrics -> {
                sing(request, lyrics, null);
            });
        })));

        post(route("/cover/:of", ifAuthenticated((request, response) -> {
            validateLyrics(request, response, lyrics -> {
                SongId of = new SongId(request.params("of"));
                Song cover = sing(request, lyrics, of);
                originalUser.get(of).pushNotification(cover);
            });
        })));

        get(route("/listen/:since", ifAuthenticated((request, response) -> {
            String since = request.params("since");
            listen(request, parseLong(since), response);
        })));

        get(route("/user/information/:about", (request, response) -> {
            User about = users.get(request.params("about"));
            try (ServletOutputStream out = response.raw().getOutputStream()) {
                new JsonInformation(about, out).generate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private void listen(Request request, long since, Response response) {
        final User user = getUser(request);
        try (ServletOutputStream out = response.raw().getOutputStream()) {
            new JsonFeed(user, since, out).generate();
        } catch (IOException e) {
            e.printStackTrace();
            response.status(500);
        }
    }

    private Song sing(Request request, String lyrics, SongId of) {
        final User user = getUser(request);
        SongId id = SongId.next();
        Song song = new Song(id, user.getUsername(), lyrics, currentTimeMillis(), of);
        user.sing(song);
        findMentions(song);
        originalUser.put(id, user);
        return song;
    }

    private void validateLyrics(Request request, Response response, Consumer<String> handler) {
        String lyrics = request.body();
        if (lyrics.length() > MAX_LENGTH_OF_LYRICS) {
            response.status(400);
            return;
        }

        handler.accept(lyrics);
    }

    private void findMentions(Song song) {
        final String lyrics = song.getLyrics();
        final Matcher matcher = mentions.matcher(lyrics);
        while (matcher.find()) {
            final String name = matcher.group(1);
            users.get(name).pushNotification(song);
        }
    }

    private Handle ifAuthenticated(Handle function) {
        return (request, response) -> {
            if (users.areValidCredentials(request.cookie(AUTH_USERNAME), request.cookie(AUTH_PASSWORD))) {
                function.accept(request, response);
            } else {
                response.status(403);
            }
        };
    }

    private Handle withCredentials(BiFunction<String, String, Boolean> function) {
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
        };
    }

    private void setCookie(Response response, String value, String key) {
        response.cookie("/", key, value, 1, false);
    }

    public void clear() {
        originalUser.clear();
    }

    public static interface Handle extends BiConsumer<Request, Response> {}

    private static Route route(String url, Handle handle) {
        return new Route(url) {
            public Object handle(Request request, Response response) {
                handle.accept(request, response);
                return "";
            }
        };
    }


    private User getUserParam(Request request) {
        final QueryParamsMap params = request.queryMap();
        return users.get(params.get(USERNAME).value());
    }

    private User getUser(Request request) {
        return users.get(request.cookie(AUTH_USERNAME));
    }

}
