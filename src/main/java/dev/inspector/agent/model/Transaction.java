package dev.inspector.agent.model;

import org.json.JSONObject;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Date;
import java.util.HashMap;

public class Transaction implements Transportable {
    private String model = "Transaction";
    private String type = "request";
    private String name;
    private String hash = System.currentTimeMillis() + "" + (int) (Math.random() * 100);
    private String http; // TBD Object
    private User user;
    private String result = "";
    private String host; // TBD Object
    private long timestamp;
    private Long duration;
    private double memoryPeak;

    private HashMap<String , Object> context = new HashMap();


    public Transaction(String name) {
        this.name = name;
    }
    public void withUser(User user){
        this.user = user;
    }

    public void setResult(String result){
        this.result = result;
    }

    public void start(){
        this.start(new Date().getTime());
    }

    public void start(long date){
        this.timestamp = date;
    }

    public void stop(){
        //TODO: Add check fo started request
        this.stop(new Date().getTime() - this.timestamp);
    }

    public void stop(long duration){
        //TODO: Validate negative duration
        this.duration = duration;
        this.memoryPeak = this.getMemoryPeak();
    }

    public static double getMemoryPeak() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        return heapMemoryUsage.getUsed();
    }

    public void addContext(String label, Object data){
        this.context.put(label, data);
    }

    public boolean isEnded(){
        return this.duration != null && this.duration > 0;
    }


    @Override
    public JSONObject toTransport() {
        return null;
    }
}
