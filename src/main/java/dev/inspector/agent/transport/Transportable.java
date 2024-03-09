package dev.inspector.agent.transport;

import org.json.JSONObject;

public interface Transportable {
    JSONObject toTransport();
}
