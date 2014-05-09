package com.insightfullogic.birdsong.spark_in_memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.currentTimeMillis;

/**
 * .Service encapsulating song domain logic.
 */
public class SongService {

    private static final Pattern mentions = Pattern.compile("@([^ ]+)");
    private static final int MAX_LENGTH_OF_LYRICS = 140;

    private final Map<SongId, User> originalPublisher;
    private final UserService users;

    public SongService(UserService users) {
        this.users = users;
        originalPublisher = new HashMap<>();
    }

    public boolean validateLyrics(String lyrics) {
        return lyrics.length() > MAX_LENGTH_OF_LYRICS;
    }

    public void findMentions(Song song) {
        final String lyrics = song.getLyrics();
        final Matcher matcher = mentions.matcher(lyrics);
        while (matcher.find()) {
            final String name = matcher.group(1);
            users.lookupByName(name).pushNotification(song);
        }
    }

    public boolean sing(User user, String lyrics, String coverOf) {
        if (validateLyrics(lyrics)) {
            return false;
        }
        SongId id = SongId.next();
        Optional<SongId> coverId = Optional.ofNullable(coverOf).map(SongId::new);
        Song song = new Song(id, user.getUsername(), lyrics, currentTimeMillis(), coverId);
        user.sing(song);
        findMentions(song);
        originalPublisher.put(id, user);

        if (coverId.isPresent()) {
            originalPublisher.get(coverId.get()).pushNotification(song);
        }
        return true;
    }

    public void clear() {
        originalPublisher.clear();
    }
}
