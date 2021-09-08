package com.sololn.fastergithub.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ShellUtil
 * @Description TODO
 * @Author HeGuojian
 * @Date 2021/9/8 16:13
 * @Version 1.0
 **/
public class ShellUtil {
    public static boolean pingAddress(String cmd) throws IOException {

        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
            String readLine = br.readLine();
            while (readLine != null) {
                if (readLine.contains("超时")){
                    return false;
                }
                readLine = br.readLine();
            }
            p.waitFor(1,TimeUnit.SECONDS);
            int i = p.exitValue();
            if (i == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e ) {
            throw e;
        } catch (InterruptedException e){
            p.destroy();
        } finally {
            if (br != null) {
                br.close();
            }
            p.destroy();
        }
        return  false;
    }

}
