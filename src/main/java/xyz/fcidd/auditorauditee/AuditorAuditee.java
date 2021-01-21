package xyz.fcidd.auditorauditee;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.auditorauditee.command.Aa;
import xyz.fcidd.auditorauditee.handler.ConfigFileHandler;

public class AuditorAuditee extends Plugin {
    @SneakyThrows
    @Override
    public void onEnable() {
        // 将配置文件输出到/plugins/BanCommandPlus
        ConfigFileHandler.create();
        // 注册指令
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Aa());
        // 监听器
        getProxy().getPluginManager().registerListener(this, new Events());
        
        System.out.println("§2[AuditorAuditee]已经加载");
    }

    @SneakyThrows
    public void onDisable() {
        
    }
}