package com.insightfullogic.birdsong.spark_in_memory;

import java.util.HashMap;
import java.util.Map;

/**
 * Service that manages storage and lookup of users.
 */
public class UserService {

    private final Map<String, User> users;

    public UserService() {
        users = new HashMap<>();
    }

    public void addUser(String username, String password) {
        users.put(username, new User(username, password));
    }

    public User lookupByName(String username) {
        return users.get(username);
    }

    public int registerCredentials(String username, String password) {
        if (users.containsKey(username)) {
            return 400;
        }

        addUser(username, password);
        return 200;
    }

    public int areValidCredentials(String username, String password) {
        User user = users.get(username);
        boolean ok = user != null && user.hasPassword(password);
        return ok ? 200 : 403;
    }

    public void clear() {
        users.clear();
    }

}
