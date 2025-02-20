package com.hinaplugin.createTimer;

import com.google.common.collect.Maps;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class CreateTimer extends JavaPlugin {

    public static CreateTimer plugin;
    public static Map<String, Timer> timerMap = Maps.newHashMap();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        /*
          コマンドをプラグインに登録
         */
        final PluginCommand command = this.getCommand("timer");
        if (command != null){
            command.setExecutor(new Commands());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        /*
          プラグイン終了時に動作しているタイマーを停止
         */
        for (Timer timer : timerMap.values()) {
            timer.stop();
        }
        /*
          上のforはこのような書き方もできます
          timerMap.values().forEach(Timer::stop);
         */
    }
}
