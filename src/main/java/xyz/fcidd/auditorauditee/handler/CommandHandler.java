package xyz.fcidd.auditorauditee.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import xyz.fcidd.auditorauditee.config.LoadConfig;

public class CommandHandler {

    private LoadConfig loadConfig = new LoadConfig();
    private Configuration auditorauditee = loadConfig.getAuditorauditee();

    public void addAA(CommandSender sender, String[] args) {
        // 创建消息
        TextComponent text = new TextComponent();
        // 获取配置文件中，被审核人所对应的列表，为空则获取空列表
        List<String> auditors = new ArrayList<>();
        auditors.addAll((List<String>) auditorauditee.getList(args[2]));
        // 如果第1个子命令是add
        // 追加消息
        text.addExtra("已将玩家");
        if (!args[2].equals(sender.getName())) {
            // 将玩家输入的审核员添加到列表中
            boolean b = false;
            for (int i = 4; i < args.length; i++) {
                if (!args[i].equals("") && !auditors.contains(args[i]) && !args[i].equals(args[2])
                        && auditorauditee.getList(args[i]).isEmpty()) {
                    // 判断，玩家输入的不是空字符串、源列表不包含新输入的内容、审核人不能是被审核人！
                    auditors.add(args[i]);
                    // 追加消息
                    text.addExtra((i == 4) ? args[i] : "、" + args[i]);
                    b = true;
                }
            }
            // 发送消息
            if (b) {
                // 保存配置文件
                auditorauditee.set(args[2], auditors);
                LoadConfig.saveConfig();
                // 追加消息
                text.addExtra("设为玩家" + args[2] + "的审核员");
                // 发送消息
                sender.sendMessage(text);
            } else {
                // 发送消息
                sender.sendMessage(new ComponentBuilder().append("无变化，你输入的玩家已经是玩家" + args[2] + "的审核员").create());
            }
        } else {
            sender.sendMessage(new ComponentBuilder().append("你不能将自己设为被审核人！").create());
        }
    }

    public void removeAA(CommandSender sender, String[] args) {
        // 创建消息
        TextComponent text = new TextComponent();
        // 获取配置文件中，被审核人所对应的列表，为空则获取空列表
        List<String> auditors = new ArrayList<>();
        auditors.addAll((List<String>) auditorauditee.getList(args[2]));
        // 如果第1个子命令是remove
        // 追加消息
        text.addExtra("已将玩家");
        // 将玩家输入的审核员添加到列表中
        boolean b = false;
        for (int i = 4; i < args.length; i++) {
            if (auditors.contains(args[i])) {
                // 判断，被删除的审核人是否包含在列表中
                auditors.remove(args[i]);
                // 追加消息
                text.addExtra((i == 4) ? args[i] : "、" + args[i]);
                b = true;
            }
        }
        // 发送消息
        if (b) {
            if (auditors.isEmpty()) {
                Collection<String> keys = auditorauditee.getKeys();
                Map<String, List<String>> aaMap = new HashMap<>();
                for (String key : keys) {
                    if (!key.equals(args[2])) {
                        auditors = (List<String>) auditorauditee.getList(key);
                        aaMap.put(key, auditors);
                    }
                }
                LoadConfig.getConfiguration().set("AuditorAuditee", aaMap);
                sender.sendMessage(new ComponentBuilder().append("已将" + args[2] + "从被审核人列表中删除").create());
            } else {
                // 保存配置文件
                auditorauditee.set(args[2], auditors);
                // 追加消息
                text.addExtra("从玩家" + args[2] + "的审核员列表中移除");
                // 发送消息
                sender.sendMessage(text);
            }
            LoadConfig.saveConfig();
        } else {
            // 发送消息
            sender.sendMessage(new ComponentBuilder().append("无变化，你输入的玩家本不是玩家" + args[2] + "的审核员").create());
        }
    }

    public void removeAll(CommandSender sender, String[] args) {
        Collection<String> keys = auditorauditee.getKeys();
        Map<String, List<String>> aaMap = new HashMap<>();
        List<String> auditors = new ArrayList<>();
        for (String key : keys) {
            if (!key.equals(args[2])) {
                auditors = (List<String>) auditorauditee.getList(key);
                aaMap.put(key, auditors);
            }
        }
        LoadConfig.getConfiguration().set("AuditorAuditee", aaMap);
        LoadConfig.saveConfig();
        sender.sendMessage(new ComponentBuilder().append("已将" + args[2] + "从被审核人列表中删除").create());
    }

    public void list(CommandSender sender) {
        // 创建消息
        TextComponent text = new TextComponent();
        Collection<String> keys = auditorauditee.getKeys();
        List<String> values = new ArrayList<>();
        String[] senderStrings = sender.toString().split("@");
        boolean b = true;
        boolean isConsole = senderStrings[0].equals("net.md_5.bungee.command.ConsoleCommandSender");
        for (String key : keys) {
            text.addExtra(isConsole ? "\n" + key : (b ? key : "\n" + key));
            values = (List<String>) auditorauditee.getList(key);
            for (String value : values) {
                text.addExtra("\n- " + value);
            }
            b = false;
        }
        // 发送消息
        if (!b) {
            sender.sendMessage(text);
        }
    }
}
