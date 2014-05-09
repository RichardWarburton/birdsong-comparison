package com.insightfullogic.birdsong.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Information {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Information parse(InputStream stream) throws IOException {
        return mapper.readValue(stream, Information.class);
    }

    private List<String> followers;
    private List<String> following;

    public Information() {

    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }
}
