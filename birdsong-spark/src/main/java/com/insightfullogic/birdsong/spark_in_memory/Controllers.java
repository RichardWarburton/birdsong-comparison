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
 * .Controllers for http requests. Any spark framework specific code should be located here.
 */
public class Controllers {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String AUTH_USERNAME = "authenticatedUsername";
    public static final String AUTH_PASSWORD = "authenticatedPassword";

    private final UserService users;
    private final SongService songs;

    public Controllers(UserService users, SongService songs) {
        this.users = users;
        this.songs = songs;

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
            songs.validateLyrics(request, response, lyrics -> {
                final User user = getUser(request);
                songs.sing(user, lyrics, null);
            });
        })));

        post(route("/cover/:of", ifAuthenticated((request, response) -> {
            songs.validateLyrics(request, response, lyrics -> {
                final User user = getUser(request);
                SongId of = new SongId(request.params("of"));
                songs.sing(user, lyrics, of);
            });
        })));

        get(route("/listen/:since", ifAuthenticated((request, response) -> {
            String since = request.params("since");
            try (ServletOutputStream out = response.raw().getOutputStream()) {
                new JsonFeed(getUser(request), parseLong(since), out).generate();
            } catch (IOException e) {
                e.printStackTrace();
                response.status(500);
            }
        })));

        get(route("/user/information/:about", (request, response) -> {
            User about = users.lookupByName(request.params("about"));
            try (ServletOutputStream out = response.raw().getOutputStream()) {
                new JsonInformation(about, out).generate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    // TODO: refactor this to be a filter
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
        return users.lookupByName(params.get(USERNAME).value());
    }

    private User getUser(Request request) {
        return users.lookupByName(request.cookie(AUTH_USERNAME));
    }

}
