package com.insightfullogic.birdsong;

import org.junit.rules.ExternalResource;

/**
 * .
 */
public class ServiceRule extends ExternalResource {

    private static BirdsongApplicationRunner service;

    public static BirdsongApplicationRunner getService() {
        return service;
    }

    public static void setService(BirdsongApplicationRunner service) {
        ServiceRule.service = service;
    }

    public ServiceRule() {
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
