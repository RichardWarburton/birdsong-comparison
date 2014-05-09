package com.insightfullogic.birdsong.spark_in_memory;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * .
 */
public class JsonInformation {

    private static final ObjectMapper json = new ObjectMapper();

    private final User user;
    private final OutputStream out;

    public JsonInformation(User user, OutputStream out) throws IOException {
        this.user = user;
        this.out = out;
    }

    public void generate() throws IOException {
        Map<String, Object> value = new HashMap<>();
        value.put("followers", toNames(user.getFollowers()));
        value.put("following", toNames(user.getFollowing()));
        json.writeValue(out, value);
    }

    private List<String> toNames(Set<User> users) {
        return users.stream()
                    .map(User::getUsername)
                    .collect(toList());
    }

}
