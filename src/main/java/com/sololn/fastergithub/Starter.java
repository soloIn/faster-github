package com.sololn.fastergithub;

import com.sololn.fastergithub.util.HostUtil;
import com.sololn.fastergithub.util.IpUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 ** springboot 启动时执行
 * */
@Component
public class Starter implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        //String url  = "https://myssl.com/api/v1/tools/dns_query?qtype=1&host=";
        Path outPath = Paths.get(System.getProperty("user.dir") ,"hosts");
        String queryUrl = IpUtil.readRequestUrlFromDom("https://www.nowapi.com/api/dns.a");
        queryUrl = queryUrl.replaceAll("&amp;", "&");
        // http://api.k780.com/?app=dns.a&domain=www.qq.com&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
        int start = queryUrl.indexOf("&");
        int end = queryUrl.indexOf("&", start + 1);
        String url = queryUrl.substring(0, start) + "&domain=";
        String params = queryUrl.substring(end + 1 );
        //Map<String, String> stringStringMap = IpUtil.readIpMap(url, params);
        
        Map<String, String> stringStringMap = IpUtil.readIpMap(url, params);
        HostUtil.toFile(stringStringMap, outPath);
    }
}
