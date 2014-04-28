package com.insightfullogic.birdsong;

import org.junit.rules.ExternalResource;

/**
 * .
 */
public class ServiceRule extends ExternalResource {

    private final BirdsongService service;

    public ServiceRule() {
        this.service = new SparkBirdsong();
    }

    protected void before() throws Throwable {
        service.start();
    }

    @Override
    protected void after() {
        try {
            service.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
