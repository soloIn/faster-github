package com.sololn.fastergithub.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sololn.fastergithub.pojo.IPPojo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @author guojian
 **/
@Slf4j
public class JsonUtil {
    /*
     * 从 json 获取 ip 数据
     * @param null
     * @return ip
     * @author guojian
     * @date 2021/9/8
     * @throws
     **/
    public static String getIpFromJson(String line) throws IOException {
        List<IPPojo> ipPojos = new ArrayList<>();
        JSONObject object = JSON.parseObject(line);
        JSONObject data = (JSONObject) object.get("data");
        for(Map.Entry<String,Object> o : data.entrySet()){
            JSONArray value = (JSONArray) o.getValue();
            JSONObject o1 = (JSONObject)value.get(0);
            JSONObject answer = (JSONObject) o1.get("answer");
            if ("0.00".equals(answer.get("time_consume"))){
                continue;
            }
            JSONArray records = (JSONArray)answer.get("records");
            if (null == records){
                continue;
            }
            JSONObject o2 = (JSONObject) records.get(0);
            
            IPPojo ipPojo = new IPPojo();
            ipPojo.setIp((String) o2.get("value"));
            ipPojo.setLocation((String) o2.get("ip_location"));
            ipPojos.add(ipPojo);
        }
        // 过滤不能 ping 通的 ip
        for (IPPojo ipPojo:ipPojos){
            String cmd = "ping -n 2 " + ipPojo.getIp();
            if (ShellUtil.pingAddress(cmd)){
                return ipPojo.getIp();
               //ipPojo.setEffective(true);
            }
        }
        List<IPPojo> ips = ipPojos.stream().filter(IPPojo::isEffective).collect(Collectors.toList());
        
        for(IPPojo ip: ips){
            if (ip.getLocation().contains("新加坡")){
                // 优先选择新加坡
                return ip.getIp();
            }
        }
        if (ips.isEmpty()){
            return "";
        }
        return ips.get(0).getIp();
    }
}
