package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import org.json.JSONObject;

import java.util.Date;

public class Segment extends Context implements Transportable {

    private String model = "segment";
    private String label;
    private String type;
    private TransactionIdentifier transaction;
    private long start;
    private long timestamp;
    private long duration;


    public Segment(TransactionIdentifier identifier, String type, String label){
        this.transaction = identifier;
        this.type = type;
        this.label = label;
        this.timestamp = Math.round(new Date().getTime() / 1000.0);
        this.start = timestamp - this.transaction.getTimestamp();
    }



    public void end(){
        this.duration = new Date().getTime() - this.timestamp;
    }


    @Override
    public JSONObject toTransport() {
        return new JsonBuilder()
            .put("model", this.model)
            .put("type", this.type)
            .put("label", this.label)
            .put("timestamp", Math.round(this.timestamp / 1000.0))
            .put("start",this.start)
            .put("duration", this.duration)
            .put("context", super.context)
            .put("transaction", new JsonBuilder()
                .put("hash",this.transaction.getHash())
                .build())
            .build();
    }
}

