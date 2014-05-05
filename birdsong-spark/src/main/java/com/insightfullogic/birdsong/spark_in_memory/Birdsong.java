package com.insightfullogic.birdsong.spark_in_memory;

import com.insightfullogic.birdsong.Users;

import static java.lang.Long.parseLong;

/**
 * Trivial, Dumb, In-memory, Ill factored implementation
 */
public class Birdsong {

    public static void main(String[] args) {
        new Birdsong();
    }

    private final UserService users;
    private final SongService songs;
    private final Controllers controllers;

    public Birdsong() {
        users = new UserService();
        songs = new SongService(users);
        controllers = new Controllers(users, songs);
        reset();
    }

    public void reset() {
        users.clear();
        songs.clear();

        users.addUser(Users.richard, Users.richardsPass);
        users.addUser(Users.bob, Users.bobsPass);
    }

}
