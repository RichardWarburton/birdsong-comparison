package com.insightfullogic.birdsong.spark_in_memory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;

public class JsonFeed {

    private static final JsonFactory json = new JsonFactory();

    private final User user;
    private final String to;
    private final Instant since;
    private final JsonGenerator generator;

    public JsonFeed(User user, long since, OutputStream out) throws IOException {
        this.user = user;
        to = String.valueOf(System.currentTimeMillis());
        this.since = Instant.ofEpochMilli(since);
        generator = json.createGenerator(out);
    }

    public void generate() throws IOException {
        generator.writeStartObject();
        generator.writeStringField("to", to);
        generateFeed("feed", user.getFeed());
        generateFeed("notifies", user.getNotifications());
        generator.writeEndObject();
        generator.flush();
        generator.close();
    }

    private void generateFeed(String name, List<Song> feed) throws IOException {
        generator.writeArrayFieldStart(name);
        for (Song song : feed) {
            if (since.isAfter(song.getTimestamp())) {
                continue;
            }

            generator.writeStartObject();
            generator.writeStringField("id", "1");
            generator.writeStringField("singer", song.getSinger());
            generator.writeStringField("song", song.getLyrics());
            generator.writeNumberField("timestamp", song.getTimestamp().toEpochMilli());
            generator.writeEndObject();
        }
        generator.writeEndArray();
    }
}
