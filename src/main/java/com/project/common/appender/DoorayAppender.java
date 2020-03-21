package com.project.common.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.util.ContextUtil;
import com.project.utils.common.JsonUtils;
import com.project.utils.common.MDCUtil;
import org.springframework.util.StringUtils;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 두레이로 Exception 메세지를 던지기 위한 Appender 클래스
 *
 * Created by KMS on 25/10/2019.
 */
public class DoorayAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private boolean enabled;
    private String token;
    private String botName;
    private String botImage;
    private Level level;

    private final int STACK_MESSAGE_SIZE_LIMIT = 50;

    @Override
    public void doAppend(ILoggingEvent eventObject) {
        super.doAppend(eventObject);
    }

    @Override
    protected void append(ILoggingEvent loggingEvent) {
        if (loggingEvent.getLevel().isGreaterOrEqual(level)) {
            if (isEnabled()) {
                toDooray(loggingEvent);
            }
        }
    }

    private void toDooray(ILoggingEvent loggingEvent) {
        if (loggingEvent.getLevel().isGreaterOrEqual(level)) {
            List<DoorayField> fields = new ArrayList<>();

            DoorayAttachment doorayAttachment = new DoorayAttachment();
            doorayAttachment.setColor("red");
            //exception message 제목
            doorayAttachment.setTitle(loggingEvent.getFormattedMessage());

            if (!StringUtils.isEmpty(MDCUtil.get(MDCUtil.REQUEST_URI_MDC))) {
                DoorayField requestUrl = new DoorayField();
                requestUrl.setTitle("요청 URL");
                requestUrl.setValue(MDCUtil.get(MDCUtil.REQUEST_URI_MDC));
                requestUrl.setShorten(false);
                fields.add(requestUrl);
            }

//            Object[] obj = loggingEvent.getArgumentArray();
//            if (obj != null && obj.length > 0) {
//                if (obj[0] instanceof String) {
//                    DoorayField errorDetail = new DoorayField();
//                    errorDetail.setTitle("에러 대상");
//                    errorDetail.setValue((String) obj[0]);
//                    errorDetail.setShorten(false);
//                    fields.add(errorDetail);
//                }
//            }

            if (loggingEvent.getThrowableProxy() != null) {
                IThrowableProxy iThrowableProxy = loggingEvent.getThrowableProxy();
                if (iThrowableProxy == null) {
                    iThrowableProxy = loggingEvent.getThrowableProxy();
                }
                DoorayField exceptionClassName = new DoorayField();
                exceptionClassName.setTitle("에러 내용");
                exceptionClassName.setShorten(false);
                exceptionClassName.setValue(iThrowableProxy.getClassName() + " / " + (!StringUtils.isEmpty(iThrowableProxy.getMessage()) ? iThrowableProxy.getMessage() : ""));
                fields.add(0, exceptionClassName);
                doorayAttachment.setText(getStackTrace(iThrowableProxy.getStackTraceElementProxyArray()));
            }

            if (!StringUtils.isEmpty(MDCUtil.get(MDCUtil.AGENT_DETAIL_MDC))) {
                DoorayField agentDetail = new DoorayField();
                agentDetail.setTitle("사용자 환경정보");
                agentDetail.setValue(JsonUtils.toPrettyJson(MDCUtil.get(MDCUtil.AGENT_DETAIL_MDC)));
                agentDetail.setShorten(true);
                fields.add(agentDetail);
            }

            DoorayField issueRating = new DoorayField();
            issueRating.setTitle("장애 등급");
            //TODO: 등급 분류 지정 수립 후 수정
            issueRating.setValue("등급 분류 필요");
            issueRating.setShorten(true);
            fields.add(issueRating);

            DoorayField dateTime = new DoorayField();
            dateTime.setTitle("발생 시간");
            dateTime.setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            dateTime.setShorten(true);
            fields.add(dateTime);

            DoorayField hostName = new DoorayField();
            hostName.setTitle("호스트 명");
            hostName.setValue(getHostName());
            hostName.setShorten(true);
            fields.add(hostName);

            if (!StringUtils.isEmpty(MDCUtil.get(MDCUtil.HEADER_MAP_MDC))) {
                DoorayField headerInformation = new DoorayField();
                headerInformation.setTitle("Http Header 정보");
                headerInformation.setValue(JsonUtils.toPrettyJson(MDCUtil.get(MDCUtil.HEADER_MAP_MDC)));
                headerInformation.setShorten(false);
                fields.add(headerInformation);
            }

            if (!StringUtils.isEmpty(MDCUtil.get(MDCUtil.PARAMETER_MAP_MDC))) {
                DoorayField bodyInformation = new DoorayField();
                bodyInformation.setTitle("Http Body 정보");
                bodyInformation.setValue(MDCUtil.get(MDCUtil.PARAMETER_MAP_MDC));
                bodyInformation.setShorten(false);
                fields.add(bodyInformation);
            }

            doorayAttachment.setFields(fields);

            DoorayMessage doorayMessage = new DoorayMessage("");
            doorayMessage.setBotName(botName);
            doorayMessage.setBotIconImage(botImage);
            doorayMessage.setAttachments(Collections.singletonList(doorayAttachment));

            DoorayApi doorayApi = new DoorayApi(token);
            doorayApi.call(doorayMessage);
        }
    }

    /**
     * Gets stack trace.
     *
     * @param stackTraceElements the stack trace elements
     * @return the stack trace
     */
    public String getStackTrace(StackTraceElementProxy[] stackTraceElements) {
        if (stackTraceElements == null || stackTraceElements.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        int stackMessageSize = stackTraceElements.length;
        if (stackMessageSize > STACK_MESSAGE_SIZE_LIMIT) {
            stackMessageSize = STACK_MESSAGE_SIZE_LIMIT;
        }

        for (int i = 0; i < stackMessageSize; i++) {
            sb.append(stackTraceElements[i].toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Gets host name.
     *
     * @return the host name
     */
    public String getHostName() {
        try {
            return ContextUtil.getLocalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotImage() {
        return botImage;
    }

    public void setBotImage(String botImage) {
        this.botImage = botImage;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
