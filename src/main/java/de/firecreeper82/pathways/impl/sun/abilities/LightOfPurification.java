package de.firecreeper82.pathways.impl.sun.abilities;

import com.google.common.util.concurrent.AtomicDouble;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class LightOfPurification extends Ability {
    public LightOfPurification(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Location loc = p.getLocation();

        //Spawning Particles
        AtomicDouble radius = new AtomicDouble();
        radius.set(1.8);
        loc.add(0, 1, 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                radius.set(radius.get() + 0.75);
                for(int j = 0; j < 30 * radius.get(); j++) {
                    double x = radius.get() * Math.cos(j);
                    double z = radius.get() * Math.sin(j);
                    loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY(), loc.getZ() + z, 5, 0.2, 1, 0.2, 0, dustRipple);
                    Random rand = new Random();
                    if(j % (rand.nextInt(8) + 1) == 0)
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0.2, 1, 0.2, 0);

                    //checking for entities
                    for(Entity entity : loc.getWorld().getNearbyEntities(new Location(loc.getWorld(), loc.getX() + x, loc.getY(), loc.getZ() + z), 1, 3, 1)) {
                        if(entity instanceof LivingEntity) {
                            if(((LivingEntity) entity).getCategory() == EntityCategory.UNDEAD)
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 4, false, false));
                        }
                    }
                }

                if(radius.get() >= 20) {
                    cancel();
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }
}
