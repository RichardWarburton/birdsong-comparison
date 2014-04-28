import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * .
 */
public class AuthenticationApi
{

    private static final String PREFIX = "http://localhost:4567/auth/";
    private static final String AUTH_URL = PREFIX + "login";
    private static final String REGISTER_URL = PREFIX + "register";

    public HttpResponse login(final String username, final String password) throws IOException
    {
        return Request.Post(AUTH_URL)
                      .addHeader("username", username)
                      .addHeader("password", password)
                      .execute().returnResponse();
    }

    public HttpResponse register(final String username, final String password) throws IOException
    {
        return Request.Post(REGISTER_URL)
                .addHeader("username", username)
                .addHeader("password", password)
                .execute().returnResponse();
    }

}
