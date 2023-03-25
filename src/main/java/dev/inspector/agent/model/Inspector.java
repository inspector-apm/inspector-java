package dev.inspector.agent.model;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.ArrayList;
public class Inspector {

    private Config conf;
    private Transport transport;
    private Transaction transaction;

    public Inspector(Config conf){
        this.conf = conf;
        this.transport = new Transport(conf);
    }

    public Transaction startTransaction(String name){
        this.transaction = new Transaction(name);
        this.transaction.start();

        this.addEntries(this.transaction);
        return this.transaction;
    }

    public Segment startSegment(String type, String label){
        if(!this.isRecording()){
            throw new Error("No active transaction found");
        }

        Segment segment = new Segment(this.transaction.getBasicTransactionInfo(),type, label);
        segment.start();

        this.addEntries(segment);

        return segment;
    }

    public Transaction transaction(){
        return this.transaction;
    }

    public boolean isRecording(){
        return this.transaction != null;
    }


    public void addEntries(Transportable data){
        ArrayList<Transportable> dataArray = new ArrayList<Transportable>();
        dataArray.add(data);
        this.addEntries(dataArray);
    }
    public void addEntries(ArrayList<Transportable> data){
        data.forEach(entry -> {
            if(this.canAddEntry()){
                this.transport.addEntry(entry);
            }
        });
    }

    public void flush(){

        if(!this.conf.isEnabled() || !this.isRecording()) return;

        if(!this.transaction.isEnded()){
            this.transaction.end();
        }
        this.transport.flush();
        this.transaction = null;
    }

    private boolean canAddEntry(){
        return this.transport.getQueueSize() < this.conf.getMaxEntries();
    }

    public <T> CompletableFuture<T> addSegment(Supplier<CompletableFuture<T>> fn, String type, String label, boolean throwE) {
        Segment segment = startSegment(type, label);
        try {
            return fn.get()
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            reportException(exception);
                            if (throwE) {
                                throw new RuntimeException(exception);
                            }
                        }
                        segment.end();
                    });
        } catch (Exception e) {
            segment.end();
            reportException(e);
            if (throwE) {
                throw new RuntimeException(e);
            } else {
                return CompletableFuture.completedFuture(null);
            }
        }
    }

    private void reportException(Throwable e) {
        System.out.println("Error...");
    }

}
