package dev.inspector.agent.model;

import dev.inspector.agent.utility.AsyncHttpPost;
import org.json.JSONArray;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.ArrayList;

public class Transport {

    private Config conf;
    private ArrayList<Transportable> queue;

    public Transport(Config conf){
        this.conf = conf;
        this.queue = new ArrayList<Transportable>();
    }

    public void flush(){
        if(this.queue.size() == 0) return;

        JSONArray items = new JSONArray();
        this.queue.forEach(item->
            items.put(item.toTransport())
        );

        this.send(items);
    }

    public void addEntry(Transportable item){
        this.queue.add(item);
    }

    public void send(JSONArray items){
        AsyncHttpPost httpHandler = new AsyncHttpPost();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        String jsonPayload = items.toString();
        httpHandler.asyncHttpPost(this.conf.getUrl(), jsonPayload, executor, this.conf.getIngestionKey(), this.conf.getVersion());
        executor.shutdown();
    }

    public int getQueueSize(){
        return this.queue.size();
    }

}
