package dev.inspector.agent.model;

import org.json.JSONObject;

import java.util.Date;

public class Segment implements Transportable {

    private String model = "Segment";
    private String label;
    private String type;
    private TransactionIdentifier transaction;
    private long start;
    private long timestamp;
    private long duration;


    public Segment(TransactionIdentifier identifier){
        this(identifier, "process", null);
    }

    public Segment(TransactionIdentifier identifier, String type, String label){
        this.transaction = identifier;
        this.type = type;
        this.label = label;
    }

    public void start(){
        this.start(new Date().getTime());
    }

    public void start(long time){
        this.start = time - this.transaction.getTimestamp();
        this.timestamp = time;
    }

    public void end(){
        this.end(new Date().getTime());
    }

    public void end(long duration){
        this.duration = duration;
    }


    @Override
    public JSONObject toTransport() {
        return null;
    }
}

