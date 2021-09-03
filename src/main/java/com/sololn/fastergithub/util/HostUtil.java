package com.sololn.fastergithub.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HostUtil
 * @Description TODO
 * @Author HeGuojian
 * @Date 2021/8/30 17:14
 * @Version 1.0
 **/
public class HostUtil {
    private static final  Path outPath = Paths.get("C:", "myProgram", "fast-github", "hosts");

    public static void buildContent(Map<String, String> ips) throws IOException {
        //C:\Windows\System32\drivers\etc
        Path inPath = Paths.get("C:", "Windows", "System32", "drivers", "etc", "hosts");
        List<String> content = new ArrayList<>();
        /*try(Stream<String> lines = Files.lines(inPath)){
            content = lines.collect(Collectors.toList());
        } catch (IOException e){
            e.printStackTrace();
        }
        if (null ==content){
            return;
        }*/
        // 删除 hosts 中关于 github 的配置
        /*boolean inside = false;
        Iterator<String> iterator = content.iterator();
        while (iterator.hasNext()){
            if (iterator.next().startsWith("#github")){
                inside = true;
                iterator.remove();
                continue;
            }
            if (iterator.next().startsWith("#update")){
                inside =false;
                iterator.remove();
                continue;
            }
            if (inside){
                iterator.remove();
                continue;
            } else {
                continue;
            }
        }*/
        // 添加 github 的配置
        List<String> contentTmp = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        time.format(DateTimeFormatter.ISO_DATE_TIME);
        contentTmp.add("#github 配置#");
        ips.forEach((key, value) -> {
            StringBuffer tap = new StringBuffer();
            int num = 30 - value.length();
            while (num > 0){
                tap.append(" ");
                num --;
            }
            contentTmp.add(value + tap + key );
        });
        contentTmp.add("#update by " + time + " #");
        content.addAll(contentTmp);
        write(content);
    }
    public static void write(List<String> content) throws IOException {
        File outDir = outPath.toFile();
        if (!outDir.exists()){
            outDir.mkdirs();
        }
        File  hosts = new File(outDir.toString() +File.separator +  "hosts");
        if (!hosts.exists()){
            hosts.createNewFile();
        }
        Files.write(hosts.toPath(),content);
    }

    private static void unlockInWin(File file) {
        String cmd = "C:\\myProgram\\fast-github\\cmd\\hostWritable";
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
