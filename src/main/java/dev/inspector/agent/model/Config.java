package dev.inspector.agent.model;

public class Config {

    private Long timeToFlush;
    private String ingestionKey;
    private String url = "https://ingest.inspector.dev";
    private boolean enabled = true;
    private boolean autoWiring = true;

    private int maxEntries = 100;
    private String version = "1.9.10";

    public Config(String ingestionKey, String timeToFlush){
        this.ingestionKey = ingestionKey;
        this.timeToFlush = Long.valueOf(timeToFlush);
    }


    public String getIngestionKey() {
        return ingestionKey;
    }

    public Config setIngestionKey(String ingestionKey) {
        this.ingestionKey = ingestionKey;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Config setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Config setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isAutoWiring() {
        return autoWiring;
    }

    public Config setAutoWiring(boolean autoWiring) {
        this.autoWiring = autoWiring;
        return this;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public Config setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Config setVersion(String version) {
        this.version = version;
        return this;
    }

    public Long getTimeToFlush() {
        return timeToFlush;
    }

    public void setTimeToFlush(Long timeToFlush) {
        this.timeToFlush = timeToFlush;
    }
}
