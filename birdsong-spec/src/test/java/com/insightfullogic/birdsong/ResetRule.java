package com.insightfullogic.birdsong;

import org.junit.rules.ExternalResource;

/**
 * .
 */
public class ResetRule extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        ServiceRule.getService().reset();
    }

}
