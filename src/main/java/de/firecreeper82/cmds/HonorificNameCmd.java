package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Arrays;
import java.util.UUID;

public class HonorificNameCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command cmd, @NonNull String label, @NonNull String[] @NonNull args) {
        if(!(s instanceof Player p)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        //check if the cmd sender is a player
        if(!Plugin.beyonders.containsKey(p.getUniqueId())) {
            s.sendMessage("§cYou have to be a Beyonder to use this command!");
            return true;
        }
        else{
             Plugin.beyonders.get(p.getUniqueId());
        }
        String new_name = Arrays.toString(args);
        //then check if the player is a beyonder. If not, stop the command.
        if (new_name.equals("change")){

            s.sendMessage(Plugin.getHonorific_name(p));
            s.sendMessage("is your current name. Changed it to:");
            s.sendMessage(new_name+"!");
            Plugin.setHonorific_Name(p, new_name);
        }




        return false;
    }
}