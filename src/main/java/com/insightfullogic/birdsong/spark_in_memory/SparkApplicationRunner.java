package com.insightfullogic.birdsong.spark_in_memory;

import com.insightfullogic.birdsong.BirdsongService;
import spark.BrokenApiWorkaround;

import static java.util.Objects.requireNonNull;

public class SparkApplicationRunner implements BirdsongService {

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
        requireNonNull(application, "Application not started");
        BrokenApiWorkaround.stop();
        application = null;
    }
}
