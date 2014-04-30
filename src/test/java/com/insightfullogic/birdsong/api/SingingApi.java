package com.insightfullogic.birdsong.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SingingApi {

    private static final JsonFactory factory = new JsonFactory();

    private final UserApi auth;

    private final String singUrl;
    private final String coverUrl;
    private final String listenUrl;

    private final Executor executor;
    private final CookieStore cookies;

    public SingingApi(final String prefix, final UserApi auth) {
        this.auth = auth;
        singUrl = prefix + "/sing";
        coverUrl = prefix + "/cover/";
        listenUrl = prefix + "/listen/";
        executor = auth.getExecutor();
        cookies = auth.getCookies();
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
        //  + since
        final InputStream content = executor.execute(Request.Get(listenUrl))
                                            .returnContent()
                                            .asStream();

        JsonParser parser = factory.createParser(content);

        // This makes me want to vomit
        // TODO: refactor
        Cursor to = null;
        List<Song> feed = null;
        List<Song> notifies = null;
        parser.nextToken(); // {
        while (parser.nextToken() != END_OBJECT) {
            final JsonToken token = parser.getCurrentToken();
            if (token == null) {
                break;
            }

            final String name = parser.getCurrentName();
            if (name == null) {
                throw new IllegalStateException("No name for: " + token);
            }
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
                    throw new IllegalStateException("Unknown field: " + name);
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
        SongId id = null;
        String singer = null;
        String song = null;
        long timestamp = -1;
        SongId covers = null;

        assertThat(parser.getCurrentToken(), is(START_OBJECT));
        while (parser.nextToken() != END_OBJECT) {
            final String name = parser.getCurrentName();
            parser.nextToken();
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
        return executor.execute(Request.Post(url).bodyString(song, TEXT_PLAIN))
                       .returnResponse();
    }

}
