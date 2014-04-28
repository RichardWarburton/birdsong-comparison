package com.insightfullogic.birdsong.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;

public class SingingApi {

    private static final JsonFactory factory = new JsonFactory();

    private final UserApi auth;

    private final String singUrl;
    private final String coverUrl;
    private final String listenUrl;

    public SingingApi(final String prefix, final UserApi auth) {
        this.auth = auth;
        singUrl = prefix + "/sing";
        coverUrl = prefix + "/cover/";
        listenUrl = prefix + "/listen/";
    }

    public HttpResponse sing(final String song) throws IOException {
        return postText(song, singUrl);
    }

    public HttpResponse cover(final String song, final SongId original) throws IOException {
        return postText(song, coverUrl + original);
    }

    public SongBook listen() throws IOException {
        return listen(Cursor.forever);
    }

    public SongBook listen(final Cursor since) throws IOException {
        final InputStream content = Request.Get(listenUrl + since)
                                           .execute()
                                           .returnContent()
                                           .asStream();

        JsonParser parser = factory.createParser(content);
        Cursor to = null;
        List<Song> feed = null;
        List<Song> notifies = null;
        while (parser.nextToken() != END_OBJECT) {
            final String name = parser.getCurrentName();
            switch (name) {
                case "to":
                    to = new Cursor(parser.getText());
                    break;
                case "feed":
                    feed = readSongs(parser);
                    break;
                case "notifies":
                    notifies = readSongs(parser);
                    break;
                default:
                    throw new IllegalStateException("Unknown field: " + name);
            }
            parser.nextToken();
        }
        return new SongBook(since, to, feed, notifies);
    }

    private List<Song> readSongs(final JsonParser parser) throws IOException {
        List<Song> songs = new ArrayList<>();
        parser.nextToken(); // [
        while (parser.nextToken() != END_ARRAY) {
            songs.add(readSong(parser));
        }
        parser.nextToken(); // ]
        return songs;
    }

    private Song readSong(final JsonParser parser) throws IOException {
        SongId id = null;
        String singer = null;
        String song = null;
        long timestamp = -1;
        SongId covers = null;

        while (parser.nextToken() != END_OBJECT) {
            final String name = parser.getCurrentName();
            switch (name) {
                case "id":
                    id = new SongId(parser.getText());
                    break;
                case "singer":
                    singer = parser.getText();
                    break;
                case "song":
                    song = parser.getText();
                    break;
                case "timestamp":
                    timestamp = parser.getLongValue();
                    break;
                case "covers":
                    covers = new SongId(parser.getText());
                    break;
                default:
                    throw new IllegalStateException("Unknown field: " + name);
            }
        }

        return new Song(id, singer, song, timestamp, covers);
    }

    private HttpResponse postText(final String song, final String url) throws IOException {
        return Request.Post(url)
                .bodyString(song, ContentType.TEXT_PLAIN)
                .execute()
                .returnResponse();
    }

}
