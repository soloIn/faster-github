package com.sololn.fastergithub.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        try {
            List<String> urls = new ArrayList<>();
            Stream<String> lines = Files.lines(Paths.get("src", "main", "resources", "urls.txt"));
            lines.forEach(url -> {
                urls.add(url);
            });
            return urls;
        } catch (IOException e){
            throw new RuntimeException(e.getCause());
        }
    }
    
    public static Map<String, String> readIpMapFromHttp(List<String> urls) {
        urls.forEach(u -> {
            log.info("url -- {}", u);
        });
        return null;
    }
}
