package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import org.json.JSONObject;

public class TransactionIdentifier {
    public String hash;
    public long timestamp;
    public String name;

    public TransactionIdentifier(String hash, long timestamp, String name){
        this.hash = hash;
        this.timestamp = timestamp;
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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
