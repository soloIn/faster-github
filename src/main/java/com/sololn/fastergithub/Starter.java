package com.sololn.fastergithub;

import com.sololn.fastergithub.util.IpUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        IpUtil.readIpMap();
        /*Map<String, String> ips = Inet4Address.getIps();
        HostUtil.buildContent(ips);*/
    }
}
