package com.insightfullogic.birdsong.api;

import java.util.Objects;

public final class SongId {

    private final long value;

    SongId(final String value) {
        this.value = Long.parseLong(value);
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
