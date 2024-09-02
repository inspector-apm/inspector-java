package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import org.json.JSONObject;

import java.math.BigDecimal;

public class TransactionIdentifier {
    public String hash;
    public BigDecimal timestamp;
    public String name;

    public TransactionIdentifier(String hash, BigDecimal timestamp, String name){
        this.hash = hash;
        this.timestamp = timestamp;
        this.timestamp = timestamp;
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigDecimal getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigDecimal timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject toObject(){
        return new JsonBuilder()
            .put("hash", hash)
            .put("timestamp", timestamp)
            .put("name", name)
            .build();
    }
}
