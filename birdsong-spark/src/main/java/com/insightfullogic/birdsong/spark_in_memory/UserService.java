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
