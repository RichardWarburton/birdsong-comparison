package com.insightfullogic.birdsong;

import java.util.List;
import java.util.stream.Stream;

public class SongBook {

    private final Cursor from;
    private final Cursor to;
    private final List<Song> feed;
    private final List<Song> notifies;

    public SongBook(final Cursor from, final Cursor to, final List<Song> feed, final List<Song> notifies) {
        this.from = from;
        this.to = to;
        this.feed = feed;
        this.notifies = notifies;
    }

    public Cursor getFrom() {
        return from;
    }

    public Cursor getTo() {
        return to;
    }

    public Stream<Song> feed() {
        return feed.stream();
    }

    public Stream<Song> notifies() {
        return notifies.stream();
    }
}
