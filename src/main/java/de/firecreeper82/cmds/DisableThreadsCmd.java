package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DisableThreadsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!(s instanceof Player)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        if(args.length != 1) {
            s.sendMessage("§cWrong usage: Use /beyonder <Pathway> <Sequence>!");
            return true;
        }

        Player p = (Player) s;

        if(Plugin.beyonders.containsKey(p.getUniqueId())) {

        }
        return true;
    }
}
