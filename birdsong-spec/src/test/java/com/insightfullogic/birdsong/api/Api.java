package com.insightfullogic.birdsong.api;

import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Api extends ExternalResource {

    private final String username;
    private final String password;

    public final UserApi users;
    public final SingingApi singing;

    public Api(String username, String password, String prefix) {
        this.username = username;
        this.password = password;
        users = new UserApi(prefix);
        singing = new SingingApi(prefix, users);
    }

    @Override
    protected void before() throws IOException {
        users.login(username, password);
    }

    public void assertNoFeedOrNotificationEntries() throws IOException {
        SongBook songs = singing.listen();
        assertTrue(songs.feed().isEmpty());
        assertTrue(songs.notifies().isEmpty());
    }

    public void notifiedOf(String user, String lyrics) throws IOException {
        List<Song> notifies = singing.listen().notifies();
        assertEquals(1,notifies.size());

        Song song = notifies.get(0);
        assertEquals(user, song.getSinger());
        assertEquals(lyrics, song.getSong());
    }

}
