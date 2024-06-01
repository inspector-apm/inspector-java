package dev.inspector.agent.model;

import org.json.JSONObject;

public interface Transportable {
    JSONObject toTransport();
}
