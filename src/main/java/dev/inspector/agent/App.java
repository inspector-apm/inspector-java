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

        Config config = new Config("7fb973a3a4a6325a78c07b70a14e72bc71cf6dd9");
        Inspector inspector = new Inspector(config);

        inspector.startTransaction("Test Java lol");
        CompletableFuture<Segment> futureResult =  inspector.addSegment(segment -> {
            System.out.println("Segment...");
            return segment;
        }, "test async", "test label");

        futureResult.thenAccept(result -> {
            System.out.println("Result: " + result);
        });

        //TODO: Check if we can implement an auto flush
        //See the node env
        inspector.flush();
    }
}
