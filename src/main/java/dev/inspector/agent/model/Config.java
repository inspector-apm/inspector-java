package dev.inspector.agent.model;

public class Config {

    private String ingestionKey;
    private String url = "https://ingest.inspector.dev";
    private boolean enabled = true;
    private boolean autoWiring = true;

    private int maxEntries = 100;
    private String version = "1.9.10";

    public Config(String ingestionKey){
        this.ingestionKey = ingestionKey;
    }

    public String getIngestionKey() {
        return ingestionKey;
    }

    public void setIngestionKey(String ingestionKey) {
        this.ingestionKey = ingestionKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAutoWiring() {
        return autoWiring;
    }

    public void setAutoWiring(boolean autoWiring) {
        this.autoWiring = autoWiring;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
