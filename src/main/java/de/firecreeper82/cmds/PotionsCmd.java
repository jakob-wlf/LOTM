package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import de.firecreeper82.pathways.impl.fool.FoolPotions;
import de.firecreeper82.pathways.impl.sun.SunPotions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PotionsCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, @NonNull Command cmd, @NonNull String label, String[] args) {
        if (!s.isOp()) {
            s.sendMessage("§cYou don't have the permission to use this command!");
            return true;
        }
        if (!(s instanceof Player p)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        if (args.length != 1) {
            s.sendMessage("§cWrong usage: Use /potions <Pathway>!");
            return true;
        }

        if (!Pathway.isValidPathway(args[0])) {
            p.sendMessage("§c" + args[0] + " is not a valid Pathway!");
            return true;
        }

        switch (args[0]) {
            case "sun" -> {
                for (Potion potion : Plugin.instance.getPotions()) {
                    if (potion instanceof SunPotions) {
                        for (int i = 1; i < 10; i++) {
                            p.getInventory().addItem(potion.returnPotionForSequence(i));
                        }
                    }
                }
            }

            case "fool" -> {
                for (Potion potion : Plugin.instance.getPotions()) {
                    if (potion instanceof FoolPotions) {
                        for (int i = 1; i < 10; i++) {
                            p.getInventory().addItem(potion.returnPotionForSequence(i));
                        }
                    }
                }
            }

            default -> p.sendMessage("§c" + args[0] + " is not a valid Pathway!");
        }

        return true;
    }
}
