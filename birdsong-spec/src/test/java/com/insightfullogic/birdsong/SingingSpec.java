package com.insightfullogic.birdsong;

import com.insightfullogic.birdsong.api.*;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.insightfullogic.birdsong.BirdsongApplicationRunner.address;
import static com.insightfullogic.birdsong.HttpAsserts.assertResponseCodeIs;
import static com.insightfullogic.birdsong.Users.*;
import static org.junit.Assert.*;

public class SingingSpec {

    private static final String doReMi = "doe a deer a female deer";
    private static final String soundOfMusic = "The hills are alive with the sound of music...";
    private static final String longSong = "utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, utz, ";

    public static final String hiBob = "Hi @" + bob;

    // Forall tests: Given we have two clients and they are logged in
    public final Api richardsClient = new Api(richard, richardsPass, address);
    public final Api bobsClient = new Api(bob, bobsPass, address);

    @ClassRule
    public static ServiceRule service = new ServiceRule();

    @Rule
    public TestRule rules = RuleChain.outerRule(new ResetRule())
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

        Instant timestamp = song.getTimestamp();
        assertTrue(before.compareTo(timestamp) <= 0);
        assertTrue(after.compareTo(song.getTimestamp()) >= 0);
    }

    @Test
    public void listenOnlyReturnsResultsSinceDay() throws IOException {
        SongBook songs;

        Given:
        bobsClient.users.follow(richard);

        When:
        richardsClient.singing.sing(doReMi);
        Cursor cursor = bobsClient.singing.listen().getTo();
        richardsClient.singing.sing(soundOfMusic);

        Then:
        songs = bobsClient.singing.listen(cursor);
        assertEquals(1, songs.feed().size());

        Song song = songs.feed().get(0);
        assertEquals(richard, song.getSinger());
        assertEquals(soundOfMusic, song.getSong());
    }

    @Test
    public void cantHearSongsIfNotFollowing() throws IOException {
        When:
        richardsClient.singing.sing(doReMi);

        Then:
        bobsClient.assertNoFeedOrNotificationEntries();
    }

    @Test
    public void cantHearSongAfterUnfollow() throws IOException {
        Given:
        bobsClient.users.follow(richard);
        bobsClient.users.unfollow(richard);


        When:
        richardsClient.singing.sing(doReMi);

        Then:
        bobsClient.assertNoFeedOrNotificationEntries();
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
        When:
        richardsClient.singing.sing(hiBob);

        Then:
        bobsClient.notifiedOf(richard, hiBob);
    }

    @Test
    public void coverNotifiesOriginalUser() throws IOException {
        Given:
        richardsClient.users.follow(bob);
        bobsClient.singing.sing(doReMi);
        SongId id = richardsClient.singing.listen().feed().get(0).getId();

        When:
        richardsClient.singing.cover(doReMi, id);

        Then:
        bobsClient.notifiedOf(richard, doReMi);
    }

    @Test
    public void cantSingTooLong() throws IOException {
        assertResponseCodeIs(richardsClient.singing.trySing(longSong), 400);
    }

}
