package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.concurrent.atomic.AtomicInteger;

public class Illuminate extends Ability {
    public Illuminate(int identifier, Pathway pathway, Player p) {
        super(identifier, pathway, p);
    }

    @Override
    public void useAbility() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 9);
        Block lastBlock = iter.next();
        Block previousBlock;
        while (iter.hasNext()) {
            previousBlock = lastBlock;
            lastBlock = iter.next();
            if(lastBlock.getType().isSolid()) {
                lastBlock = previousBlock;
                break;
            }

        }
        Location loc = lastBlock.getLocation();

        loc.getBlock().setType(Material.LIGHT);
        loc.add(0.5, 0.5, 0.5);

        AtomicInteger counter = new AtomicInteger();

        new BukkitRunnable() {
            @Override
            public void run() {
                counter.getAndIncrement();
                double x = Math.cos(counter.get());
                double z = Math.sin(counter.get());
                double y = Math.sin(counter.get());
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY() + y, loc.getZ(), 1, 0, 0, 0, 0);
                y = Math.cos(counter.get());
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX(), loc.getY() + y, loc.getZ() + z, 1, 0, 0, 0, 0);

                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 5, 0.25, 0.25, 0.25, 0);

                if(counter.get() == 2 * 20) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }

                if(counter.get() >= 15 * 20) {
                    loc.getBlock().setType(Material.AIR);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }
}
