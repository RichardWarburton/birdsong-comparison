package com.insightfullogic.birdsong.spark_in_memory;

import com.insightfullogic.birdsong.BirdsongApplicationRunner;
import spark.BrokenApiWorkaround;

public class SparkApplicationRunner implements BirdsongApplicationRunner {

    private Birdsong application = null;

    @Override
    public synchronized void start() throws Exception {
        if (application != null) {
            throw new IllegalStateException("Application not stopped");
        }
        application = new Birdsong();
    }

    @Override
    public synchronized void stop() throws Exception {
        BrokenApiWorkaround.stop();
        application = null;
    }

    @Override
    public long getStartupPauseInMilliseconds() {
        return 500;
    }

    @Override
    public void reset() {
        application.reset();
    }
}
