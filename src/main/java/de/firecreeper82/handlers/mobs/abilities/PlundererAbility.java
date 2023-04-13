package de.firecreeper82.handlers.mobs.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.MobUsableAbility;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlundererAbility extends MobUsableAbility {
    public PlundererAbility(int frequency) {
        super(frequency);
    }

    @Override
    public void useAbility() {

    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public void useAbility(Location startLoc, Location endLoc, double multiplier, Entity user, Entity target) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(154, 0, 194), 1.25f);
        if(!(target instanceof Player playerTarget))
            return;

        Vector vector = (target.getLocation().toVector().subtract(startLoc.toVector())).normalize();
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                user.getWorld().spawnParticle(Particle.REDSTONE, startLoc, 4, .05, .05, .05, dust);
                startLoc.add(vector);

                for(Entity e : user.getWorld().getNearbyEntities(startLoc, 1, 1, 1)) {
                    if(e != playerTarget)
                        continue;
                    playerTarget.damage(4, user);
                    playerTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
                    playerTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2));
                    cancel();
                }

                counter++;
                if(counter > 50)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 4);
    }
}
