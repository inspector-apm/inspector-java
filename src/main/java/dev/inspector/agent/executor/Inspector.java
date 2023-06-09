package dev.inspector.agent.executor;
import dev.inspector.agent.error.IError;
import dev.inspector.agent.model.*;
import dev.inspector.agent.transport.Transport;
import dev.inspector.agent.transport.Transportable;

import java.util.ArrayList;
import java.util.List;

public class Inspector {

    private Config conf;
    private Transport transport;
    private Transaction transaction;

    public Inspector(Config conf){
        this.conf = conf;
        transport = new Transport(conf);
    }

    public Transaction startTransaction(String name){
        transaction = new Transaction(name);
        addEntries(transaction);
        return transaction;
    }

    public Segment startSegment(String type, String label){
        if(!isRecording()){
            throw new Error("No active transaction found");
        }
        Segment segment = new Segment(transaction.getBasicTransactionInfo(), type, label);

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

    private void reportException(Throwable e) {
        Segment segment = startSegment("exception", e.toString());

        IError error = new IError(e, transaction.getBasicTransactionInfo());
        addEntries(error);
        segment.end();
    }

}
