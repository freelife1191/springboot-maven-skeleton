package com.project.common.appender;

import com.google.gson.JsonObject;

import java.util.Objects;

/**
 * 두레이 필드 구성 클래스 (json)
 *
 * Created by KMS on 25/10/2019.
 */
public class DoorayField {

    private static final String TITLE = "title";
    private static final String VALUE = "value";
    private static final String SHORT = "short";

    private boolean shorten = false;
    private String title = null;
    private String value = null;

    public boolean isShorten() {
        return this.shorten;
    }

    public DoorayField setShorten(boolean shorten) {
        this.shorten = shorten;
        return this;
    }

    public DoorayField setTitle(String title) {
        this.title = title;
        return this;
    }

    public DoorayField setValue(String value) {
        this.value = value;
        return this;
    }

    public JsonObject toJson() {
        final JsonObject data = new JsonObject();
        data.addProperty(TITLE, this.title);
        data.addProperty(VALUE, this.value);
        data.addProperty(SHORT, this.shorten);
        return data;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final DoorayField that = (DoorayField) o;

        if (shorten != that.shorten)
            return false;
        if (!Objects.equals(title, that.title)) {
            return false;
        }
        return !(!Objects.equals(value, that.value));
    }

    public String toString() {
        return "DoorayField{shorten=" + this.shorten + ", title='" + this.title + '\'' + ", value='" + this.value + '\'' + '}';
    }
}
