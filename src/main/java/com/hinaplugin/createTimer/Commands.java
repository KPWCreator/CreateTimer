package com.hinaplugin.createTimer;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        /*
          色をつけるためのメソッド
         */
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        /*
          引数が1個以下であれば処理終了
         */
        if (strings.length <= 1){
            commandSender.sendMessage(miniMessage.deserialize("<red>usage: /timer create <name> <time>"));
            commandSender.sendMessage(miniMessage.deserialize("<red>usage: /timer delete <name>"));
            return true;
        }

        /*
          引数を取り出す
         */
        final String mode = strings[0];
        final String name = strings[1];

        /*
          引数が2個の時の処理
         */
        if (strings.length == 2){
            /*
              1個めの引数がdeleteか判定する.
             */
            if (mode.equalsIgnoreCase("delete")){
                /*
                  作成したタイマーに指定した名前が含まれていたら削除
                 */
                for (String timerName : CreateTimer.timerMap.keySet()){
                    if (timerName.equalsIgnoreCase(name)){
                        Timer timer = CreateTimer.timerMap.get(name);
                        timer.stop();
                        commandSender.sendMessage(miniMessage.deserialize("<green>" + name + "を停止しました．"));
                        return true;
                    }
                }

                /*
                  名前が含まれていなかったら削除失敗のメッセージを送信
                 */
                commandSender.sendMessage(miniMessage.deserialize("<red>" + name + "という名前のタイマーが見つかりませんでした．"));
            }else {
                /*
                  delete以外が指定されたときの処理
                 */
                commandSender.sendMessage(miniMessage.deserialize("<red>usage: /timer create <name> <time>"));
                commandSender.sendMessage(miniMessage.deserialize("<red>usage: /timer delete <name>"));
            }
            return true;
        }

        /*
          引数が3個の時の処理
         */
        if (strings.length == 3){
            /*
              1個めの引数がcreateか判定する
             */
            if (mode.equalsIgnoreCase("create")){
                /*
                  作成しようとしている名前のボスバーが既にあるか判定
                 */
                if (Bukkit.getServer().getBossBar(new NamespacedKey("minecraft", name)) != null){
                    /*
                      既に存在する場合の処理
                     */
                    commandSender.sendMessage(miniMessage.deserialize("<red>" + name + "という名前のボスバーが既に存在します．"));
                    return true;
                }

                /*
                  3個めの引数が数字かどうか判定
                 */
                String timeText = strings[2];
                if (timeText.chars().allMatch(Character::isDigit)){
                    /*
                      文字を数字に変換
                     */
                    final int time = Integer.parseInt(timeText);

                    /*
                      タイマーの作成
                     */
                    final Timer timer = new Timer(name, time);
                    timer.start();
                    CreateTimer.timerMap.put(name, timer);
                    commandSender.sendMessage(miniMessage.deserialize("<green>" + time + "秒のタイマー: [" + name + "]を開始しました．"));
                }else {
                    /*
                      数字ではなかった時の処理
                     */
                    commandSender.sendMessage(miniMessage.deserialize("<red>usage: /timer create <name> <time>"));
                }
            }else {
                /*
                  create以外が指定されたときの処理
                 */
                commandSender.sendMessage(miniMessage.deserialize("<red>usage: /timer create <name> <time>"));
                commandSender.sendMessage(miniMessage.deserialize("<red>usage: /timer delete <name>"));
            }
        }
        return true;
    }
}
