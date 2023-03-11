package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.impl.fool.FoolPathway;
import de.firecreeper82.pathways.impl.fool.abilities.SpiritBodyThreads;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ThreadLengthCmd implements CommandExecutor {

    @Override
    //Command for the Player to choose how far he can see Spirit Body Threads
    //Only works for Beyonders of the Fool Pathway
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!(s instanceof Player)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        if(args.length != 1 || !isInteger(args[0])) {
            s.sendMessage("§cWrong usage: Use /thread-length <length>");
            return true;
        }

        Player p = (Player) s;

        if(!Plugin.beyonders.containsKey(p.getUniqueId())) {
            s.sendMessage("§cYou have to be a Beyonder to use this command!");
            return true;
        }

        if(!(Plugin.beyonders.get(p.getUniqueId()).getPathway() instanceof FoolPathway)) {
            s.sendMessage("§cYou have to be a Beyonder of the Fool Pathway to use this command!");
            return true;
        }

        SpiritBodyThreads spiritBodyThreads = (SpiritBodyThreads) Plugin.beyonders.get(p.getUniqueId()).getPathway().getSequence().getAbilities().get(6);
        spiritBodyThreads.setPreferredDistance(Integer.parseInt(args[0]));

        p.sendMessage("§aSet the distance for Spirit Body Threads");
        return true;
    }

    private boolean isInteger(String s) {
        try {
            @SuppressWarnings("unused")
            int a = Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
