package dev.inspector.agent.utility;

import org.json.JSONObject;

public class JsonBuilder {
    private final JSONObject jsonObject;

    public JsonBuilder() {
        this.jsonObject = new JSONObject();
    }

    public JsonBuilder put(String key, Object value) {
        jsonObject.put(key, value);
        return this;
    }

    public JSONObject build() {
        return jsonObject;
    }
}
