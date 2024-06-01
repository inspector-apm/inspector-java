package dev.inspector.agent.transport;

import dev.inspector.agent.model.Transportable;

public interface Transport {

    void flush();

    void addEntry(Transportable item);

    Integer getQueueSize();

}
