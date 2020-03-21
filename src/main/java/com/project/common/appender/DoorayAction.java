package com.project.common.appender;

import com.google.gson.JsonObject;

/**
 * 두레이 액션 구성 클래스 (json)
 *
 * Created by KMS on 25/10/2019.
 */
public class DoorayAction {

    private static final String NAME = "name";
    private static final String TEXT = "text";
    private static final String TYPE = "type";
    private static final String DATA_SOURCE = "dataSource";
    private static final String VALUE = "value";

    private String name;
    private String text;
    private DoorayActionType type;
    private String value;

    public DoorayAction(String name, String text, DoorayActionType type, String value) {
        this.name = name;
        this.text = text;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DoorayActionType getType() {
        return this.type;
    }

    public void setType(DoorayActionType type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public JsonObject toJson() {
        JsonObject data = new JsonObject();
        data.addProperty(NAME, this.name);
        data.addProperty(TEXT, this.text);
        if (this.type != null) {
            data.addProperty(TYPE, this.type.getCode());
        }

        data.addProperty(VALUE, this.value);
        return data;
    }
}
