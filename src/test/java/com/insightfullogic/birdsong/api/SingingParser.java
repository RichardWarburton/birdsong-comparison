package com.insightfullogic.birdsong.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.core.JsonToken.*;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SingingParser {
    public static final JsonFactory factory = new JsonFactory();

    public SongBook songBook(Cursor since, InputStream content) throws IOException {
        JsonParser parser = SingingParser.factory.createParser(content);

        Cursor to = null;
        List<Song> feed = null;
        List<Song> notifies = null;
        parser.nextToken(); // {
        while (parser.nextToken() != END_OBJECT && parser.hasCurrentToken()) {
            final String name = parser.getCurrentName();
            requireNonNull(name, () -> "No name for: " + parser.getCurrentToken());
            switch (name) {
                case "to":
                    parser.nextToken();
                    to = new Cursor(parser.getText());
                    break;
                case "feed":
                    feed = readSongs(parser);
                    break;
                case "notifies":
                    notifies = readSongs(parser);
                    break;
                default:
                    unknownField(name);
            }
            parser.nextToken();
        }
        return new SongBook(since, to, feed, notifies);
    }

    private List<Song> readSongs(final JsonParser parser) throws IOException {
        List<Song> songs = new ArrayList<>();
        assertThat(parser.getCurrentToken(), is(START_ARRAY));
        while (parser.nextToken() != END_ARRAY) {
            songs.add(readSong(parser));
        }
        return songs;
    }

    private Song readSong(final JsonParser parser) throws IOException {
        Song.SongFactory factory = new Song.SongFactory();

        assertAtStartOfObject(parser);
        while (parser.nextToken() != END_OBJECT) {
            final String name = parser.getCurrentName();
            parser.nextToken();
            switch (name) {
                case "id":
                    factory.setId(parser.getText());
                    break;
                case "singer":
                    factory.setSinger(parser.getText());
                    break;
                case "song":
                    factory.setSong(parser.getText());
                    break;
                case "timestamp":
                    factory.setTimestamp(parser.getLongValue());
                    break;
                case "covers":
                    factory.setCovers(parser.getText());
                    break;
                default:
                    unknownField(name);
            }
        }

        return factory.make();
    }

    private void assertAtStartOfObject(JsonParser parser) {
        assertThat(parser.getCurrentToken(), is(START_OBJECT));
    }

    private void unknownField(String name) {
        throw new IllegalStateException("Unknown field: " + name);
    }
}
