package com.biztweets.model;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class Tweet {

    private String appName;
    private String entityName;
    private List<Metadata> metadata;
    private Timestamp insertionTime;

    public Tweet() {
    }

    public Tweet(final String appName, final String entityName, final List<Metadata> metadata, final Timestamp insertionTime) {
        super();
        this.appName = appName;
        this.entityName = entityName;
        this.metadata = metadata;
        this.insertionTime = insertionTime;
    }

    public String getAppName() {
        return appName;
    }

    public String getEntityName() {
        return entityName;
    }

    public Timestamp getInsertionTime() {
        return insertionTime;
    }

    public List<Metadata> getMetadata() {
        return Collections.unmodifiableList(metadata);
    }

    @Override
    public String toString() {
        return "Tweet [appName=" + appName + " entityName=" + entityName + ", metadata=" + metadata + ", insertionTime="
            + insertionTime + "]";
    }

}
