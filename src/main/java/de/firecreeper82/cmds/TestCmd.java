package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Plugin;
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
        if(args.length == 0 || !(s instanceof Player p))
            return true;

        if(args[0].equalsIgnoreCase("cauldron"))
            cauldronCmd(p);



        return true;
    }

    private void cauldronCmd(Player p) {
        Location loc = p.getLocation();
        Block block = loc.getBlock();
        block.setType(Material.CAULDRON);
        block.setMetadata("special", new FixedMetadataValue(Plugin.instance, true));
    }
}
