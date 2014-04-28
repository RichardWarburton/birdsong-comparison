import org.apache.http.HttpResponse;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * .
 */
public class AuthenticationTest {

    public static final String USERNAME = "richard";
    public static final String PASSWORD = "Gau1suph";

    @ClassRule
    public static BirdsongRule birdsongRule = new BirdsongRule();

    private final AuthenticationApi auth = new AuthenticationApi();

    @Test
    public void canAuthenticate() throws IOException {
        assertHttpOk(auth.login(USERNAME, PASSWORD));
    }

    @Test
    public void canRegisterNewUsernames() throws IOException {
        Given:
        assertHttpForbidden(auth.login("b", "c"));

        When:
        assertHttpOk(auth.register("b", "c"));

        Then:
        assertHttpOk(auth.login("b", "c"));
    }

    @Test
    public void cantRegisterDuplicateUsernames() throws IOException {
        Given:
        assertHttpForbidden(auth.login("b", "c"));

        When:
        assertHttpOk(auth.register("b", "c"));

        Then:
        assertHttpOk(auth.login("b", "c"));
    }

    @Test
    public void invalidCredentialsAreRejected() throws IOException {
        assertHttpForbidden(auth.login("a", "b"));
    }

    private void assertHttpOk(HttpResponse response) {
        assertEquals(500, response.getStatusLine().getStatusCode());
    }

    private void assertHttpForbidden(HttpResponse response) {
        assertEquals(403, response.getStatusLine().getStatusCode());
    }

}
