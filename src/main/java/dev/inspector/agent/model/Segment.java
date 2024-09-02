package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import dev.inspector.agent.utility.TimesUtils;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public class Segment extends Context implements Transportable {

    private String model = "segment";
    private String label;
    private String type;
    private TransactionIdentifier transaction;
    private BigDecimal timestamp;
    private Integer duration;
    private BigDecimal start;


    public Segment(TransactionIdentifier identifier, String type, String label){
        this.transaction = identifier;
        this.type = type;
        this.label = label;
        this.timestamp = TimesUtils.getTimestamp();
    }

    public Segment(TransactionIdentifier identifier, String type) {
        this.transaction = identifier;
        this.type = type;
        this.timestamp = TimesUtils.getTimestamp();
    }

    public Segment start(){
        this.start = TimesUtils.getTimestamp().subtract(this.transaction.getTimestamp());
        return this;
    }


    public void end(){
        BigDecimal end = TimesUtils.getTimestamp();
        this.end(end.subtract(this.timestamp).multiply(BigDecimal.valueOf(1000)));
    }

    public void end(BigDecimal duration){
        this.duration = duration.intValue();
    }


    @Override
    public JSONObject toTransport() {
        return new JsonBuilder()
            .put("model", this.model)
            .put("type", this.type)
            .put("label", this.label)
            .put("timestamp", this.timestamp)
            .put("start",this.start)
            .put("duration", this.duration)
            .put("context", super.context)
            .put("transaction", new JsonBuilder()
                .put("hash",this.transaction.getHash())
                .build())
            .build();
    }

    public Segment setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getLabel() {
        return this.label;
    }
}

