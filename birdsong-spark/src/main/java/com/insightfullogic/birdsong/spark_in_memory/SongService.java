/*
 * Copyright 2014 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.insightfullogic.birdsong.spark_in_memory;

import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
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
