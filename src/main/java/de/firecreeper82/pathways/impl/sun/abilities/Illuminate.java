package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.Objects;

public class Illuminate extends Recordable {
    public Illuminate(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        if (!recorded)
            pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        destroy(beyonder, recorded);

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 9);
        Block lastBlock = iter.next();
        Block previousBlock;
        while (iter.hasNext()) {
            previousBlock = lastBlock;
            lastBlock = iter.next();
            if (lastBlock.getType().isSolid()) {
                lastBlock = previousBlock;
                break;
            }

        }
        Location loc = lastBlock.getLocation();

        loc.getBlock().setType(Material.LIGHT);
        loc.add(0.5, 0.5, 0.5);


        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;
                double x = Math.cos(counter);
                double z = Math.sin(counter);
                double y = Math.sin(counter);
                Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY() + y, loc.getZ(), 1, 0, 0, 0, 0);
                y = Math.cos(counter);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX(), loc.getY() + y, loc.getZ() + z, 1, 0, 0, 0, 0);

                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 5, 0.25, 0.25, 0.25, 0);

                if (counter == 2 * 20) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }

                if (counter >= 15 * 20) {
                    loc.getBlock().setType(Material.AIR);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.GOLD_NUGGET, "Illuminate", "20", identifier, 8, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
