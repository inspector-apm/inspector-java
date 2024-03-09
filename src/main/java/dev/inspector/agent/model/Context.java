package dev.inspector.agent.model;

import org.json.JSONObject;

import java.util.HashMap;

public class Context {

    protected HashMap<String , JSONObject> context = new HashMap();

    public void addContext(String label, JSONObject data){
        context.put(label, data);
    }

}
