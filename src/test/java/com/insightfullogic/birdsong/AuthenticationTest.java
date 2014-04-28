package com.insightfullogic.birdsong;

import org.apache.http.HttpResponse;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * .
 */
public class AuthenticationTest {

    public static final String userToRegister = "b";
    public static final String passToRegister = "c";

    public static final String unknownUser = "a";
    public static final String unknownPass = "b";

    @ClassRule
    public static BirdsongRule birdsongRule = new BirdsongRule();

    private final AuthenticationApi auth = new AuthenticationApi();

    @Test
    public void canAuthenticate() throws IOException {
        assertHttpOk(auth.login(AuthenticationApi.user, AuthenticationApi.pass));
    }

    @Test
    public void canRegisterNewUsernames() throws IOException {
        Given:
        assertHttpForbidden(auth.login(userToRegister, passToRegister));

        When:
        assertHttpOk(auth.register(userToRegister, passToRegister));

        Then:
        assertHttpOk(auth.login(userToRegister, passToRegister));
    }

    @Test
    public void cantRegisterDuplicateUsernames() throws IOException {
        Given:
        assertHttpOk(auth.login(AuthenticationApi.user, AuthenticationApi.pass));

        Then:
        assertHttpForbidden(auth.register(AuthenticationApi.user, AuthenticationApi.pass));
    }

    @Test
    public void invalidCredentialsAreRejected() throws IOException {
        assertHttpForbidden(auth.login(unknownUser, unknownPass));
    }

    private void assertHttpOk(HttpResponse response) {
        assertEquals(500, response.getStatusLine().getStatusCode());
    }

    private void assertHttpForbidden(HttpResponse response) {
        assertEquals(403, response.getStatusLine().getStatusCode());
    }

}
