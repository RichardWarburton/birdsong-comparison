package com.insightfullogic.birdsong.spark_in_memory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FeedGenerator {

    private static final JsonFactory json = new JsonFactory();

    private final User user;
    private final JsonGenerator generator;

    public FeedGenerator(User user, OutputStream out) throws IOException {
        this.user = user;
        generator = json.createGenerator(out);
    }

    public void generate() throws IOException {
        generator.writeStartObject();
        generator.writeStringField("to", "1");
        generateFeed("feed", user.getFeed());
        generateFeed("notifies", user.getFeed());
        generator.writeEndObject();
    }

    private void generateFeed(String name, List<Song> feed) throws IOException {
        generator.writeArrayFieldStart(name);
        for (Song song : feed) {
            generator.writeStringField("id", "1");
            generator.writeStringField("singer", song.getSinger());
            generator.writeStringField("song", song.getSong());
            generator.writeNumberField("timestamp", song.getTimestamp().toEpochMilli());
        }
        generator.writeEndArray();
    }
}
