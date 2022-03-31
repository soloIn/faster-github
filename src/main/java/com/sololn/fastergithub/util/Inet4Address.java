package com.sololn.fastergithub.util;

import com.sololn.fastergithub.Starter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName getIP
 * @Author HeGuojian
 * @Date 2021/8/30 16:57
 * @Version 1.0
 **/
public class Inet4Address {
    private static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void SkipSSL() throws NoSuchAlgorithmException, KeyManagementException {
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        //  配置认证管理器
        javax.net.ssl.TrustManager[] trustAllCerts = {new TrustAllTrustManager()};
        SSLContext sc = SSLContext.getInstance("SSL");
        SSLSessionContext sslsc = sc.getServerSessionContext();
        sslsc.setSessionTimeout(0);
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        //  激活主机认证
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
    
    
    public static String sendGet(String url) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            logger.info("url is {}",url);
            SkipSSL();
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

    

    /*public static String getIpFromJson(String url){
      return sendGet(url);
    }
    public static Map<String, String> getIps(){
        String[] urls = {"alive.github.com",
                "live.github.com",
                "github.githubassets.com",
                "central.github.com",
                "desktop.githubusercontent.com",
                "assets-cdn.github.com",
                "camo.githubusercontent.com",
                "github.map.fastly.net",
                "github.global.ssl.fastly.net",
                "gist.github.com",
                "github.io",
                "github.com",
                "github.blog",
                "api.github.com",
                "raw.githubusercontent.com",
                "user-images.githubusercontent.com",
                "favicons.githubusercontent.com",
                "avatars5.githubusercontent.com",
                "avatars4.githubusercontent.com",
                "avatars3.githubusercontent.com",
                "avatars2.githubusercontent.com",
                "avatars1.githubusercontent.com",
                "avatars0.githubusercontent.com",
                "avatars.githubusercontent.com",
                "codeload.github.com",
                "github-cloud.s3.amazonaws.com",
                "github-com.s3.amazonaws.com",
                "github-production-release-asset-2e65be.s3.amazonaws.com",
                "github-production-user-asset-6210df.s3.amazonaws.com",
                "github-production-repository-file-5c1aeb.s3.amazonaws.com",
                "githubstatus.com",
                "github.community",
                "media.githubusercontent.com"};
        Map<String, String> ips = new HashMap<>();
        for (String s : urls){
            String response = Inet4Address.sendGet(s);
            *//*String ip = Inet4Address.getIpFromStr(response);
            if (null == ip){
                continue;
            }*//*
            if ("".equals(response)){
                continue;
            }
            logger.info("final ip is {}",response);
            ips.put(s, response);
        }
        return ips;
    }
    public static String getIpFromStr (String response){
        String regEx = "\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher;
        if (response.startsWith("<!DOCTYPE")) {
            int i = response.indexOf("<th>IP");
            String line = response.substring(i, i + 100);
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                return matcher.group();
            } else {
                int j = response.indexOf("<li><a href=\"https://www.ipaddress.com/ipv4");
                String line2 = response.substring(j, j + 100);
                 matcher = pattern.matcher(line2);
                if (matcher.find()) {
                    return matcher.group();
                }
            }
        }
        return null;
    }
*/
}
