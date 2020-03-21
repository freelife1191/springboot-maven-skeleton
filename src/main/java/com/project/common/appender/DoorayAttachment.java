package com.project.common.appender;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 두레이 attachment 구성 클래스
 *
 * Created by KMS on 25/10/2019.
 */
public class DoorayAttachment {

    private static final String CALLBACK_ID = "callbackId";
    private static final String TEXT = "text";
    private static final String TITLE = "title";
    private static final String TITLE_LINK = "titleLink";
    private static final String AUTHOR_NAME = "authorName";
    private static final String AUTHOR_LINK = "authorLink";
    private static final String COLOR = "color";
    private static final String IMAGE_URL = "imageUrl";
    private static final String THUMB_URL = "thumbUrl";
    private static final String ACTIONS = "actions";
    private static final String FIELDS = "fields";

    private String callbackId;
    private String text;
    private String color;
    private String authorName;
    private String authorLink;
    private String title;
    private String titleLink;
    private String imageUrl;
    private String thumbUrl;

    private List<DoorayField> fields = new ArrayList();
    private List<DoorayAction> actions = new ArrayList<DoorayAction>();

    public DoorayAttachment() {
    }

    public DoorayAttachment addFields(DoorayField field) {
        this.fields.add(field);
        return this;
    }

    public DoorayAttachment addAction(DoorayAction action) {
        this.actions.add(action);

        return this;
    }

    private JsonArray prepareFields() {
        final JsonArray data = new JsonArray();
        for (DoorayField field : fields) {
            data.add(field.toJson());
        }
        return data;
    }


    private JsonArray prepareActions() {
        final JsonArray data = new JsonArray();
        for (DoorayAction action : actions) {
            data.add(action.toJson());
        }
        return data;
    }

    public DoorayAttachment removeAction(int index) {
        this.actions.remove(index);
        return this;
    }


    public DoorayAttachment removeFields(int index) {
        this.fields.remove(index);
        return this;
    }

    private boolean isHex(String pair) {
        return pair.matches("^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    }

    public DoorayAttachment setColor(String color) {
        if (color != null) {
            if (color.charAt(0) == '#') {
                if (!this.isHex(color.substring(1))) {
                    throw new IllegalArgumentException("Invalid Hex Color @ DoorayAttachment");
                }
            }
        }

        this.color = color;
        return this;
    }

    public DoorayAttachment setCallbackId(String callbackId) {
        this.callbackId = callbackId;
        return this;
    }

    public DoorayAttachment setFields(List<DoorayField> fields) {
        this.fields = fields;
        return this;
    }

    public DoorayAttachment setText(String text) {
        this.text = text;
        return this;
    }

    public DoorayAttachment setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public DoorayAttachment setAuthorLink(String authorLink) {
        this.authorLink = authorLink;
        return this;
    }

    public DoorayAttachment setTitle(String title) {
        this.title = title;
        return this;
    }

    public DoorayAttachment setTitleLink(String titleLink) {
        this.titleLink = titleLink;
        return this;
    }

    public DoorayAttachment setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public DoorayAttachment setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
        return this;
    }

    public JsonObject toJson() {
        JsonObject data = new JsonObject();
        if (this.text != null) {
            data.addProperty(TEXT, this.text);
        }

        if (this.color != null) {
            data.addProperty(COLOR, this.color);
        }

        if (this.authorName != null) {
            data.addProperty(AUTHOR_NAME, this.authorName);
        }

        if (this.authorLink != null) {
            data.addProperty(AUTHOR_LINK, this.authorLink);
        }

        if (this.title != null) {
            data.addProperty(TITLE, this.title);
        }

        if (this.titleLink != null) {
            data.addProperty(TITLE_LINK, this.titleLink);
        }

        if (this.imageUrl != null) {
            data.addProperty(IMAGE_URL, this.imageUrl);
        }

        if (this.thumbUrl != null) {
            data.addProperty(THUMB_URL, this.thumbUrl);
        }

        if (this.fields != null && this.fields.size() > 0) {
            data.add(FIELDS, this.prepareFields());
        }

        if (actions != null && actions.size() > 0) {
            data.add(ACTIONS, prepareActions());

            if (callbackId == null) {
                throw new IllegalArgumentException("Missing Callback ID @ DoorayAttachment");
            } else {
                data.addProperty(CALLBACK_ID, callbackId);
            }
        }

        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DoorayAttachment that = (DoorayAttachment) o;

        if (!Objects.equals(text, that.text)) {
            return false;
        }
        if (!Objects.equals(color, that.color)) {
            return false;
        }
        if (!Objects.equals(authorName, that.authorName)) {
            return false;
        }
        if (!Objects.equals(authorLink, that.authorLink)) {
            return false;
        }
        if (!Objects.equals(title, that.title)) {
            return false;
        }
        if (!Objects.equals(titleLink, that.titleLink)) {
            return false;
        }
        if (!Objects.equals(imageUrl, that.imageUrl)) {
            return false;
        }
        if (!Objects.equals(thumbUrl, that.thumbUrl)) {
            return false;
        }
        return !(!Objects.equals(fields, that.fields));

    }

    @Override
    public String toString() {
        return "SlackAttachment{" + " text='" + text + '\'' + ", color='" + color + '\'' + ", authorName='" + authorName + '\'' + ", authorLink='"
                + authorLink + '\'' + ", title='" + title + '\''
                + ", titleLink='" + titleLink + '\'' + ", imageUrl='" + imageUrl + '\'' + ", thumbUrl='" + thumbUrl
                + '\'' + ", fields=" + fields + '}';
    }
}
