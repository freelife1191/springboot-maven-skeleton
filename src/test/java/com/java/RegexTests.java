package com.java;

import org.junit.jupiter.api.Test;

/**
 * Created by KMS on 25/03/2020.
 */
public class RegexTests {

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
}
