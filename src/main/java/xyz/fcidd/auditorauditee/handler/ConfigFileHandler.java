package xyz.fcidd.auditorauditee.handler;

import net.md_5.bungee.config.Configuration;
import xyz.fcidd.auditorauditee.config.LoadConfig;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileHandler {
    public static void create() throws IOException {
        // 目标文件夹
        File file = new File("./plugins/AuditorAuditee/");
        File file2 = new File("./plugins/AuditorAuditee/config.yml");
        // 如果文件不存在
        if (!file2.exists()) {
            // 创建文件夹
            file.mkdirs();
            // 将文件创建到/plugins/AuditorAuditee/config.yml
            file2.createNewFile();
            Configuration configuration = LoadConfig.loadConfig();
            Map mp = new HashMap<>();
            mp.put("被审核人", new String[]{"审核员1", "审核员2"});
            configuration.set("AuditorAuditee", mp);
            LoadConfig.saveConfig();
        } else {
            Configuration configuration = LoadConfig.loadConfig();
            Collection<String> keys = configuration.getKeys();
            if (!keys.contains("AuditorAuditee")) {
                Map mp = new HashMap<>();
                mp.put("被审核人", new String[]{"审核员1", "审核员2"});
                configuration.set("AuditorAuditee", mp);
                LoadConfig.saveConfig();
            }
        }
    }
}