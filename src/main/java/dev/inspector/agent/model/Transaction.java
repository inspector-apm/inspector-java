package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import dev.inspector.agent.utility.TimesUtils;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.time.Instant;
import java.util.Date;

public class Transaction extends Context implements Transportable {
    private String model = "transaction";
    private String type = "request";
    private String name;
    private String hash = System.currentTimeMillis() + "" + (int) (Math.random() * 100);
    private User user;
    private String result = "";
    private BigDecimal timestamp;
    private Integer duration;
    private Long memoryPeak;



    public Transaction(String name) {
        this.name = name;
        this.timestamp = TimesUtils.getTimestamp();
    }
    public void withUser(User user){
        this.user = user;
    }

    public void setResult(String result){
        this.result = result;
    }




    public void end(){
        BigDecimal end = TimesUtils.getTimestamp();
        this.end(end.subtract(this.timestamp).multiply(BigDecimal.valueOf(1000)));
    }

    public void end(BigDecimal duration){
        this.duration = duration.intValue();
        this.memoryPeak = this.getMemoryPeak();
    }

    public static long getMemoryPeak() {
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
