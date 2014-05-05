package com.insightfullogic.birdsong.spark_in_memory;

import com.insightfullogic.birdsong.Users;
import spark.*;

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
 * Trivial, Dumb, In-memory, Ill factored implementation
 */
public class Birdsong {

    public static void main(String[] args) {
        new Birdsong();
    }

    private final UserService users;
    private final Controllers controllers;

    public Birdsong() {
        users = new UserService();
        controllers = new Controllers(users);
        reset();
    }

    public void reset() {
        users.clear();
        controllers.clear();

        users.addUser(Users.richard, Users.richardsPass);
        users.addUser(Users.bob, Users.bobsPass);
    }

}
