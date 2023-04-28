package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DarkFlames extends Ability {

    public DarkFlames(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        Vector vector = p.getLocation().getDirection().normalize();
        Location loc = p.getEyeLocation().clone();
        if(loc.getWorld() == null)
            return;
        World world = loc.getWorld();

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                if(counter >= 50) {
                    cancel();
                    return;
                }

                loc.add(vector);
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 40, .25, .25, .25, 0);

                if(world.getNearbyEntities(loc, 1, 1, 1).isEmpty())
                    return;

                if(loc.getBlock().getType().isSolid()) {
                    loc.clone().subtract(vector).getBlock().setType(Material.SOUL_FIRE);
                    cancel();
                    return;
                }

                boolean cancelled = false;
                for(Entity entity : world.getNearbyEntities(loc, 1, 1, 1)) {
                    if(!(entity instanceof LivingEntity livingEntity) || entity == p)
                        continue;
                    livingEntity.damage(15, p);
                    livingEntity.setFireTicks(20 * 20);
                    cancelled = true;
                }

                if(cancelled)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.CHORUS_FRUIT, "Black Flames", "35", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
