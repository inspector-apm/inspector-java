package dev.inspector.agent.model;

public enum TransactionType {

    GENERAL("transaction"),
    REQUEST("request"),
    SCHEDULER("scheduler");

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
