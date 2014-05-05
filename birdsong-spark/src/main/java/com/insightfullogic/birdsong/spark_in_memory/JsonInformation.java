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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

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
