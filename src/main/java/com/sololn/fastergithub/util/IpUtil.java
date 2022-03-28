package com.sololn.fastergithub.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @author guojian
 **/
@Slf4j
public class IpUtil {
    public static Map<String, String> readIpMap(String queryUrl, String ... params){
        List<String> sources = readUrlFromTxt();
       return readIpMapFromHttp(sources, queryUrl, params);
    }
    
    public static List<String> readUrlFromTxt(){
        try (Stream<String> lines = Files.lines(Paths.get("src", "main", "resources", "urls.txt"))){
            List<String> urls = new ArrayList<>();
            lines.forEach(urls::add);
            return urls;
        } catch (IOException e){
            throw new RuntimeException(e.getCause());
        }
    }
    
    public static Map<String, String> readIpMapFromHttp(List<String> sources, String queryUrl, String ... params){
        Map<String, String> ipMap = new HashMap<>();
        List<String> error = new ArrayList<>();
        sources.forEach(u -> {
            try {
                StringBuilder url = new StringBuilder();
                url.append(queryUrl).append(u).append("&");
                Optional<String> reduce = Arrays.stream(params).reduce((x, y) -> x +
                        "&" + y);
                reduce.ifPresent(url::append);
                String res = Inet4Address.sendGet(url.toString());
                String ipFromJson = JsonUtil.getIpFromJson(res);
                if (StringUtils.isEmpty(ipFromJson)){
                    log.info("source {} there is no available ip", u);
                    error.add(u);
                    return;
                }
                ipMap.put(u, ipFromJson);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
        return ipMap;
    }
    
}
