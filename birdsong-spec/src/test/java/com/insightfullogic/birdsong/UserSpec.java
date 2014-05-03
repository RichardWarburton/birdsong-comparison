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

    private final UserApi auth = new UserApi(address);

    @Test
    public void canAuthenticate() throws IOException {
        auth.login(Users.richard, Users.richardsPass);
        auth.login(Users.bob, Users.bobsPass);
    }

    @Test
    public void canRegisterNewUsernames() throws IOException {
        Given:
        assertHttpForbidden(auth.tryLogin(userToRegister, passToRegister));

        When:
        assertHttpOk(auth.register(userToRegister, passToRegister));

        Then:
        auth.login(userToRegister, passToRegister);
    }

    @Test
    public void cantRegisterDuplicateUsernames() throws IOException {
        Given:
        auth.login(Users.richard, Users.richardsPass);

        Then:
        assertHttpForbidden(auth.register(Users.richard, Users.richardsPass));
    }

    @Test
    public void canFollowAnotherUser() throws IOException {
        Given:
        auth.login(Users.richard, Users.richardsPass);

        Then:
        auth.follow(Users.bob);
    }

    @Test
    public void invalidCredentialsAreRejected() throws IOException {
        assertHttpForbidden(auth.tryLogin(unknownUser, unknownPass));
    }

}
