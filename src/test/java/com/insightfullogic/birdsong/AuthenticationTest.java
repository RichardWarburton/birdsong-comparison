package com.insightfullogic.birdsong;

import com.insightfullogic.birdsong.api.UserApi;
import org.apache.http.HttpResponse;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static com.insightfullogic.birdsong.BirdsongService.ADDRESS;
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
    public static ServiceRule birdsongRule = new ServiceRule();

    private final UserApi auth = new UserApi(ADDRESS);

    @Test
    public void canAuthenticate() throws IOException {
        assertHttpOk(auth.login(UserApi.user, UserApi.pass));
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
        assertHttpOk(auth.login(UserApi.user, UserApi.pass));

        Then:
        assertHttpForbidden(auth.register(UserApi.user, UserApi.pass));
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
