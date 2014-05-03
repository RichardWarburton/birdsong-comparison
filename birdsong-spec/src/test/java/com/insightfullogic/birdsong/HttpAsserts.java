package com.insightfullogic.birdsong;

import org.apache.http.HttpResponse;

import static org.junit.Assert.assertEquals;

public class HttpAsserts {

    public static void assertHttpOk(HttpResponse response) {
        assertResponseCodeIs(response, 200);
    }

    public static void assertHttpForbidden(HttpResponse response) {
        assertResponseCodeIs(response, 403);
    }

    public static void assertResponseCodeIs(HttpResponse response, int code) {
        assertEquals(code, response.getStatusLine().getStatusCode());
    }

}
