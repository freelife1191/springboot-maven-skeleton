package com.project.common.appender;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 두레이 메세지 구성 클래스 (json)
 *
 * Created by KMS on 25/10/2019.
 */
public class DoorayMessage {

    private static final String BOT_NAME = "botName";
    private static final String BOT_ICON_IMAGE = "botIconImage";
    private static final String TEXT = "text";
    private static final String ATTACHMENTS = "attachments";

    private List<DoorayAttachment> attach = new ArrayList<>();
    private JsonObject doorayMessage = new JsonObject();

    private String text;
    private String botName;
    private String botIconImage;

    public DoorayMessage() {
    }

    public DoorayMessage(String text) {
        this(null, null, text);
    }

    public DoorayMessage(String botName, String text) {
        this(null, botName, text);
    }

    public DoorayMessage(String botIconImage, String botName, String text) {
        if (botIconImage != null) {
            this.botIconImage = botIconImage;
        }

        if (botName != null) {
            this.botName = botName;
        }

        this.text = text;
    }

    public DoorayMessage addAttachments(DoorayAttachment attach) {
        this.attach.add(attach);
        return this;
    }

    public JsonObject prepare() {
        if (botIconImage != null) {
            doorayMessage.addProperty(BOT_ICON_IMAGE, botIconImage);
        }

        if (botName != null) {
            doorayMessage.addProperty(BOT_NAME, botName);
        }

        if (text != null) {
            doorayMessage.addProperty(TEXT, text);
        }

        if (!attach.isEmpty()) {
            doorayMessage.add(ATTACHMENTS, this.prepareAttach());
        }

        return doorayMessage;
    }

    private JsonArray prepareAttach() {
        final JsonArray attachs = new JsonArray();
        for (DoorayAttachment attach : this.attach) {
            attachs.add(attach.toJson());
        }

        return attachs;
    }

    public DoorayMessage removeAttachment(int index) {
        this.attach.remove(index);

        return this;
    }

    public DoorayMessage setAttachments(List<DoorayAttachment> attach) {
        this.attach = attach;

        return this;
    }

    public DoorayMessage setText(String message) {
        if (message != null) {
            this.text = message;
        }

        return this;
    }

    public DoorayMessage setBotName(String botName) {
        if (botName != null) {
            this.botName = botName;
        }
        return this;
    }

    public DoorayMessage setBotIconImage(String botIconImage) {
        if (botIconImage != null) {
            this.botIconImage = botIconImage;
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final DoorayMessage that = (DoorayMessage) o;

        if (!Objects.equals(attach, that.attach)) {
            return false;
        }
        if (!Objects.equals(text, that.text)) {
            return false;
        }

        if (!Objects.equals(botIconImage, that.botIconImage)) {
            return false;
        }

        return !(!Objects.equals(botName, that.botName));

    }

    @Override
    public String toString() {
        return "SlackMessage{" + "attach=" + attach + ", text='" + text + '\'' + ", botName='" + botName + '\''
                + ", botIconImage=" + botIconImage + '}';
    }

}
