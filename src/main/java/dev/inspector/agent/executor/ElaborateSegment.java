package dev.inspector.agent.executor;

import dev.inspector.agent.model.Segment;

public interface ElaborateSegment {
    Segment execute(Segment segment);
}
