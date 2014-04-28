package com.insightfullogic.birdsong.api;

import java.time.Instant;
import java.util.Optional;

public class Song {

    private final SongId id;
    private final String singer;
    private final String song;
    private final Instant timestamp;
    private final Optional<SongId> covers;

    public Song(final SongId id, final String singer, final String song, final long timestamp, final SongId covers) {
        this.singer = singer;
        this.song = song;
        this.id = id;
        this.covers = Optional.ofNullable(covers);
        this.timestamp = Instant.ofEpochMilli(timestamp);
    }

    public String getSinger() {
        return singer;
    }

    public String getSong() {
        return song;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Optional<SongId> getCovers() {
        return covers;
    }
}
