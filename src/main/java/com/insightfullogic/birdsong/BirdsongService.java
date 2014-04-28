package com.insightfullogic.birdsong;

public interface BirdsongService {

    public static final int PORT = 4567;
    public static final String ADDRESS = "http://localhost:" + PORT + "/";

    public void start() throws Exception;

    public void stop() throws Exception;

}
