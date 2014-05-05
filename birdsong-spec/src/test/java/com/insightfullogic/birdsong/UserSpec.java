package com.insightfullogic.birdsong;

import com.insightfullogic.birdsong.api.UserApi;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.insightfullogic.birdsong.BirdsongApplicationRunner.address;
import static com.insightfullogic.birdsong.HttpAsserts.assertHttpForbidden;
import static com.insightfullogic.birdsong.HttpAsserts.assertHttpOk;

/**
 * .
 */
public class UserSpec {

    public static final String userToRegister = "b";
    public static final String passToRegister = "c";

    public static final String unknownUser = "a";
    public static final String unknownPass = "b";

    @ClassRule
    public static ServiceRule birdsongRule = new ServiceRule();

    @Rule
    public ResetRule reset = new ResetRule();

    private final UserApi richard = new UserApi(address);

    @Test
    public void canAuthenticate() throws IOException {
        richard.login(Users.richard, Users.richardsPass);
        richard.login(Users.bob, Users.bobsPass);
    }

    @Test
    public void canRegisterNewUsernames() throws IOException {
        Given:
        assertHttpForbidden(richard.tryLogin(userToRegister, passToRegister));

        When:
        assertHttpOk(richard.register(userToRegister, passToRegister));

        Then:
        richard.login(userToRegister, passToRegister);
    }

    @Test
    public void cantRegisterDuplicateUsernames() throws IOException {
        When:
        richard.login(Users.richard, Users.richardsPass);

        Then:
        assertHttpForbidden(richard.register(Users.richard, Users.richardsPass));
    }

    @Test
    public void canFollowAnotherUser() throws IOException {
        When:
        richard.login(Users.richard, Users.richardsPass);

        Then:
        richard.follow(Users.bob);
        richard.assertFollowing(Users.richard, Users.bob);
        richard.assertFollows(Users.bob, Users.richard);
    }

    @Test
    public void invalidCredentialsAreRejected() throws IOException {
        assertHttpForbidden(richard.tryLogin(unknownUser, unknownPass));
    }

}
