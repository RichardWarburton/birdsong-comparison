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

import org.apache.http.HttpResponse;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * .
 */
public class AuthenticationTest
{

    @ClassRule
    public static BirdsongRule birdsongRule = new BirdsongRule();

    private final AuthenticationApi auth = new AuthenticationApi();

    @Test
    public void canAuthenticate() throws IOException
    {
        HttpResponse response = auth.login("richard", "Gau1suph");
        assertEquals(500, response.getStatusLine().getStatusCode());
    }

    @Test
    public void invalidCredentialsAreRejected() throws IOException
    {
        HttpResponse response = auth.login("a", "b");
        assertEquals(403, response.getStatusLine().getStatusCode());
    }

}
