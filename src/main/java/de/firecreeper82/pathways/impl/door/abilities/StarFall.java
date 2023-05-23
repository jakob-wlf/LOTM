package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class StarFall extends Ability {
    public StarFall(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);

        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();

        for (int i = 0; i < 85; i++) {
            if (loc.getBlock().getType().isSolid())
                break;
            loc.add(dir);
        }

        loc.subtract(dir);

        if (loc.getWorld() == null)
            return;


        Random random = new Random();

        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(255, 251, 0), 5f);

        for (int i = 0; i < random.nextInt(40, 80); i++) {
            Location starLoc = (i == 0) ? loc.clone() : loc.clone().add(random.nextInt(-45, 45), 0, random.nextInt(-45, 45));

            float angle = p.getEyeLocation().getYaw() / 60;
            Location startLoc = starLoc.clone().add(-Math.cos(angle) * 15, random.nextInt(40, 80), -Math.sin(angle) * 15);

            Vector fallDir = starLoc.clone().toVector().subtract(startLoc.clone().toVector()).normalize().multiply(2);

            if (starLoc.getWorld() == null || startLoc.getWorld() == null)
                return;

            new BukkitRunnable() {
                @Override
                public void run() {

                    startLoc.getWorld().spawnParticle(Particle.REDSTONE, startLoc, 8, 0.15, 0.15, 0.15, dust);
                    p.spawnParticle(Particle.REDSTONE, startLoc, 2, 0, 0, 0, dust);

                    startLoc.add(fallDir);

                    if (startLoc.getBlock().getType().isSolid()) {

                        for (Entity entity : startLoc.getWorld().getNearbyEntities(startLoc, 10, 10, 10)) {
                            if (entity == p)
                                continue;
                            if (entity instanceof Damageable d)
                                d.damage(50, p);
                        }

                        startLoc.getWorld().createExplosion(startLoc, 15f);
                        cancel();
                    }
                }
            }.runTaskTimer(Plugin.instance, random.nextInt(20 * 4), 0);
        }
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.DIAMOND_SWORD, "Starfall", "25000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
