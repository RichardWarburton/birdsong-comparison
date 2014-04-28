import org.junit.rules.ExternalResource;

/**
 * .
 */
public class BirdsongRule extends ExternalResource
{
    private Birdsong server;

    protected void before() throws Throwable
    {
        server = new Birdsong();
        server.start();
    }

}
