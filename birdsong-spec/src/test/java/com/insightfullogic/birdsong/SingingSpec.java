package com.insightfullogic.birdsong;

import com.insightfullogic.birdsong.api.Api;
import com.insightfullogic.birdsong.api.Song;
import com.insightfullogic.birdsong.api.SongBook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.insightfullogic.birdsong.BirdsongApplicationRunner.address;
import static com.insightfullogic.birdsong.HttpAsserts.assertHttpOk;
import static com.insightfullogic.birdsong.Users.*;
import static org.junit.Assert.*;

public class SingingSpec {

    private static final String doReMi = "doe a deer a female deer";
    private static final String soundOfMusic = "The hills are alive with the sound of music...";
    public static final String hiBob = "Hi @" + bob;

    // Forall tests: Given we have two clients and they are logged in
    public final Api richardsClient = new Api(richard, richardsPass, address);
    public final Api bobsClient = new Api(bob, bobsPass, address);

    @Rule
    public final TestRule rules = RuleChain.outerRule(new ServiceRule())
                                           .around(richardsClient)
                                           .around(bobsClient);

    @Test
    public void followersCanHearSong() throws IOException {
        SongBook songs;

        Given:
        bobsClient.users.follow(richard);

        Instant before = Instant.now();

        When:
        richardsClient.singing.sing(doReMi);
        Instant after = Instant.now();

        Then:
        songs = bobsClient.singing.listen();
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
        SongBook songs;

        When:
        richardsClient.singing.sing(doReMi);

        Then:
        songs = bobsClient.singing.listen();
        assertTrue(songs.feed().isEmpty());
        assertTrue(songs.notifies().isEmpty());
    }

    @Test
    public void songsAreOrderedNewestFirst() throws IOException {
        List<Song> feed;

        Given:
        bobsClient.users.follow(richard);

        When:
        richardsClient.singing.sing(doReMi);
        richardsClient.singing.sing(soundOfMusic);

        Then:
        feed = bobsClient.singing.listen().feed();
        assertEquals(soundOfMusic, feed.get(0).getSong());
        assertEquals(doReMi, feed.get(1).getSong());
    }

    @Test
    public void mentionsNotifyMentionedUser() throws IOException {
        List<Song> notifies;

        When:
        richardsClient.singing.sing(hiBob);

        Then:
        notifies = bobsClient.singing.listen().notifies();
        assertEquals(1,notifies.size());

        Song song = notifies.get(0);
        assertEquals(richard, song.getSinger());
        assertEquals(hiBob, song.getSong());
        assertFalse(song.getCovers().isPresent());
    }

}
