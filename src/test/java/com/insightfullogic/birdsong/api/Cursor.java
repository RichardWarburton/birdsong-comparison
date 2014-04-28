package com.insightfullogic.birdsong.api;

import java.util.Objects;

public final class Cursor implements Comparable<Cursor> {

    private final long value;

    Cursor(final String value) {
        this.value = Long.parseLong(value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Cursor)) {
            return false;
        }
        return Objects.equals(value, ((Cursor) obj).value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Cursor other) {
        return Long.compare(value, other.value);
    }
}
