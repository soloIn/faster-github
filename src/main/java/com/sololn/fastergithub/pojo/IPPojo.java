package com.sololn.fastergithub.pojo;

/**
 * @ClassName pojo
 * @Description TODO
 * @Author HeGuojian
 * @Date 2021/9/8 15:53
 * @Version 1.0
 **/
public class IPPojo {
    private String ip;
    private String location;
    private boolean effective = false;

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
