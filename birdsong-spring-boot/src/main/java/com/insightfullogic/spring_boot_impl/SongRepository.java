package com.insightfullogic.spring_boot_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.SQLException;

import static java.lang.Long.parseLong;

/**
 * .
 */
@Repository
public class SongRepository {

    private static final int MAX_LENGTH_OF_LYRICS = 140;

    private final EntityManager entities;
    private final UserRepository users;
    private final DataSource source;

    @Autowired
    public SongRepository(EntityManager entities, UserRepository users, DataSource source) {
        this.entities = entities;
        this.users = users;
        this.source = source;
    }

    @Transactional
    public void sing(String lyrics, String loggedInUser, String originalId) {
        validate(lyrics);

        User singer = users.lookupByName(loggedInUser);
        Song original = song(originalId);
        Song song = new Song(lyrics, singer, original);
        entities.persist(song);

        notifyCover(original, song);
        notifyFollowers(singer, song);
        notifyMentions(song);
    }

    private void notifyCover(Song original, Song song) {
        if (original != null) {
            original.onCover(song);
        }
    }

    private Song song(String originalId) {
        if (originalId == null) {
            return null;
        }

        return entities.find(Song.class, parseLong(originalId));
    }

    private void notifyFollowers(User singer, Song song) {
        singer.getFollowers().forEach(user -> {
            user.newFeedEntry(song);
            entities.persist(user);
        });
    }

    private void validate(String lyrics) {
        if (lyrics.length() > MAX_LENGTH_OF_LYRICS) {
            throw new InvalidSongException();
        }
    }

    private void notifyMentions(Song song) {
        song.findMentions()
            .stream()
            .map(users::lookupByName)
            .forEach(user -> {
                user.newMention(song);
                entities.persist(user);
            });
    }

    public SongBook listen(String username, String since) {
        long from = Long.parseLong(since);
        long to = System.currentTimeMillis();
        User user = users.lookupByName(username);
        return new SongBook(to, user.feedSince(from), user.mentionsSince(from));
    }

    @Transactional
    public void clear() {
        // horrible hack
        try {
            source.getConnection().createStatement().execute("SET REFERENTIAL_INTEGRITY FALSE;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        entities.createQuery("DELETE FROM Song s").executeUpdate();
    }
}
