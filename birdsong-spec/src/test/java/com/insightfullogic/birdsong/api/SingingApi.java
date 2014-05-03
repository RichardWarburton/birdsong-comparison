package com.insightfullogic.birdsong.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.InputStream;

import static com.insightfullogic.birdsong.HttpAsserts.assertHttpOk;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;

public class SingingApi {

    private static final SingingParser parser = new SingingParser();

    private final String singUrl;
    private final String coverUrl;
    private final String listenUrl;

    private final Executor executor;

    public SingingApi(final String prefix, final UserApi auth) {
        singUrl = prefix + "/sing";
        coverUrl = prefix + "/cover/";
        listenUrl = prefix + "/listen/";
        executor = auth.getExecutor();
    }

    public void sing(final String song) throws IOException {
        assertHttpOk(trySing(song));
    }

    public HttpResponse trySing(String song) throws IOException {
        return postText(song, singUrl);
    }

    public HttpResponse cover(final String song, final SongId original) throws IOException {
        return postText(song, coverUrl + original);
    }

    public SongBook listen() throws IOException {
        return listen(Cursor.forever);
    }

    public SongBook listen(final Cursor since) throws IOException {
        final InputStream content = executor.execute(Request.Get(listenUrl + since))
                                            .returnContent()
                                            .asStream();

        return parser.songBook(since, content);
    }

    private HttpResponse postText(final String song, final String url) throws IOException {
        return executor.execute(Request.Post(url).bodyString(song, TEXT_PLAIN))
                       .returnResponse();
    }

}
