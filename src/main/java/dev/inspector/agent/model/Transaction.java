package dev.inspector.agent.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.inspector.agent.utility.JsonBuilder;
import dev.inspector.agent.utility.TimesUtils;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

public class Transaction extends Context implements Transportable {

    private static Logger LOGGER = LoggerFactory.getLogger(Transaction.class);
    private String model = "transaction";
    private TransactionType type;
    private String name;
    private String hash = System.currentTimeMillis() + "" + (int) (Math.random() * 100);
    private User user;
    private String result = "";
    private BigDecimal timestamp;
    private Long durationInMillis;
    private Long memoryPeak;

    public Transaction(String name) {
        this.name = name;
        this.timestamp = TimesUtils.getTimestamp();
        type = TransactionType.GENERAL;
    }
    public Transaction withUser(User user){
        this.user = user;

        return this;
    }

    public Transaction withType(TransactionType type) {
        this.type = type;

        return this;
    }

    public Transaction setResult(String result){
        this.result = result;

        return this;
    }

    public Transaction end(){
        BigDecimal end = TimesUtils.getTimestamp();
        this.end(end.subtract(this.timestamp).multiply(BigDecimal.valueOf(1000)));

        return this;
    }

    public Transaction end(BigDecimal duration){
        this.durationInMillis = duration.longValue();
        this.memoryPeak = this.getMemoryPeak();

        return this;
    }

    public static long getMemoryPeak() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        return heapMemoryUsage.getUsed() /1000000;
    }

    public boolean isEnded(){
        return this.durationInMillis != null && this.durationInMillis > 0;
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
            LOGGER.error("Error retrieving hostname when converting transaction to json",e);
        }

        return new JsonBuilder()
            .put("model", this.model)
            .put("hash", this.hash)
            .put("name", this.name)
            .put("type", this.type.getType())
            .put("timestamp", this.timestamp)
            .put("duration", this.durationInMillis)
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
