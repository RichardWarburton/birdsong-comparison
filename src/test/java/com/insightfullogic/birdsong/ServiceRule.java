package com.insightfullogic.birdsong;

import com.insightfullogic.birdsong.spark_in_memory.ApplicationRunner;
import org.junit.rules.ExternalResource;

/**
 * .
 */
public class ServiceRule extends ExternalResource {

    private final BirdsongService service;

    public ServiceRule() {
        this.service = new ApplicationRunner();
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
