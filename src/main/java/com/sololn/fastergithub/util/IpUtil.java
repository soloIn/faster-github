package com.sololn.fastergithub.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
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
            StringBuilder url = new StringBuilder();
            url.append(queryUrl).append(u).append("&");
            Optional<String> reduce = Arrays.stream(params).reduce((x, y) -> x +
                    "&" + y);
            reduce.ifPresent(url::append);
            String res = Inet4Address.sendGet(url.toString());
            //String ipFromJson = JsonUtil.getIpFromJson(res);
            JSONObject result = JSONObject.parseObject(res).getJSONObject("result");
            String a = result.getString("a");
            String ipFromJson = a.split(",")[0];
            if (StringUtils.isEmpty(ipFromJson)){
                log.info("source {} there is no available ip", u);
                error.add(u);
                return;
            }
            ipMap.put(u, ipFromJson);
        });
        log.error("error: {}",error.toString());
        return ipMap;
    }
    
    public static String readRequestUrlFromDom(String url) {
        try {
            URL realUrl = new URL(url);
            Document document = Jsoup.parse(realUrl, 10000);
            Elements elements = document.select("a");
            Elements elementsByAttributeValue = document.getElementsByAttributeValue("target", "_blank");
            for (Element element : elementsByAttributeValue) {
                if (element.attr("href").contains("api.k780.com")) {
                    return   element.html();
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return "";
    }
}
