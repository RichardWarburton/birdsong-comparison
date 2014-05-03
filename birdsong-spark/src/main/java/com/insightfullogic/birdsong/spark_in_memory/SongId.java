package com.insightfullogic.birdsong.spark_in_memory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Long.parseLong;

public final class SongId {

    private static final AtomicLong counter = new AtomicLong();

    public static SongId next() {
        return new SongId(counter.getAndIncrement());
    }

    private final long value;

    public SongId(final String value) {
        this(parseLong(value));
    }

    private SongId(final long value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SongId)) {
            return false;
        }
        return Objects.equals(value, ((SongId) obj).value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
