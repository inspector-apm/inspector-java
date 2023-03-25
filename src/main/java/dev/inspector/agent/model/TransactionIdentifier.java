package dev.inspector.agent.model;

public class TransactionIdentifier {
    public String hash;
    public long timestamp;
    //TODO: Add name parameter

    public TransactionIdentifier(String hash, long timestamp){
        this.hash = hash;
        this.timestamp = timestamp;
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
}
