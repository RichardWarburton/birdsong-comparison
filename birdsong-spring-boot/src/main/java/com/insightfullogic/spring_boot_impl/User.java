package com.insightfullogic.spring_boot_impl;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;
import static javax.persistence.CascadeType.ALL;

/**
 * .
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    @Column(nullable=false)
    private String password;

    @ManyToMany(cascade = ALL)
    private Set<User> following;

    @ManyToMany(mappedBy="following", cascade = ALL)
    private Set<User> followers;

    @ManyToMany(cascade = ALL)
    @OrderBy("timestamp")
    private SortedSet<Song> feed;

    @ManyToMany(cascade = ALL)
    @OrderBy("timestamp")
    private SortedSet<Song> mentions;

    protected User() {

    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        following = new HashSet<>();
        followers = new HashSet<>();
        feed = new TreeSet<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @JsonIgnore
    public Set<User> getFollowing() {
        return following;
    }

    @JsonIgnore
    public Set<User> getFollowers() {
        return followers;
    }

    public SortedSet<Song> mentionsSince(long since) {
        return songsSince(mentions, since);
    }

    public SortedSet<Song> feedSince(long since) {
        return songsSince(feed, since);
    }

    private SortedSet<Song> songsSince(SortedSet<Song> songs, long since) {
        return songs.stream()
                    .filter(song -> song.getTimestamp() >= since)
                    .collect(toCollection(TreeSet::new));
    }

    public void newFeedEntry(Song song) {
        feed.add(song);
    }

    public void newMention(Song song) {
        mentions.add(song);
    }

}
