package xyz.fcidd.auditorauditee.config;

import lombok.Data;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Data
public class LoadConfig {
    private Configuration auditorauditee = configuration.getSection("AuditorAuditee");

    private static Configuration configuration;

    /**
     * 读取配置文件
     */
    public static Configuration loadConfig() {
        try {
            LoadConfig.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File("./plugins/AuditorAuditee/config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LoadConfig.configuration;
    }

    public static Configuration getConfiguration() {
        return LoadConfig.configuration;
    }

    public static void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(LoadConfig.configuration,
                    new File("./plugins/AuditorAuditee/config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}