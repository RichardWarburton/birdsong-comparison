package com.insightfullogic.birdsong;

public interface BirdsongApplicationRunner {

    public static final int PORT = 4567;
    public static final String address = "http://localhost:" + PORT + "/";

    public void start() throws Exception;

    public void stop() throws Exception;

    /**
     * Ideally should be 0, but many things suck.
     */
    public long getStartupPauseInMilliseconds();

    public void reset();
}
