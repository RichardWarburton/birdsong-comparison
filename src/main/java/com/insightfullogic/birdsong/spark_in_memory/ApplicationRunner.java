package com.insightfullogic.birdsong.spark_in_memory;

import com.insightfullogic.birdsong.BirdsongService;
import spark.BrokenApiWorkaround;
import spark.Spark;

public class ApplicationRunner implements BirdsongService {

    private Birdsong application;

    @Override
    public void start() throws Exception {
        application = new Birdsong();
    }

    @Override
    public void stop() throws Exception {
        BrokenApiWorkaround.stop();
    }
}
