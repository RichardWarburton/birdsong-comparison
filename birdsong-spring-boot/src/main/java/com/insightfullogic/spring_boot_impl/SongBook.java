package com.insightfullogic.spring_boot_impl;

import java.util.SortedSet;

import static java.util.Collections.emptySortedSet;

public class SongBook {

    private final String to;
    private final SortedSet<Song> feed;
    private final SortedSet<Song> notifies;

    public SongBook(final long to, final SortedSet<Song> feed, final SortedSet<Song> notifies) {
        this.to = String.valueOf(to);
        this.feed = nonNull(feed);
        this.notifies = nonNull(notifies);
    }

    private SortedSet<Song> nonNull(SortedSet<Song> value) {
        return value == null ? emptySortedSet() : value;
    }

    public String getTo() {
        return to;
    }

    public SortedSet<Song> getFeed() {
        return feed;
    }

    public SortedSet<Song> getNotifies() {
        return notifies;
    }

    @Override
    public String toString() {
        return "SongBook{" +
                ", to=" + to +
                ", feed=" + feed +
                ", notifies=" + notifies +
                '}';
    }
}
