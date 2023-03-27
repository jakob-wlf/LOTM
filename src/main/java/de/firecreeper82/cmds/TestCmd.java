package de.firecreeper82.cmds;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
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
        block.setMetadata("special", new MetadataValue() {
            @Override
            public Object value() {
                return true;
            }

            @Override
            public int asInt() {
                return 1;
            }

            @Override
            public float asFloat() {
                return 1f;
            }

            @Override
            public double asDouble() {
                return 1d;
            }

            @Override
            public long asLong() {
                return 1L;
            }

            @Override
            public short asShort() {
                return 1;
            }

            @Override
            public byte asByte() {
                return 1;
            }

            @Override
            public boolean asBoolean() {
                return true;
            }

            @Override
            public String asString() {
                return "true";
            }

            @Override
            public Plugin getOwningPlugin() {
                return de.firecreeper82.lotm.Plugin.instance;
            }

            @Override
            public void invalidate() {}
        });
    }
}
