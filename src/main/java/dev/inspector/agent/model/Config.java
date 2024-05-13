package dev.inspector.agent.model;

public class Config {

    private String ingestionKey;
    private String url;
    private Boolean enabled = true;

    private Integer maxEntries = 100;
    private String version = "1.9.10";

    public Config(String ingestionKey, String url, String enabled, String version, String maxEntries){
        this.ingestionKey = ingestionKey;
        this.url = url;
        this.enabled = Boolean.valueOf(enabled);
        this.version = version;
        this.maxEntries = Integer.valueOf(maxEntries);
    }


    public Config setIngestionKey(String ingestionKey) {
        this.ingestionKey = ingestionKey;
        return this;
    }


    public Config setUrl(String url) {
        this.url = url;
        return this;
    }


    public Config setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }



    public Config setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
        return this;
    }


    public Config setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getIngestionKey() {
        return ingestionKey;
    }

    public String getUrl() {
        return url;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Integer getMaxEntries() {
        return maxEntries;
    }

    public String getVersion() {
        return version;
    }
}
