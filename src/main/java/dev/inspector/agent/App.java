package dev.inspector.agent;

import dev.inspector.agent.model.*;

import java.util.Date;

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
        inspector.startTransaction("Test Java segment 1");
        Segment segmentRef =  inspector.addSegment((segment) -> {
            int millisecToWait = 3000;
            long startingTime = new Date().getTime();
            while(new Date().getTime() <= startingTime + millisecToWait){
                System.out.println("Wait...");
            }
            return segment;
        }, "test async", "test label", false);

        //TODO: Check if we can implement an auto flush
        //See the node env
        inspector.flush();
    }
}
