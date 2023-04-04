package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class SpawnCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command cmd, @NonNull String label, @NonNull String[] args) {
        if(!s.isOp()) {
            s.sendMessage("§cYou don't have the permission to use this command!");
            return true;
        }
        if(!(s instanceof Player p)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        if(args.length != 1) {
            s.sendMessage("§cWrong usage: Use /spawn <ID>!");
            return true;
        }

        if(!Plugin.instance.getBeyonderMobsHandler().spawnEntity(args[0], p.getLocation(), p.getWorld()))
            p.sendMessage("§cThere is no mob with the id: " + args[0]);

        return true;
    }
}
