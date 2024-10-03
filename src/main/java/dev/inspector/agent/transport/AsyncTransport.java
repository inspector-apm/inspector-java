package dev.inspector.agent.transport;

import dev.inspector.agent.model.Config;
import dev.inspector.agent.model.Transportable;
import dev.inspector.agent.utility.AsyncHttpPost;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public class AsyncTransport implements Transport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTransport.class);

    private Config conf;
    private Queue<Transportable> queue;

    public AsyncTransport(Config conf) {
        this.conf = conf;
        this.queue = new LinkedList<>();
    }

    public void flush() {

        LOGGER.debug("Thread {}: Flushing the transport queue. Queue size: {}", Thread.currentThread().getName(), getQueueSize());
        if (this.queue.isEmpty()) {
            LOGGER.debug("Thread {}: Queue is empty. Aborting the flush process", Thread.currentThread().getName());
            return;
        }
        try {
            JSONArray items = new JSONArray();
            this.queue.forEach(item ->
                    items.put(item.toTransport())
            );

            this.queue.clear();
            send(items);
        } catch (Exception e) {
            LOGGER.error("Thread {}: Exception occurred during flushing the transport queue", Thread.currentThread().getName(), e);
        }

    }

    public void addEntry(Transportable item) {
            this.queue.add(item);
    }

    public void send(JSONArray items) {
        AsyncHttpPost httpHandler = new AsyncHttpPost();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        String jsonPayload = items.toString();

        CompletableFuture<String> response = httpHandler.asyncHttpPost(this.conf.getUrl(), jsonPayload, executor, this.conf.getIngestionKey(), this.conf.getVersion());

        response.thenAccept(res -> {
                    LOGGER.debug("Thread {}: Transport queue flushed successfully!", Thread.currentThread().getName());
                })
                .exceptionally(
                        ex -> {
                            LOGGER.error("Thread {}: Exception occurred during flushing the transport queue", Thread.currentThread().getName(), ex);
                            return null;
                        }
                );

        executor.shutdown();
    }


    public Integer getQueueSize() {
        return this.queue.size();
    }

}
