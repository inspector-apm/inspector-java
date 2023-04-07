package dev.inspector.agent;

import dev.inspector.agent.model.*;
import dev.inspector.agent.utility.JsonBuilder;
import java.util.Date;


public class App 
{
    public static void main( String[] args )
    {

        Config config = new Config("81e6d4df93e1bfad8e9f3c062022e3a0d8a77dce");
        Inspector inspector = new Inspector(config);
        Transaction transaction = inspector.startTransaction("Test Java segment 1");
        transaction.setResult("SUCCESS");

        Segment segmentRef =  inspector.addSegment((segment) -> {

            long now = new Date().getTime();
            while(new Date().getTime() < now + 1000 ){
                System.out.println("Wait");
            }

            String ptr = null;
            boolean x =  ptr.equals("gfg");

            return segment;
        }, "test async", "test label", false);

        segmentRef.addContext("view1", new JsonBuilder().put("test","test2").build());

        //TODO: Check if we can implement an auto flush
        //See the node env
        inspector.flush();

    }
}
