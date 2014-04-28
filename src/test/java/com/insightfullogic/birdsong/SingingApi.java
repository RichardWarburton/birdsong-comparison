package com.insightfullogic.birdsong;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.rules.ExternalResource;

import java.io.IOException;

public class SingingApi extends ExternalResource {

    private final AuthenticationApi auth;

    private final String singUrl;
    private final String coverUrl;
    private final String listenUrl;

    public SingingApi(final String prefix) {
        auth = new AuthenticationApi();
        singUrl = prefix + "/sing";
        coverUrl = prefix + "/cover/";
        listenUrl = prefix + "/listen";
    }

    @Override
    protected void before() throws IOException {
        auth.defaultLogin();
    }

    public HttpResponse sing(final String song) throws IOException {
        return postText(song, singUrl);
    }

    public HttpResponse cover(final String song, final SongId original) throws IOException {
        return postText(song, coverUrl + original);
    }

    private HttpResponse postText(final String song, final String url) throws IOException {
        return Request.Post(url)
                .bodyString(song, ContentType.TEXT_PLAIN)
                .execute()
                .returnResponse();
    }

    public SongBook listen(final Cursor since) {
        return null;
    }

}
