package dev.inspector.agent.executor;
import dev.inspector.agent.model.Error;
import dev.inspector.agent.model.*;
import dev.inspector.agent.transport.AsyncTransport;
import dev.inspector.agent.model.Transportable;
import dev.inspector.agent.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Inspector {

    private static Logger LOGGER = LoggerFactory.getLogger(Inspector.class);

    private Config inspectorConfig;
    private Transport dataTransportService;
    private ThreadLocal<Transaction> transactionThreadSafeWrapper;
    private ScheduledExecutorService scheduler;

    public Inspector(Config conf){
        this.inspectorConfig = conf;
        dataTransportService = new AsyncTransport(inspectorConfig);
        transactionThreadSafeWrapper = new ThreadLocal<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public Transaction startTransaction(String name){
        transactionThreadSafeWrapper.set(new Transaction(name));

        Transaction transaction = transactionThreadSafeWrapper.get();
        LOGGER.debug("Thread {}: Starting monitoring transaction {}", Thread.currentThread().getName(), transaction.getBasicTransactionInfo().getHash());

        addEntries(transaction);

        return transaction;
    }

    public Segment startSegment(String type, String label){
        if(!isRecording()){
            throw new IllegalStateException("No active transaction found");
        }

        Transaction currentMonitoringTransaction = transactionThreadSafeWrapper.get();
        LOGGER.debug("Thread {}: Starting segment of type {} and label {} for transaction {}",
                Thread.currentThread().getName(),
                type,
                label,
                currentMonitoringTransaction.getBasicTransactionInfo().getHash()
        );
        Segment segment = new Segment(currentMonitoringTransaction.getBasicTransactionInfo(), type, label);
        segment.start();
        addEntries(segment);
        return segment;
    }


    public Segment startSegment(String type){
        if(!isRecording()){
            throw new IllegalStateException("No active transaction found");
        }

        Transaction currentMonitoringTransaction = transactionThreadSafeWrapper.get();
        LOGGER.debug("Thread {}: Starting segment of type {} for transaction {}",
                Thread.currentThread().getName(),
                type,
                currentMonitoringTransaction.getBasicTransactionInfo().getHash()
        );
        Segment segment = new Segment(currentMonitoringTransaction.getBasicTransactionInfo(), type);
        segment.start();
        addEntries(segment);
        return segment;
    }

    public boolean isRecording(){
        return transactionThreadSafeWrapper.get() != null;
    }


    public void addEntries(Transportable data) {
        if (dataTransportService.getQueueSize() >= inspectorConfig.getMaxEntries()) {
            dataTransportService.flush();
        }
        dataTransportService.addEntry(data);
    }

    public void flush() {
        if(!inspectorConfig.isEnabled() || !isRecording()) {
            LOGGER.debug("Thread {}: No active transactions found to flush!", Thread.currentThread().getName());
            return;
        }

        Transaction currentMonitoringTransaction = transactionThreadSafeWrapper.get();

        if(!currentMonitoringTransaction.isEnded()){
            currentMonitoringTransaction.end();
        }

        LOGGER.debug("Thread {}: Flushing data to monitoring server", Thread.currentThread().getName());
        dataTransportService.flush();
        transactionThreadSafeWrapper.remove();
    }


    public Segment addSegment(ElaborateSegment fn, String type, String label, boolean throwE) {
        Segment segment = startSegment(type, label);
        segment.start();
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

    public void reportException(Throwable e) {
        if(!hasTransaction()) {
            startTransaction(e.getClass().getSimpleName());
        }
        Segment segment = startSegment("exception", e.getMessage());
        Transaction currentMonitoringTransaction = getTransaction();

        Error error = new Error(e, currentMonitoringTransaction.getBasicTransactionInfo());
        addEntries(error);
        segment.end();
    }

    public Transaction getTransaction() {
        return transactionThreadSafeWrapper.get();
    }

    public Boolean hasTransaction() {
       return (this.getTransaction() != null);
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
