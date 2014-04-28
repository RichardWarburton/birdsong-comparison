package com.insightfullogic.birdsong.spark_in_memory;

import java.util.*;

/**
 * Gloriously not thread safe
 */
public class User {

    private final String username;
    private final String password;
    private final List<Song> feed;
    private final List<Song> notifications;
    private final Set<User> followers;

    public User(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        this.username = username;
        this.password = password;
        followers = new HashSet<>();
        feed = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    public void sing(Song song) {
        followers.forEach(follower -> follower.pushFeed(song));
    }

    public void newFollower(User user) {
        followers.add(user);
    }

    public void pushFeed(Song song) {
        feed.add(song);
    }

    public void pushNotification(Song song) {
        notifications.add(song);
    }

    public boolean hasPassword(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public List<Song> getFeed() {
        return feed;
    }
}
