package com.yike.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilakeyc
 * @since 2017/2/22
 */
public class WxButton {

    public static final String typeClick = "click";
    public static final String typeView = "view";

    private String type;
    private String name;
    private String key;
    private String url;
    private List<WxButton> sub_button;

    public WxButton(String name) {
        this(name, null, null, null);
    }

    public WxButton(String name, String type, String key, String url) {
        this.name = name;
        this.type = type;
        this.key = key;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WxButton> getSub_button() {
        if (sub_button == null) {
            sub_button = new ArrayList<WxButton>();
        }
        return sub_button;
    }

    public void setSub_button(List<WxButton> sub_button) {
        this.sub_button = sub_button;
    }

    public WxButton addSubButton(WxButton button) {
        if (button != null) {
            getSub_button().add(button);
        }
        return this;
    }
}
