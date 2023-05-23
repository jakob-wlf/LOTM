package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.impl.fool.FoolPathway;
import de.firecreeper82.pathways.impl.fool.abilities.SpiritBodyThreads;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public class DisableThreadsCmd implements CommandExecutor {

    @Override
    //Command for the Player to choose which Spirit Body Threads he can see
    //Only works for Beyonders of the Fool Pathway
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        if (args.length != 1) {
            s.sendMessage("§cWrong usage: Use /disable-threads <Normal | Player | Undead | Arthropod | Illager>");
            return true;
        }

        Player p = (Player) s;

        if (!Plugin.beyonders.containsKey(p.getUniqueId())) {
            s.sendMessage("§cYou have to be a Beyonder to use this command!");
            return true;
        }

        if (!(Plugin.beyonders.get(p.getUniqueId()).getPathway() instanceof FoolPathway)) {
            s.sendMessage("§cYou have to be a Beyonder of the Fool Pathway to use this command!");
            return true;
        }

        String[] validArgs = {"normal", "player", "undead", "arthropod", "illager"};

        if (!Arrays.asList(validArgs).contains(args[0].toLowerCase())) {
            s.sendMessage("§cWrong usage: Use /disable-threads <Normal | Player | Undead | Arthropod | Illager>");
            return true;
        }

        SpiritBodyThreads spiritBodyThreads = (SpiritBodyThreads) Plugin.beyonders.get(p.getUniqueId()).getPathway().getSequence().getAbilities().get(6);
        if (spiritBodyThreads.disableCategory(args[0].toLowerCase())) {
            p.sendMessage("§aDisabled the category " + args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase() + "!");
            p.sendMessage("§aUse the same command to enable the Spirit Body Thread again");
        } else {
            p.sendMessage("§aEnabled the category " + args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase() + "!");
        }
        return true;
    }
}
