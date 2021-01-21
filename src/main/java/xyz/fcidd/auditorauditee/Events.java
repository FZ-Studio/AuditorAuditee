package xyz.fcidd.auditorauditee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import xyz.fcidd.auditorauditee.config.LoadConfig;

public class Events implements Listener {
    @EventHandler
    public void onLogin(LoginEvent event) {
        LoadConfig loadConfig = new LoadConfig();
        Configuration auditorauditee = loadConfig.getAuditorauditee();
        String playerName = event.getConnection().getName();
        //在线玩家列表
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        List<String> players = new ArrayList<>();
        for (ProxiedPlayer networkPlayer : networkPlayers) {
            players.add(networkPlayer.getName());
        }
        //如果上线的是被审核人
        if (auditorauditee.getKeys().contains(playerName)) {
            //审核员列表
            List<String> auditors = (List<String>) auditorauditee.getList(playerName);
            boolean b = false;
            //玩家列表是否有他的审核员
            for (String auditor : auditors) {
                if (players.contains(auditor)) {
                    b = true;
                    break;
                }
            }
            //如果没有
            if (!b) {
                event.setCancelReason(new ComponentBuilder().append("你的审核员不在线").create());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLogout(PlayerDisconnectEvent event) {
        LoadConfig loadConfig = new LoadConfig();
        Configuration auditorauditee = loadConfig.getAuditorauditee();
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        List<String> auditors = new ArrayList<>();
        boolean b;
        String player;
        for (ProxiedPlayer networkPlayer : networkPlayers) {
            // 判断游戏内有没有被监管人，遍历在线列表
            player = networkPlayer.getName();
            if (auditorauditee.getKeys().contains(player)) {
                // 查表
                auditors.addAll((List<String>) auditorauditee.getList(player));
                auditors.remove(event.getPlayer().getName());
                b = false;
                // 如果有审核员在线则不踢出
                for (ProxiedPlayer networkPlayer2 : networkPlayers) {
                    if (auditors.contains(networkPlayer2.getName())) {
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    networkPlayer.disconnect(new ComponentBuilder().append("你的审核员已下线").create());
                }
            }
        }
    }
}
