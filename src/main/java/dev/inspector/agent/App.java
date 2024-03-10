package dev.inspector.agent;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.*;
import dev.inspector.agent.utility.JsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> onShutdown()));

        Config config = new Config("69597a87ae16c25741ab1623c1b6be2e6b113ac8", "10000");
        Inspector inspector = new Inspector(config);
        Transaction transaction = inspector.startTransaction("Transaction 2");
        transaction.setResult("SUCCESS");

        transaction.addContext("Context1",  new JsonBuilder().put("contextkey", "contextvalue").build());


        Segment segmentRef = inspector.addSegment((segment) -> {
            waitMillis(1000);
            String ptr = null;
            if(ptr.equals("exception")) System.out.println(1234);
            return segment;
        }, "test async", "test label", false);
        segmentRef.addContext("view1", new JsonBuilder().put("test", "test2").build());


        waitMillis(3000);

        Segment segmentRef2 = inspector.addSegment((segment) -> {
            waitMillis(2000);
            return segment;
        }, "test async2", "test label2", false);
        segmentRef2.addContext("view2", new JsonBuilder().put("tes3", "test4").build());


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
