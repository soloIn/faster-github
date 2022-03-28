package com.sololn.fastergithub.util;

/**
 * TODO
 *
 * @author guojian
 **/
public class StringUtil {
    public static boolean blank(String str) {
        return str == null ||  "".equals(str);
    }
    public static boolean notBlank(String str) {
        return str != null && !"".equals(str);
    }
}
