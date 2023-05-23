package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ColdWind extends Ability {

    public ColdWind(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Vector dir = p.getEyeLocation().getDirection().normalize().multiply(1.25);

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                for (Entity entity : p.getNearbyEntities(9, 9, 9)) {
                    entity.setVelocity(dir);
                    entity.setFreezeTicks(20 * 8);
                }

                for (int i = 0; i < 30; i++) {
                    Location tempLoc = p.getEyeLocation().add(random.nextInt(16) - 8, random.nextInt(10) - 5, random.nextInt(16) - 8);
                    p.getWorld().spawnParticle(Particle.SNOWFLAKE, tempLoc, 0, dir.getX(), dir.getY(), dir.getZ(), .4);
                }

                if (!pathway.getSequence().getUsesAbilities()[identifier - 1] || pathway.getBeyonder().getSpirituality() <= 10) {
                    cancel();
                    return;
                }

                counter++;

                if (counter >= 20) {
                    counter = 0;
                    pathway.getSequence().removeSpirituality(10);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.FEATHER, "Cold Wind", "10/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
