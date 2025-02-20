package com.hinaplugin.createTimer;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {

    private final String name;
    private final int time;
    private int now;
    private BukkitRunnable runnable;
    private BossBar bossBar;
    private NamespacedKey key;

    /**
     * @param name
     *        タイマーの名前
     * @param time
     *        タイマーの時間
     */
    public Timer(String name, int time){
        this.name = name;
        this.time = time;
        this.now = time;
    }

    /**
     * タイマーを開始する
     */
    public void start(){
        key = new NamespacedKey("minecraft", name);
        bossBar = Bukkit.createBossBar(key, "", BarColor.GREEN, BarStyle.SOLID);
        bossBar.setVisible(true);

        for (final Player player : Bukkit.getOnlinePlayers()){
            bossBar.addPlayer(player);
        }

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (now < 0){
                    stop();
                    return;
                }
                bossBar.setProgress(now / (double) time);
                bossBar.setTitle("残り: " + translate(now) + "です.");
                now--;
            }
        };

        runnable.runTaskTimer(CreateTimer.plugin, 0L, 20L);
    }

    /**
     * タイマーを停止する
     */
    public void stop(){
        /*
          タイマー終了時にメッセージを送信
         */
        for (Player player : bossBar.getPlayers()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>" + name + "が終了しました!"));
        }

        /*
          タイマーからプレイヤーを削除して非表示に
         */
        bossBar.removeAll();
        Bukkit.removeBossBar(key);
        CreateTimer.timerMap.remove(name);
        runnable.cancel();
    }

    /**
     * @param time
     *        現在の残り時間
     * @return 表示用の時刻表記に修正した時間
     */
    private String translate(int time){
        if (time > 3600){
            int hour = time / 3600;
            int minute = (time % 3600) / 60;
            int second = time % 60;
            return String.format("%02d時間%02d分%02d秒", hour, minute, second);
        }else if (time > 60){
            int minute = time / 60;
            int second = time % 60;
            return String.format("%02d分%02d秒", minute, second);
        }else {
            return String.format("%02d秒", time);
        }
    }
}
