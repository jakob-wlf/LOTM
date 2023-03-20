package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;


public class BeyonderCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, @NonNull Command cmd, @NonNull String label, @NonNull String[] args) {
        if(!s.isOp()) {
            s.sendMessage("§cYou don't have the permission to use this command!");
            return true;
        }
        if(!(s instanceof Player p)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        if(args.length != 2) {
            s.sendMessage("§cWrong usage: Use /beyonder <Pathway> <Sequence>!");
            return true;
        }

        int sequence;
        try { sequence = Integer.parseInt(args[1]); }
        catch(Exception exc) {
            s.sendMessage("§cWrong usage: Use /beyonder <Pathway> <Sequence>!");
            return true;
        }

        if(sequence > 9 || sequence < 1) {
            s.sendMessage("§cYou can only choose a sequence between 9 and 1!");
            return true;
        }


        //Check if Player is already a Beyonder.
        // If he is, then remove him from the pathway and initialize a new one for the player
        //If he isn't then just initialize a pathway for him
        if(Plugin.beyonders.containsKey(p.getUniqueId())) {
            Plugin.beyonders.get(p.getUniqueId()).removeBeyonder();
            Pathway pathway = Pathway.initializeNew(args[0].toLowerCase(), p.getUniqueId(), sequence);
            if(pathway == null) {
                p.sendMessage("§c" + args[0].toLowerCase() + " is not a valid Pathway! Removing your status as a beyonder");
                return true;
            }
            p.sendMessage(pathway.getStringColor() + "Made you a Beyonder of the " + pathway.getName() + "-pathway at Sequence " + sequence);
            return true;
        }


        Pathway pathway = Pathway.initializeNew(args[0].toLowerCase(), p.getUniqueId(), sequence);
        if(pathway == null) {
            p.sendMessage("§c" + args[0].toLowerCase() + " is not a valid Pathway");
            return true;
        }
        p.sendMessage(pathway.getStringColor() + "Made you a Beyonder of the " + pathway.getName() + "-pathway at Sequence " + sequence);
        return true;
    }
}
