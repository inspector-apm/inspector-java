package dev.inspector.agent.transport;

import dev.inspector.agent.model.Config;
import dev.inspector.agent.utility.AsyncHttpPost;
import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.ArrayList;

public class Transport {

    private Config conf;
    private List<Transportable> queue;

    public Transport(Config conf){
        this.conf = conf;
        this.queue = new ArrayList<>();
    }

    public void flush(){

        if(this.queue.size() == 0) return;

        JSONArray items = new JSONArray();
        this.queue.forEach(item->
                items.put(item.toTransport())
        );

        send(items);

    }

    public void addEntry(Transportable item) {
        this.queue.add(item);
    }

    public void send(JSONArray items){
        AsyncHttpPost httpHandler = new AsyncHttpPost();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        String jsonPayload = items.toString();

        CompletableFuture<String> response = httpHandler.asyncHttpPost(this.conf.getUrl(), jsonPayload, executor, this.conf.getIngestionKey(), this.conf.getVersion());

        response.thenAccept(res -> {
            System.out.println(res);
            // Remove items from the queue after successful sending
            this.queue.clear();
        });

        // Handle any exceptions that may occur during the execution
        response.exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        executor.shutdown();
    }


    public int getQueueSize(){
        return this.queue.size();
    }

}
