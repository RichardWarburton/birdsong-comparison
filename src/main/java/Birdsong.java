/*
 * Copyright 2014 Richard Warburton
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

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Objects;
import java.util.function.BiFunction;

import static spark.Spark.*;

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
