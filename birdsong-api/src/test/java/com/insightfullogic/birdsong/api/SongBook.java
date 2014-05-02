package com.insightfullogic.birdsong.api;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

public class SongBook {

    private final Cursor from;
    private final Cursor to;
    private final List<Song> feed;
    private final List<Song> notifies;

    public SongBook(final Cursor from, final Cursor to, final List<Song> feed, final List<Song> notifies) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        this.from = from;
        this.to = to;
        this.feed = nonNull(feed);
        this.notifies = nonNull(notifies);
    }

    private List<Song> nonNull(List<Song> value) {
        return value == null ? emptyList() : value;
    }

    public Cursor getFrom() {
        return from;
    }

    public Cursor getTo() {
        return to;
    }

    public List<Song> feed() {
        return feed;
    }

    public List<Song> notifies() {
        return notifies;
    }
}
