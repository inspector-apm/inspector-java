package dev.inspector.agent.model;

import java.util.Date;

public class PerformanceModel {

    private long start;
    private long timestamp;
    private long duration;

    public PerformanceModel(){
    }

    public void start(){
        this.start(new Date().getTime());
    }

    public void start(long time){
        this.timestamp = time;
    }

    public void end(){
        this.end(new Date().getTime() - this.timestamp);
    }

    public void end(long duration){
        this.duration = duration;
    }

}
