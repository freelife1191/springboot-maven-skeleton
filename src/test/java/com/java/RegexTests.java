package com.java;

import com.project.utils.common.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KMS on 25/03/2020.
 */
public class RegexTests {

    @Test
    public void 일치하는패턴() {
        String region = "경상북도>영등포구";
        region = "";

        String result = null;
        Pattern regex = Pattern.compile("^[가-힣]+");
        Matcher matcher = regex.matcher(region);

        if(matcher.find())
            result = matcher.group();

        System.out.println(result);
    }

    @Test
    public void 두번째일치하는패턴() {
        String region = "경상북도>영등포구";

        String result = null;
        Pattern regex = Pattern.compile("(?![가-힣]+>)[가-힣]+");
        Matcher matcher = regex.matcher(region);

        if(matcher.find())
            result = matcher.group();

        System.out.println(result);
    }

    @Test
    public void 시도가져오기() {
        String SIDO = "서울|부산|대구|인천|광주|대전|울산|세종|경기|강원|충북|충남|전북|경북|경남|제주";

        String result = null;
        String address = "서울특별시 용산구 원효로3가 50-1";
        //        address = "서울 용산구 한강로3가 2-8 한강로3가 2-1";
        Pattern regex = Pattern.compile("([가-힣]{1,5}+(시|도)|"+SIDO+")");
        Matcher matcher = regex.matcher(address);

        if(matcher.find())
            result = matcher.group();

        System.out.println(result);
    }

    @Test
    public void 시구군가져오기() {
        String SIDO = "서울|부산|대구|인천|광주|대전|울산|세종|경기|강원|충북|충남|전북|경북|경남|제주";

        String result = null;
        String address = "서울특별시 용산구 원효로3가 50-1";
        //        address = "서울 용산구 한강로3가 2-8 한강로3가 2-1";
        Pattern regex = Pattern.compile("([가-힣]+(시|도)|"+SIDO+")");
        Matcher matcher = regex.matcher(address);

        if(matcher.find())
            result = matcher.group();

        System.out.println(result);
    }

    @Test
    public void 문자제외_테스트() {
        String SIDO = "서울|부산|대구|인천|광주|대전|울산|세종|경기|강원|충북|충남|전북|경북|경남|제주";

        String result = null;
        String address = "서울특별시 용산구 원효로3가 50-1";
        //        address = "서울 용산구 한강로3가 2-8 한강로3가 2-1";
        //        address = "서울";
        Pattern regex = Pattern.compile("(?![가-힣]+(시|도)|"+SIDO+"\\s)[가-힣]+(시|군|구)");
        Matcher matcher = regex.matcher(address);

        if(matcher.find())
            result = matcher.group();

        System.out.println(result);
    }

    @Test
    public void 읍면동추출_테스트() {
        String SIDO = "서울|부산|대구|인천|광주|대전|울산|세종|경기|강원|충북|충남|전북|경북|경남|제주";
        String DONG_NUM = "\\d{1,2}|\\d{1,2}(,|\\.)\\d{1,2}";
        String BUNGI_NUM = "\\d{1,5}(~|-)\\d{1,5}|\\d{1,5}";
        String DONG_STR = "([가-힣]{1,5})("+DONG_NUM+"|)(읍|면|동|가|리)";
        String ROAD_STR = "([가-힣]{1,5})(("+BUNGI_NUM+")|("+BUNGI_NUM+"[가-힣]{1,5})|)(로|길)";

        String result = null;
        String address = "서울특별시 용산구 원효로3가 50-1";
        address = "서울 용산구 한강로3가 2-8 한강로3가 2-1";
        address = "서울특별시 서대문구 연희맛로 20 (연희동 188-51,연희빌딩) ";
        //        address = "서울";
        Pattern regex = Pattern.compile("(?![가-힣]+(시|도)|"+SIDO+"\\s[가-힣]+(시|군|구)\\s)("+DONG_STR+")|("+ROAD_STR+")");
        Matcher matcher = regex.matcher(address);

        if(matcher.find())
            result = matcher.group();

        System.out.println(result);
    }

    @Test
    public void ipRegexTest() {
        String ip1 = "127.0.0.1";
        String ip2 = "255.255.255.255";
        String ip3 = "255.255.255.256";
        String ipRegex = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";

        System.out.println(ip1.matches(ipRegex));
        System.out.println(ip2.matches(ipRegex));
        System.out.println(ip3.matches(ipRegex));

    }

    @Test
    public void ipRegexUtilTest() {
        String ip = "127.0.0.1";
        System.out.println("## IP TEST : "+ ValidationUtils.isIp(ip));
    }

    @Test
    public void portRegexUtilTest() {
        Integer port = 65536;
        System.out.println("## PORT TEST = "+ ValidationUtils.isPort(port));
    }
}
