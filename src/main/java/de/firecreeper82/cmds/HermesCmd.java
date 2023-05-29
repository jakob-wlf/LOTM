/*
Quick explanation;
the /hermes command will have to be used AFTER using /pray.
It will serve as the var in which the message will be stored.
The target of the /pray will receive all args related to the next /hermes

Settings for /hermes:
 <pray>: you will send a message to the last prayedTo entity.
 <talk>: you will use hermes. This feature will be important for pathways which will be developed later on.
*/
package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static de.firecreeper82.lotm.Plugin.honorific_name;
import static de.firecreeper82.lotm.Plugin.honorific_name_keys;

public class HermesCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command cmd, @NonNull String label, @NonNull String[] args) {
        String[] cmd_args_array;
        String cmd_args_string;
        cmd_args_array = ArrayUtils.remove(args, 0);
        cmd_args_string = Arrays.toString(cmd_args_array);
        cmd_args_string = cmd_args_string.toLowercase()
        //creates a var which contains all arguments, but excludes the first one.
        //E.G: args=["help", "hello", "world"]
        //Here, cmd_args=["hello", "world"].

        if (!(s instanceof Player p)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        //check if the cmd sender is a player
        if (!Plugin.beyonders.containsKey(p.getUniqueId())) {
            s.sendMessage("§cYou have to be a Beyonder to use this command!");
            return true;
        }
        switch (args[1]) {

            case "pray_to":
                //ArrayUtils.remove(args, 1);

                String EntityPrayed = Arrays.toString(cmd_args_array);
                Plugin.setLastPrayedTo(p, EntityPrayed);
                s.sendMessage("§dYou have prayed to: " + EntityPrayed);
                break;
            case "set_name":
                //if (true){} the "CheckAngel" function will be created soon? Anyway, for now, everyone's got an Honorific Name ;) enjoy it while it lasts~
                s.sendMessage(Plugin.getHonorific_name(p));
                s.sendMessage("is your current name. Changed it to:");
                s.sendMessage(cmd_args_string + "!");
                Plugin.setHonorific_Name(p, cmd_args_string);
                break;
            case "send_prayer":
                String lastPrayedTo = Plugin.getLastPrayedTo(p);
                Plugin.sendPrayer(cmd_args_string, p.getUniqueId(), Objects.requireNonNull(Plugin.getKeyByValue(honorific_name, lastPrayedTo)));
                break;
            case "hermes":
                s.sendMessage("§cThis function has yet to be implemented !");
            case "get_all_names":
                if (!(s.isOp())) {
                    return true;
                } else {
                    for (int i = 0; i != ArrayUtils.getLength(honorific_name_keys); i++) {
                        UUID honorific_name_id = honorific_name_keys.get(i);
                        p.sendMessage(honorific_name.get(honorific_name_id));
                    }

                }
                break;
            case "reset_prayed_to":
                Plugin.resetLastPrayedTo(p);
                break;
            case "help":
            default:
                Plugin.getHermesHelp(p);


        }
        return false;
    }
}
