package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class Wind extends Ability {

    public Wind(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Vector dir = p.getEyeLocation().getDirection().normalize().multiply(.5);

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                for (Entity entity : p.getNearbyEntities(7, 7, 7)) {
                    entity.setVelocity(dir);
                }

                for (int i = 0; i < 8; i++) {
                    Location tempLoc = p.getEyeLocation().add(random.nextInt(10) - 5, random.nextInt(6) - 3, random.nextInt(10) - 5);
                    p.getWorld().spawnParticle(Particle.CLOUD, tempLoc, 0, dir.getX(), dir.getY(), dir.getZ(), .4);
                }

                if (!pathway.getSequence().getUsesAbilities()[identifier - 1] || pathway.getBeyonder().getSpirituality() <= 8) {
                    cancel();
                    return;
                }

                counter++;

                if (counter >= 20) {
                    counter = 0;
                    pathway.getSequence().removeSpirituality(8);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.FEATHER, "Wind", "8/s", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
