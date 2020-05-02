package com.blame.mates;

import javax.swing.*;
import java.net.URL;

public class ContactMethod {
    private Type type;
    private URL intentionUrl;
    private String name;
    private Icon icon;

    public ContactMethod(Type type, URL intentionUrl, String name, Icon icon) {
        this.type = type;
        this.intentionUrl = intentionUrl;
        this.name = name;
        this.icon = icon;
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

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public enum Type {
        TELEGRAM, EMAIL, VK, OTHER
    }
}
