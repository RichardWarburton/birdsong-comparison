package spark;

public class BrokenApiWorkaround {

    public static void stop() {
        Spark.clearRoutes();
        Spark.stop();
    }
}
