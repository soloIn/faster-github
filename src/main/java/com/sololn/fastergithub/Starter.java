package com.sololn.fastergithub;

import com.sololn.fastergithub.util.HostUtil;
import com.sololn.fastergithub.util.IpUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName starter
 * @Description springboot 启动时执行
 * @Author HeGuojian
 * @Date 2021/8/30 17:02
 * @Version 1.0
 **/
@Component
public class Starter implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Map<String, String> stringStringMap = IpUtil.readIpMap("https://myssl.com/api/v1/tools/dns_query?qtype=1&host=", "qmode=-1");
        HostUtil.buildContent(stringStringMap);
        /*Map<String, String> ips = Inet4Address.getIps();
        HostUtil.buildContent(ips);*/
    }
}
