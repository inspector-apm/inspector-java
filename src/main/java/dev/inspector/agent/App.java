package dev.inspector.agent;

import dev.inspector.agent.model.*;

import java.util.concurrent.CompletableFuture;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Config config = new Config("81e6d4df93e1bfad8e9f3c062022e3a0d8a77dce");
        Inspector inspector = new Inspector(config);
        System.out.println("pippo");
        inspector.startTransaction("Test Java lol");
//        Segment futureResult =  inspector.addSegment((segment) -> {
//            System.out.println("Segment...");
//            return segment;
//        }, "test async", "test label", false);

        //TODO: Check if we can implement an auto flush
        //See the node env
        inspector.flush();
        System.out.println("pippo");
    }
}
