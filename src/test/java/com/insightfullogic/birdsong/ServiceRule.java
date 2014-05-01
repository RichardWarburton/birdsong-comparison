package com.insightfullogic.birdsong;

import com.insightfullogic.birdsong.spark_in_memory.SparkApplicationRunner;
import org.junit.rules.ExternalResource;

/**
 * .
 */
public class ServiceRule extends ExternalResource {

    private final BirdsongApplicationRunner service;

    public ServiceRule() {
        this.service = new SparkApplicationRunner();
    }

    protected void before() throws Throwable {
        service.start();
        pauseToAllowStartup();
    }

    // This is horrific
    private void pauseToAllowStartup() {
        try {
            Thread.sleep(service.getStartupPauseInMilliseconds());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
