package com.insightfullogic.birdsong.spark_in_memory;

import java.util.*;

/**
 * Gloriously not thread safe
 */
public class User {

    private final String username;
    private final String password;
    private final LinkedList<Song> feed;

    private final LinkedList<Song> notifications;

    private final Set<User> followers;

    public User(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        this.username = username;
        this.password = password;
        followers = new HashSet<>();
        feed = new LinkedList<>();
        notifications = new LinkedList<>();
    }

    public void sing(Song song) {
        followers.forEach(follower -> follower.pushFeed(song));
    }

    public void newFollower(User user) {
        followers.add(user);
    }

    public void pushFeed(Song song) {
        feed.addFirst(song);
    }

    public void pushNotification(Song song) {
        notifications.addFirst(song);
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

    public List<Song> getNotifications() {
        return notifications;
    }

    public void unfollow(User user) {
        followers.remove(user);
    }
}
