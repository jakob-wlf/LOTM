package dev.ua.ikeepcalm.cmds;

import dev.ua.ikeepcalm.entities.beyonders.Beyonder;
import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.utils.GeneralPurposeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TestCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (args.length == 0 || !(s instanceof Player p))
            return true;

        if (args[0].equalsIgnoreCase("cauldron"))
            cauldronCmd(p);

        if (args[0].equalsIgnoreCase("characteristic")) {
            p.getInventory().addItem(LordOfTheMinecraft.instance.getCharacteristic().getCharacteristic(GeneralPurposeUtil.parseInt(args[2]), args[1], "§" + args[3]));
        }

        if (args[0].equalsIgnoreCase("recipe")) {
            p.getInventory().addItem(LordOfTheMinecraft.instance.getRecipe().getRecipeForSequence(LordOfTheMinecraft.instance.getPotions().get(0), GeneralPurposeUtil.parseInt(args[1])));
        }

        if (args[0].equalsIgnoreCase("instances")) {
            Bukkit.broadcastMessage(LordOfTheMinecraft.beyonders.size() + "");
        }
        if (args[0].equalsIgnoreCase("npc")) {
            boolean aggressive = Boolean.parseBoolean(args[1]);
            int sequence = Integer.parseInt(args[2]);
            int pathway = Integer.parseInt(args[3]);

            LordOfTheMinecraft.instance.getRogueBeyonders().spawnNPC(aggressive, sequence, pathway, p.getLocation());
        }
        if (args[0].equalsIgnoreCase("acting")) {
            Beyonder beyonder = LordOfTheMinecraft.beyonders.get(p.getUniqueId());
            beyonder.setActing(Integer.parseInt(args[1]));
        }

        return true;
    }

    private void cauldronCmd(Player p) {
        Location loc = p.getLocation();
        Block block = loc.getBlock();
        block.setType(Material.CAULDRON);
        block.setMetadata("special", new FixedMetadataValue(LordOfTheMinecraft.instance, true));
    }
}
