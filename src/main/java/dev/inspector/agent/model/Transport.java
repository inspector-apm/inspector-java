package dev.inspector.agent.model;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public void addEntry(Transportable item){}

    public void send(JSONArray items){


    }

}
