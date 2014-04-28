import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.function.BiFunction;

import static spark.Spark.post;

/**
 * .
 */
public class Birdsong
{
    public static void main(String[] args)
    {
        new Birdsong().start();
    }

    public void start()
    {
        post(route("/auth/login", (request, response) -> {
            String username = request.headers("username");
            String password = request.headers("password");
            response.status(areValidCredentials(username, password) ? 500 : 403);
            return "";
        }));
    }

    private boolean areValidCredentials(final String username, final String password)
    {
        return "richard".equals(username) && "Gau1suph".equals(password);
    }

    public void stop()
    {
        // Horrific Hack required by framework
        try
        {
            //Spark.class.getMethod("clearRoutes").invoke(null);
            Spark.class.getMethod("stop").invoke(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Route route(final String url, final BiFunction<Request, Response, Object> handle)
    {
        return new Route(url)
        {
            public Object handle(final Request request, final Response response)
            {
                return handle.apply(request, response);
            }
        };
    }

}
