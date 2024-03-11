package dev.ua.ikeepcalm.cmds;

import dev.ua.ikeepcalm.Beyonder;
import dev.ua.ikeepcalm.Plugin;
import dev.ua.ikeepcalm.utils.GeneralPurposeUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class AbilityInfoCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command cmd, @NonNull String label, @NonNull String[] args) {
        if (!(s instanceof Player p)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }

        if (!Plugin.beyonders.containsKey(p.getUniqueId())) {
            s.sendMessage("§cYou have to be a Beyonder to use this command!");
            return true;
        }

        if (args.length != 1) {
            s.sendMessage("§cWrong usage: Use /ability-info [Sequence]!");
            return true;
        }

        if (!GeneralPurposeUtil.isInteger(args[0])) {
            p.sendMessage("§cWrong usage: Use /ability-info [Sequence]!");
            return true;
        }

        Beyonder beyonder = Plugin.beyonders.get(p.getUniqueId());
        int sequence = GeneralPurposeUtil.parseInt(args[0]);

        if (beyonder.getPathway().getSequence().getCurrentSequence() > sequence) {
            p.sendMessage("§cYou have not reached Sequence " + sequence + " yet!");
            return true;
        }

        p.sendMessage(beyonder.getPathway().getItems().getAbilityInfo().get(sequence));

        return true;
    }
}
