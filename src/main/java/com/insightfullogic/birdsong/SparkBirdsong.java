package com.insightfullogic.birdsong;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static spark.Spark.post;

/**
 * Trivial, Dumb, In-memory, Ill factored implementation
 */
public class SparkBirdsong implements BirdsongService {
    public static void main(String[] args) {
        new SparkBirdsong().start();
    }

    private final Map<String, String> users;

    public SparkBirdsong() {
        users = new HashMap<>();
        users.put(Users.richard, Users.richardsPass);
        users.put(Users.bob, Users.bobsPass);
    }

    public void start() {
        post(route("/user/login", withCredentials(this::areValidCredentials)));
        post(route("/user/register", withCredentials(this::registerCredentials)));
        post(route("/user/follow", (request, response) -> {
            response.status(500);
            return "";
        }));
    }

    private BiFunction<Request, Response, Object> withCredentials(BiFunction<String, String, Boolean> function) {
        return (request, response) -> {
            final QueryParamsMap params = request.queryMap();
            String username = params.get("username").value();
            String password = params.get("password").value();
            final boolean ok = function.apply(username, password);
            response.status(ok ? 500 : 403);
            return "";
        };
    }

    private boolean registerCredentials(final String username, final String password) {
        if (users.containsKey(username)) {
            return false;
        }

        users.put(username, password);
        return true;
    }

    private boolean areValidCredentials(final String username, final String password) {
        final String expectedPassword = users.get(username);
        return expectedPassword != null && expectedPassword.equals(password);
    }

    public void stop() {
        // Horrific Hack required by framework
        try {
            //Spark.class.getMethod("clearRoutes").invoke(null);
            //Spark.class.getMethod("stop").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Route route(final String url, final BiFunction<Request, Response, Object> handle) {
        return new Route(url) {
            public Object handle(final Request request, final Response response) {
                return handle.apply(request, response);
            }
        };
    }

}
