package com.insightfullogic.birdsong.api;

import java.time.Instant;
import java.util.Optional;

public class Song {

    private final SongId id;
    private final String singer;
    private final String song;
    private final Instant timestamp;
    private final Optional<SongId> covers;

    private Song(final SongId id, final String singer, final String song, final long timestamp, final SongId covers) {
        this.singer = singer;
        this.song = song;
        this.id = id;
        this.covers = Optional.ofNullable(covers);
        this.timestamp = Instant.ofEpochMilli(timestamp);
    }

    public SongId getId() {
        return id;
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

    public static class SongFactory {

        private SongId id;
        private String singer;
        private String song;
        private long timestamp = -1;
        private SongId covers;

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public void setCovers(String covers) {
            this.covers = new SongId(covers);
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public void setSong(String song) {
            this.song = song;
        }

        public void setId(String id) {
            this.id = new SongId(id);
        }

        public Song make() {
            return new Song(id, singer, song, timestamp, covers);
        }
    }

}
