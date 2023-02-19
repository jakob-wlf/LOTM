package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FlameControlling extends Ability {

    public FlameControlling(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        //double multiplier = getMultiplier();
        p = pathway.getBeyonder().getPlayer();

        Vector direction = p.getLocation().getDirection().normalize();
        Location loc = p.getEyeLocation().clone();

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;

                loc.add(direction);
                if(loc.getWorld() == null) {
                    cancel();
                    return;
                }
                loc.getWorld().spawnParticle(Particle.FLAME,  loc, 15, 0.12, 0.12, 0.12, 0.025);
                loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL,  loc.clone().add(0, 0.12, 0), 6, 0.01, 0.01, 0.01, 0);

                if(loc.getBlock().getType().isSolid() || counter >= 100) {
                    if(loc.getBlock().getType().isSolid() || !loc.clone().add(0, 1, 0).getBlock().getType().isSolid())
                        loc.clone().add(0, 1, 0).getBlock().setType(Material.FIRE);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.FIRE_CHARGE, "Flame Controlling", "45", identifier, 7, pathway.getBeyonder().getPlayer().getName());
    }
}
