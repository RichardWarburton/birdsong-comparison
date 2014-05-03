package com.insightfullogic.birdsong;

import org.apache.http.HttpResponse;

import static org.junit.Assert.assertEquals;

public class HttpAsserts {

    public static void assertHttpOk(HttpResponse response) {
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    public static void assertHttpForbidden(HttpResponse response) {
        assertEquals(403, response.getStatusLine().getStatusCode());
    }

}
