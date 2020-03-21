package com.project.utils.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Http 통신을 위한 유틸리티 클래스
 */
public class HttpUtils {
    private final String USER_AGENT = "Mozilla/5.0";

    /**
     * 일반적인 포스트 요청방식
     * @param target
     * @param msg
     * @return
     */
    public String post(String target, String msg) {
        String inputLine = null;
        StringBuffer sb = new StringBuffer();

        try {
            URL url = new URL(target);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            try (BufferedOutputStream out = new BufferedOutputStream(  conn.getOutputStream()) ) {
                out.write(msg.getBytes());
                out.flush();
            }
            // 응답 내용(BODY) 구하기
            try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream() );
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buf = new byte[1024 * 8];
                int length = 0;
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                inputLine = new String(out.toByteArray(), "UTF-8");
            }
            conn.disconnect();
            return inputLine;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 헤더 세팅이 필요한 post 요청을 할때
     * @param target
     * @param msg
     * @param headers
     * @return
     */
    public String post(String target, String msg, HashMap<String, String> headers) {
        String inputLine = null;
        StringBuffer sb = new StringBuffer();

        try {
            URL url = new URL(target);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);


            Set keys = headers.keySet();
            Iterator it = keys.iterator();
            while( it.hasNext()){
                String key = (String) it.next();
                conn.setRequestProperty(key, (String) headers.get(key)); // add request header
            }

            try (BufferedOutputStream out = new BufferedOutputStream(  conn.getOutputStream()) ) {
                out.write(msg.getBytes());
                out.flush();
            }
            // 응답 내용(BODY) 구하기
            if( conn != null ) {
                try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    byte[] buf = new byte[1024 * 8];
                    int length = 0;
                    while ((length = in.read(buf)) != -1) {
                        out.write(buf, 0, length);
                    }
                    inputLine = new String(out.toByteArray(), "UTF-8");
                }
                conn.disconnect();
            }
            return inputLine;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 일반적인 get 요청 방식
     * @param targetUrl
     * @return
     * @throws Exception
     */
    public JsonObject get(String targetUrl) throws Exception {
        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET"); // optional default is GET
        con.setRequestProperty("User-Agent", USER_AGENT); // add request header
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close(); // print result
//        System.out.println("HTTP 응답 코드 : " + responseCode);
////        System.out.println("HTTP body : " + response.toString());

        Gson gson = new Gson();
        return gson.fromJson(response.toString(), JsonObject.class ) ;

    }

    /**
     * Get 요청 방식 , 헤더 세팅이 필요할때 사용함.
     * @param targetUrl
     * @param headers
     * @return
     * @throws Exception
     */
    public JsonObject get(String targetUrl, HashMap<String, String> headers ) throws Exception {
        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET"); // optional default is GET
        con.setRequestProperty("User-Agent", USER_AGENT); // add request header

        Set keys = headers.keySet();
        Iterator it = keys.iterator();
        while( it.hasNext()){
            String key = (String) it.next();
            con.setRequestProperty(key, (String) headers.get(key)); // add request header
        }

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close(); // print result
//        System.out.println("HTTP 응답 코드 : " + responseCode);
////        System.out.println("HTTP body : " + response.toString());

        Gson gson = new Gson();
        return gson.fromJson(response.toString(), JsonObject.class ) ;

    }
}
