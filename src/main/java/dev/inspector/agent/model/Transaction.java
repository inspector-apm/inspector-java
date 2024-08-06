package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import org.json.JSONObject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Date;

public class Transaction extends Context implements Transportable {
    private String model = "transaction";
    private String type = "request";
    private String name;
    private String hash = System.currentTimeMillis() + "" + (int) (Math.random() * 100);
    private User user;
    private String result = "";
    private long timestamp;
    private Long duration;
    private double memoryPeak;



    public Transaction(String name) {
        this.name = name;
        this.timestamp = Math.round(new Date().getTime() / 1000.0);
    }
    public void withUser(User user){
        this.user = user;
    }

    public void setResult(String result){
        this.result = result;
    }




    public void end(){
        this.end((new Date().getTime() / 1000) - this.timestamp);
    }

    public void end(long duration){
        this.duration = duration;
        this.memoryPeak = this.getMemoryPeak();
    }

    public static double getMemoryPeak() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        return heapMemoryUsage.getUsed() /1000000;
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
            .put("timestamp", this.timestamp)
            .put("end",  this.timestamp + this.duration)
            .put("duration", this.duration)
            .put("result", this.result)
            .put("memory_peak", this.memoryPeak)
            .put("user", this.user)
            .put("host", new JsonBuilder()
                    .put("hostname",hostname)
                    .build())
            .put("context", super.context)
            .build();

    }
}
