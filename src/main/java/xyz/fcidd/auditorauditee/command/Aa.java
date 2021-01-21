package xyz.fcidd.auditorauditee.command;

import lombok.SneakyThrows;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;
import xyz.fcidd.auditorauditee.config.LoadConfig;
import xyz.fcidd.auditorauditee.handler.CommandHandler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Aa extends Command implements TabExecutor {

    public Aa() {
        super("aa");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        // 创建命令推荐列表
        List<String> list = new LinkedList<>();
        // 在玩家输入内容长度为1时
        if (args.length == 0) {
            // 主命令
            list.add("aa");
        } else if (args.length == 1) {
            // 第1个子命令
            // aa add auditee 受审核人 auditor 审核方
            list.add("add");
            // aa remove auditee 受审核人 auditor 审核方
            list.add("remove");
            // 暂时没写
            list.add("list");
        } else if (args.length == 2 && (args[0].equals("add") || args[0].equals("remove"))) {
            // 第2个子命令
            list.add("auditee");
        } else if (args.length == 3 && args[1].equals("auditee")) {
            // 第3个子命令
            if (args[0].equals("add")) {
                // 如果第1个子命令是add则推荐玩家列表
                Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
                for (ProxiedPlayer networkPlayer : networkPlayers) {
                    list.add(networkPlayer.getName());
                }
            } else if (args[0].equals("remove")) {
                // 如果第1个子命令是remove则推荐配置文件中的受审核人的列表
                LoadConfig loadConfig = new LoadConfig();
                Collection<String> keys = loadConfig.getAuditorauditee().getKeys();
                for (String key : keys) {
                    list.add(key);
                }
            }
        } else if (args.length == 4 && (args[0].equals("add") || args[0].equals("remove"))) {
            // 第4个子命令
            list.add("auditor");
        } else if (args.length >= 5 && args[1].equals("auditee") && args[3].equals("auditor")) {
            // 其余子命令
            if (args[0].equals("add")) {
                // 如果第1个子命令是add则推荐玩家列表
                Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
                for (ProxiedPlayer networkPlayer : networkPlayers) {
                    list.add(networkPlayer.getName());
                }
                // 从该列表中移除被审核人
                list.remove(args[2]);
                // 从该列表中移除已输入的审核人
                for (int i = 4; i < args.length; i++) {
                    list.remove(args[i]);
                }
            } else if (args[0].equals("remove")) {
                // 如果第1个子命令是remove则推荐配置文件中的审核人的列表
                LoadConfig loadConfig = new LoadConfig();
                List<String> auditor = (List<String>) loadConfig.getAuditorauditee().getList(args[2]);
                list.addAll(auditor);
                // 从该列表中移除被审核人
                list.remove(args[2]);
                // 从该列表中移除已输入的审核人
                for (int i = 4; i < args.length; i++) {
                    list.remove(args[i]);
                }
            }
        }
        // 返回list即向玩家发送
        return list;
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, String[] args) {
        // 设置权限
        final boolean commandAuth = sender.hasPermission("auditorauditee.command.aa");
        // 载入命令处理模块
        CommandHandler commandHandler = new CommandHandler();
        // 判断权限以及命令长度是否达标、参数是否准确
        if (!commandAuth) {
            sender.sendMessage(new ComponentBuilder().append("你没有使用该命令的权限！").create());
        } else if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder().append("请输入完整命令").create());
        } else if (args.length == 1 && args[0].equals("list")) {
            commandHandler.list(sender);
        } else if (args.length == 3 && args[0].equals("remove") && args[1].equals("auditee")) {
            commandHandler.removeAll(sender, args);
        } else if (args.length >= 5 && args[1].equals("auditee") && args[3].equals("auditor")) {
            if (args[0].equals("add")) {
                commandHandler.addAA(sender, args);
            } else if (args[0].equals("remove")) {
                commandHandler.removeAA(sender, args);
            }
        }
    }
}