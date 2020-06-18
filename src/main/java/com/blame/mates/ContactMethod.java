package com.blame.mates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class ContactMethod {
    private Type type;
    private URL intentionUrl;
    private String name;

    @JsonCreator
    public ContactMethod(@JsonProperty("type") Type type,
                         @JsonProperty("intentionUrl") URL intentionUrl,
                         @JsonProperty("name") String name) {
        this.type = type;
        this.intentionUrl = intentionUrl;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public URL getIntentionUrl() {
        return intentionUrl;
    }

    public void setIntentionUrl(URL intentionUrl) {
        this.intentionUrl = intentionUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Type {
        TELEGRAM, EMAIL, VK, OTHER
    }
}
