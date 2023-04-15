package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import org.json.JSONObject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Date;
import java.util.HashMap;

public class Transaction implements Transportable {
    private String model = "transaction";
    private String type = "request";
    private String name;
    private String hash = System.currentTimeMillis() + "" + (int) (Math.random() * 100);
    private User user;
    private String result = "";
    private long timestamp;
    private Long duration;
    private double memoryPeak;

    private HashMap<String , JSONObject> context = new HashMap();


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

    public void end(){
        this.end(new Date().getTime() - this.timestamp);
    }

    public void end(long duration){
        this.duration = duration;
        this.memoryPeak = this.getMemoryPeak();
    }

    public static double getMemoryPeak() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        return heapMemoryUsage.getUsed();
    }

    public void addContext(String label, JSONObject data){
        this.context.put(label, data);
    }

    public boolean isEnded(){
        return this.duration != null && this.duration > 0;
    }


    public TransactionIdentifier getBasicTransactionInfo(){
        return new TransactionIdentifier(this.hash, this.timestamp, this.name);
    }

    @Override
    public JSONObject toTransport() {
        String hostname = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new JsonBuilder()
            .put("model", this.model)
            .put("hash", this.hash)
            .put("name", this.name)
            .put("type", this.type)
            .put("timestamp", Math.round(this.timestamp / 1000.0))
            .put("end",  Math.round((this.timestamp + this.duration) / 1000.0))
            .put("duration", this.duration)
            .put("result", this.result)
            .put("memory_peak", this.memoryPeak)
            .put("user", this.user)
            .put("host", new JsonBuilder()
                    .put("hostname",hostname)
                    .build())
            .put("context", this.context)
            .build();

    }
}
