package com.sololn.fastergithub.util;

import com.sololn.fastergithub.Starter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName getIP
 * @Description TODO
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
    
    public static String sendPost(String url , String params  , String formData) throws Exception{

        StringBuilder builder = new StringBuilder();

        if(!(params == null || params.length() == 0) ){
            url += ("?" + params );
        }

        URL Url = new URL(url );
        URLConnection conn = Url.openConnection();

        //如果设置代理 , 和发送GET一样.
        conn.setRequestProperty("accept", "*/*" );
        conn.setRequestProperty("Connection", "Keep-Alive" );
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");

        //设置之后就可以发送POST请求了
        conn.setDoInput(true );
        conn.setDoOutput(true );


        //获取它的输出流 , 直接写入post请求
        PrintWriter writer = new PrintWriter(conn.getOutputStream() );
        writer.print(formData);
        writer.flush();


        //获取浏览器的返回数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream() ) );
        String line = reader.readLine();
        line = new String(line.getBytes() , "utf-8" );  //解决乱码的问题
        while(line != null ){
            System.out.println(line );
            builder.append(line + "\r\n" );
            line = reader.readLine();
        }
        reader.close();
        writer.close();


        return builder.toString();
    }
    
    public static String sendGet(String url) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = "https://myssl.com/api/v1/tools/dns_query?qtype=1&host=" + url + "&qmode=-1";
            logger.info("url is {}",urlNameString);
            SkipSSL();
            URL realUrl = new URL(urlNameString);
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

    

    public static String getIpFromJson(String url){
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
            /*String ip = Inet4Address.getIpFromStr(response);
            if (null == ip){
                continue;
            }*/
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

}
