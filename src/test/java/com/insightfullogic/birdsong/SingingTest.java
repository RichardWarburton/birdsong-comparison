package com.insightfullogic.birdsong;

import com.insightfullogic.birdsong.api.Api;
import com.insightfullogic.birdsong.api.Song;
import com.insightfullogic.birdsong.api.SongBook;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.insightfullogic.birdsong.BirdsongService.address;
import static com.insightfullogic.birdsong.api.UserApi.*;
import static org.junit.Assert.*;

public class SingingTest {

    private static final String doReMi = "doe a deer a female deer";
    private static final String soundOfMusic = "The hills are alive with the sound of music...";
    public static final String hiBob = "Hi @" + bob;

    @Rule
    public static ServiceRule service = new ServiceRule();

    // Forall tests: Given we have two clients and they are logged in
    @Rule
    private final Api richardsClient = new Api(address, richard, richardsPass);

    @Rule
    private final Api bobsClient = new Api(address, bob, bobsPass);

    @Test
    public void followersCanHearSong() throws IOException {
        Given:
        bobsClient.users.follow(richard);

        Instant before = Instant.now();

        When:
        richardsClient.singing.sing(doReMi);
        Instant after = Instant.now();

        // Then
        SongBook songs = bobsClient.singing.listen();
        assertEquals(1, songs.feed().size());

        Song song = songs.feed().get(0);
        assertEquals(richard, song.getSinger());
        assertEquals(doReMi, song.getSong());
        assertFalse(song.getCovers().isPresent());
        assertTrue(before.isBefore(song.getTimestamp()));
        assertTrue(after.isAfter(song.getTimestamp()));
    }

    @Test
    public void onlyFollowersCanHearSongs() throws IOException {
        When:
        richardsClient.singing.sing(doReMi);

        // Then
        SongBook songs = bobsClient.singing.listen();
        assertTrue(songs.feed().isEmpty());
        assertTrue(songs.notifies().isEmpty());
    }

    @Test
    public void songsAreOrderedNewestFirst() throws IOException {
        When:
        richardsClient.singing.sing(doReMi);
        richardsClient.singing.sing(soundOfMusic);

        // Then
        List<Song> songs = bobsClient.singing.listen().feed();
        assertEquals(soundOfMusic, songs.get(0).getSong());
        assertEquals(doReMi, songs.get(1).getSong());
    }

    @Test
    public void mentionsNotifyMentionedUser() throws IOException {
        When:
        richardsClient.singing.sing(hiBob);

        // Then
        List<Song> notifies = bobsClient.singing.listen().notifies();
        assertEquals(1,notifies.size());

        Song song = notifies.get(0);
        assertEquals(richard, song.getSinger());
        assertEquals(hiBob, song.getSong());
        assertFalse(song.getCovers().isPresent());
    }

}
