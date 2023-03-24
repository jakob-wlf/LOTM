package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.impl.fool.FoolPathway;
import de.firecreeper82.pathways.impl.fool.abilities.SpiritBodyThreads;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ExcludeEntityCmd implements CommandExecutor {

    @Override
    //Command for the Player to choose which Spirit Body Threads he can see
    //Only works for Beyonders of the Fool Pathway
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!(s instanceof Player p)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        if (args.length != 1) {
            s.sendMessage("§cWrong usage: Use /exclude-entity <Entity>");
            return true;
        }

        if (!Plugin.beyonders.containsKey(p.getUniqueId())) {
            s.sendMessage("§cYou have to be a Beyonder to use this command!");
            return true;
        }

        if (!(Plugin.beyonders.get(p.getUniqueId()).getPathway() instanceof FoolPathway)) {
            s.sendMessage("§cYou have to be a Beyonder of the Fool Pathway to use this command!");
            return true;
        }

        EntityType entityType = null;

        for(EntityType type : EntityType.values()) {
            if(type.name().equalsIgnoreCase(args[0])) {
                entityType = type;
                break;
            }
        }

        if(entityType == null) {
            p.sendMessage("§c" + args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase() + " is not a valid entity! If you want to cancel the divination, type \"cancel\"");
            s.sendMessage("§cUse /exclude-entity <Entity>");
            return true;
        }

        SpiritBodyThreads spiritBodyThreads = (SpiritBodyThreads) Plugin.beyonders.get(p.getUniqueId()).getPathway().getSequence().getAbilities().get(6);
        if(spiritBodyThreads.addExcludedEntity(entityType)) {
            p.sendMessage("§aDisabled the entity " + entityType.name().substring(0, 1).toUpperCase() + entityType.name().substring(1).toLowerCase() + "!");
            p.sendMessage("§aUse the same command to enable the Spirit Body Thread again");
        }
        else {
            p.sendMessage("§aEnabled the entity " + entityType.name().substring(0, 1).toUpperCase() + entityType.name().substring(1).toLowerCase() + "!");
        }

        return true;
    }
}
