package com.insightfullogic.spring_boot_impl;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.persistence.CascadeType.ALL;

/**
 * .
 */
@Entity
public class Song implements Comparable<Song> {

    private static final Pattern mentionFinder = Pattern.compile("@([^ ]+)");

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String song;

    @ManyToOne(optional=false, cascade = ALL)
    private User singer;

    @JsonIgnore
    @ManyToOne(optional=true, cascade = ALL)
    private Song coverOf;

    @Column(nullable=false)
    private long timestamp;

    protected Song() {

    }

    public Song(String song, User singer, Song coverOf) {
        this.song = song;
        this.singer = singer;
        this.coverOf = coverOf;
        timestamp = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public String getSong() {
        return song;
    }

    public String getSinger() {
        return singer.getName();
    }

    public String getCovers() {
        if (coverOf == null) {
            return null;
        }

        return coverOf.getId().toString();
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", song='" + song + '\'' +
                ", singer=" + singer +
                ", coverOf=" + coverOf +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int compareTo(Song o) {
        return -1 * Long.compare(timestamp, o.timestamp);
    }

    public List<String> findMentions() {
        List<String> mentions = new ArrayList<>();
        Matcher matcher = mentionFinder.matcher(song);
        while (matcher.find()) {
            mentions.add(matcher.group(1));
        }
        return mentions;
    }

    public void onCover(Song song) {
        singer.newMention(song);
    }
}
