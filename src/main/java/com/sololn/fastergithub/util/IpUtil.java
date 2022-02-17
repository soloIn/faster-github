package com.sololn.fastergithub.util;

import java.util.Arrays;
import java.util.Map;

/**
 * TODO
 *
 * @author guojian
 **/
public class IpUtil {
    public static Map<String, String> readIpMap(){
        String[] urls = readUrlFromTxt();
       return readIpMapFromHttp(urls);
    }
    
    public static String[] readUrlFromTxt() {
        // todo
        return new String[]{""};
    }
    
    public static Map<String, String> readIpMapFromHttp(String[] urls) {
        Arrays.stream(urls).forEach(url -> {
            String s = Inet4Address.getIpFromJson(url);
        });
        return null;
    }
}
