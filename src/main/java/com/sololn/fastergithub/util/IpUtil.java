package com.sololn.fastergithub.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @author guojian
 **/
@Slf4j
public class IpUtil {
    public static Map<String, String> readIpMap(){
        List<String> urls = readUrlFromTxt();
       return readIpMapFromHttp(urls);
    }
    
    public static List<String> readUrlFromTxt(){
        try (Stream<String> lines = Files.lines(Paths.get("src", "main", "resources", "urls.txt"))){
            List<String> urls = new ArrayList<>();
            lines.forEach(url -> {
                urls.add(url);
            });
            return urls;
        } catch (IOException e){
            throw new RuntimeException(e.getCause());
        }
    }
    
    public static Map<String, String> readIpMapFromHttp(List<String> urls){
        Map<String, String> ipMap = new HashMap<>();
        urls.forEach(u -> {
            try {
                String url = "https://myssl.com/api/v1/tools/dns_query?qtype=1&host=" + u + "&qmode=-1";
                String res = Inet4Address.sendGet(url);
                ipMap.put(u, JsonUtil.getIpFromJson(res));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
        return ipMap;
    }
    
}
