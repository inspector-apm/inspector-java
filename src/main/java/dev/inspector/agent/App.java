package dev.inspector.agent;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.*;
import dev.inspector.agent.utility.JsonBuilder;
import java.util.Date;

public class App {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> onShutdown()));
        Config config = new Config("81e6d4df93e1bfad8e9f3c062022e3a0d8a77dce");
        Inspector inspector = new Inspector(config);
        Transaction transaction = inspector.startTransaction("Test Java segment 1");
        transaction.setResult("SUCCESS");

        transaction.addContext("Context1",  new JsonBuilder().put("test_transaction", "test2").build());


        Segment segmentRef = inspector.addSegment((segment) -> {
            waitMillis(1000);
            String ptr = null;
            if(ptr.equals("excetion")) System.out.println(1234);
            return segment;
        }, "test async", "test label", false);
        segmentRef.addContext("view1", new JsonBuilder().put("test", "test2").build());


        waitMillis(3000);

        Segment segmentRef2 = inspector.addSegment((segment) -> {
            waitMillis(2000);
            return segment;
        }, "test async2", "test label2", false);


        // See the node env
        inspector.flush();

    }

    private static void onShutdown() {
        System.out.println("qui c'è il flush finale");
    }


    public static void waitMillis(long x){
        System.out.println("Waiting for " + x);
        long now = new Date().getTime();
        while (new Date().getTime() < now + x) {}
    }
}
