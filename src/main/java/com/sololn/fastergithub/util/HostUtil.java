package com.sololn.fastergithub.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName HostUtil
 * @Author HeGuojian
 * @Date 2021/8/30 17:14
 * @Version 1.0
 **/
public class HostUtil {
    
    public static void toFile(Map<String, String> stringStringMap, Path path) throws IOException {
        write(buildContent(stringStringMap), path);
    }
    private static List<String> buildContent(Map<String, String> ips) {
        //C:\Windows\System32\drivers\etc
        Path inPath = Paths.get("C:", "Windows", "System32", "drivers", "etc", "hosts");
        List<String> content = new ArrayList<>();
        Map<String,String> contentGithub = new HashMap<>();

        try(Stream<String> lines = Files.lines(inPath)){
            content = lines.collect(Collectors.toList());
        } catch (IOException e){
            e.printStackTrace();
        }
        // 删除 hosts 中关于 github 的配置
        boolean inside = false;
        Iterator<String> iterator = content.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if (next.trim().startsWith("#github-faster")){
                inside = true;
                iterator.remove();
                continue;
            }
            if (next.trim().startsWith("#update by github-faster")){
                inside =false;
                iterator.remove();
                continue;
            }
            if (inside){
                if (!StringUtils.isBlank(next)){
                    String[] split = next.trim().split("\\s+");
                    contentGithub.put(split[1],split[0]);
                    iterator.remove();
                }
            }
            continue;
        }
        /*
        * 用ips的值覆盖hosts的值
        * */
        ips.forEach((key, value) -> {
            contentGithub.put(key, value);
        });
        List<String> contentStr = new ArrayList<>();
        // 添加 github 的配置
        contentStr.add("#github-faster 配置 #");
        // map 转 string
        contentGithub.forEach((key, value) -> {
            StringBuffer tap = new StringBuffer();
            int num = 30 - value.length();
            while (num > 0){
                tap.append(" ");
                num --;
            }
            contentStr.add(value + tap + key);
        });
        LocalDateTime time = LocalDateTime.now();
        time.format(DateTimeFormatter.ISO_DATE_TIME);
        contentStr.add("#update by github-faster " + time + " #");
        content.addAll(contentStr);
        return content;
    }

    private static void openFolder(Path path) {
        try {
            String[] cmd = new String[5];
            cmd[0] = "cmd";
            cmd[1] = "/c";
            cmd[2] = "start";
            cmd[3] = " ";
            cmd[4] = path.toString();
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(List<String> content, Path path) throws IOException {
        File outDir = path.toFile();
        if (!outDir.exists()){
            outDir.mkdirs();
        }
        File  hosts = new File(outDir.toString() +File.separator +  "hosts");
        if (!hosts.exists()){
            hosts.createNewFile();
        }
        Files.write(hosts.toPath(),content);
        openFolder(path);
    }
    
    
   
    /* private static void unlockInWin(File file) {
         String cmd = "C:\\myProgram\\fast-github\\cmd\\hostWritable";
         try {
             Process p = Runtime.getRuntime().exec(cmd);
             p.waitFor();
         } catch (IOException | InterruptedException e) {
             throw new RuntimeException(e);
         }
     }*/
}
