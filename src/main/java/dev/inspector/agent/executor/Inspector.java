package dev.inspector.agent.executor;
import dev.inspector.agent.model.Error;
import dev.inspector.agent.model.*;
import dev.inspector.agent.transport.AsyncTransport;
import dev.inspector.agent.model.Transportable;
import dev.inspector.agent.transport.Transport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Inspector {

    private Config conf;
    private Transport transport;
    private Transaction transaction;
    private ScheduledExecutorService scheduler;

    public Inspector(Config conf){
        this.conf = conf;
        transport = new AsyncTransport(conf);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public Transaction startTransaction(String name){
        transaction = new Transaction(name);
        addEntries(transaction);
        return transaction;
    }

    public Segment startSegment(String type, String label){
        if(!isRecording()){
            throw new java.lang.Error("No active transaction found");
        }
        Segment segment = new Segment(transaction.getBasicTransactionInfo(), type, label);
        addEntries(segment);
        return segment;
    }


    public Segment startSegment(String type){
        if(!isRecording()){
            throw new java.lang.Error("No active transaction found");
        }
        Segment segment = new Segment(transaction.getBasicTransactionInfo(), type);
        addEntries(segment);
        return segment;
    }

    public boolean isRecording(){
        return transaction != null;
    }


    public void addEntries(Transportable data) {
        while(transport.getQueueSize() >= conf.getMaxEntries()){
            transport.flush();
        }
        transport.addEntry(data);
    }



    public void flush(){
        if(!conf.isEnabled() || !isRecording()) return;

        if(!transaction.isEnded()){
            transaction.end();
        }
        transport.flush();
        transaction = null;
    }



    public Segment addSegment(ElaborateSegment fn, String type, String label, boolean throwE) {
        Segment segment = startSegment(type, label);
        try {
            //TODO: anonymous function da implementare e cancellare ElaborateSegment
            return fn.execute(segment);
        } catch (Exception e) {
            reportException(e);
            if(throwE){
                throw e;
            }
        } finally {
            segment.end();
            addEntries(segment);
            return segment;
        }
    }

    public void reportException(Throwable e) {
        if(!hasTransaction()) {
            startTransaction(e.getClass().getSimpleName());
        }
        Segment segment = startSegment("exception", e.getMessage());

        Error error = new Error(e, transaction.getBasicTransactionInfo());
        addEntries(error);
        segment.end();
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Boolean hasTransaction() {
       return (null != this.transaction);
    }

    public void shutdown() {
        flush();
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
    }

}
