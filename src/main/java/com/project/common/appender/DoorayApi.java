package com.project.common.appender;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * 두레이 Exception Message 전송부
 *
 * Created by KMS on 25/10/2019.
 */
@Slf4j
public class DoorayApi {

    private static final String POST = "POST";
    private static final String UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE = "application/json";
    private final String service;
    private final int timeout;
    private final Proxy proxy;

    public DoorayApi(String service) {
        this(service, 5000);
    }

    public DoorayApi(String service, Proxy proxy) {
        this(service, 5000, proxy);
    }

    public DoorayApi(String service, int timeout) {
        this(service, timeout, Proxy.NO_PROXY);
    }

    public DoorayApi(String service, int timeout, Proxy proxy) {
        this.timeout = timeout;
        if (service == null) {
            throw new IllegalArgumentException("Missing WebHook URL Configuration @ DoorayApi");
        } else {
            if (proxy == null) {
                this.proxy = Proxy.NO_PROXY;
            } else {
                this.proxy = proxy;
            }

            this.service = service;
        }
    }

    public void call(DoorayMessage message) {
        if (message != null) {
            this.send(message.prepare());
        }

    }

    public String send(JsonObject message) {
        HttpURLConnection connection = null;
        String inputLine;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(this.service);
            connection = (HttpURLConnection) url.openConnection(this.proxy);
            connection.setRequestMethod(POST);
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty("Accept-Charset", UTF_8);
            connection.setConnectTimeout(this.timeout);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(message.toString().getBytes(UTF_8));
            outputStream.flush();
            outputStream.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                stringBuffer.append(inputLine);
                stringBuffer.append('\n');
            }
            in.close();
            return stringBuffer.toString();
        } catch (Exception e) {
            throw new DoorayException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
