package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class HolyLight extends Ability {

    public HolyLight(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    //TODO make it be able to hit enemies in the air too
    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 15);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        Location loc = lastBlock.getLocation();
        loc.add(0, 14, 0);

        AtomicInteger counter = new AtomicInteger();
        final Material[] lastMaterial = {loc.getBlock().getType()};
        new BukkitRunnable() {
            @Override
            public void run() {
                counter.getAndIncrement();

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                for(double i = 0; i < 3.2; i+=0.8) {
                    for(int j = 0; j < 100; j++) {
                        double x = i * Math.cos(j);
                        double z = i * Math.sin(j);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY(), loc.getZ() + z, 5, dust);
                        loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY() + 1, loc.getZ() + z, 1, 0, 0, 0, 0);
                    }
                }

                loc.getBlock().setType(lastMaterial[0]);
                loc.subtract(0, 0.75, 0);
                lastMaterial[0] = loc.getBlock().getType();
                loc.getBlock().setType(Material.LIGHT);

                if((lastMaterial[0].isSolid() && counter.get() >= 18.6) || counter.get() >= 200) {
                    loc.getBlock().setType(lastMaterial[0]);
                    counter.set(0);
                    cancel();
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;

                    //damage nearby entities
                    ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc.subtract(5, 0, 5), 10, 10, 10);
                    for(Entity entity : nearbyEntities) {
                        if(entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
                            } else {
                                if(livingEntity.getUniqueId() != pathway.getUuid())
                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 0));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }
}
