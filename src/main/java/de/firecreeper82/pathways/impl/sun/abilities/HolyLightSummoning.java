package de.firecreeper82.pathways.impl.sun.abilities;

import com.google.common.util.concurrent.AtomicDouble;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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

public class HolyLightSummoning extends Ability {
    public HolyLightSummoning(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    //TODO make it be able to hit enemies in the air too
    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 22);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (!lastBlock.getType().isSolid()) {
                continue;
            }
            break;
        }
        Location loc = lastBlock.getLocation();
        loc.add(0, 14, 0);

        //Runnable
        AtomicInteger counter = new AtomicInteger();
        final Material[] lastMaterial = {loc.getBlock().getType()};
        new BukkitRunnable() {
            @Override
            public void run() {
                counter.getAndIncrement();

                //Particles
                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + 3.2, loc.getY(), loc.getZ(), 6, 0.1, 0, 0.1, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() - 3.2, loc.getY(), loc.getZ(), 6, 0.1, 0, 0.1, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX(), loc.getY(), loc.getZ() + 3.2, 6, 0.1, 0, 0.1, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX(), loc.getY(), loc.getZ() - 3.2, 6, 0.1, 0, 0.1, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + 2.4, loc.getY(), loc.getZ() + 2.4, 5, 0.1, 0, 0.1, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() - 2.4, loc.getY(), loc.getZ() - 2.4, 5, 0.1, 0, 0.1, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() - 2.4, loc.getY(), loc.getZ() + 2.4, 5, 0.1, 0, 0.1, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + 2.4, loc.getY(), loc.getZ() - 2.4, 5, 0.1, 0, 0.1, 0);

                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + 2.7, loc.getY(), loc.getZ(), 12, 0.2, 0, 0.2, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() - 2.7, loc.getY(), loc.getZ(), 12, 0.2, 0, 0.2, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX(), loc.getY(), loc.getZ() + 2.7, 12, 0.2, 0, 0.2, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX(), loc.getY(), loc.getZ() - 2.7, 12, 0.2, 0, 0.2, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + 1.9, loc.getY(), loc.getZ() + 1.9, 12, 0.2, 0, 0.2, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() - 1.9, loc.getY(), loc.getZ() - 1.9, 12, 0.2, 0, 0.2, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() - 1.9, loc.getY(), loc.getZ() + 1.9, 12, 0.2, 0, 0.2, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + 1.9, loc.getY(), loc.getZ() - 1.9, 12, 0.2, 0, 0.2, 0);


                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.25f);
                for(double i = 0; i < 3.2; i+=0.8) {
                    for(int j = 0; j < 100; j++) {
                        double x = i * Math.cos(j);
                        double z = i * Math.sin(j);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, dust);
                        if(j % 2 == 0)
                            loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY() + 1, loc.getZ() + z, 1, 0, 0, 0, 0);
                    }
                }

                //light at current loc
                loc.getBlock().setType(lastMaterial[0]);
                loc.subtract(0, 1, 0);
                lastMaterial[0] = loc.getBlock().getType();
                loc.getBlock().setType(Material.LIGHT);

                if((lastMaterial[0].isSolid() && counter.get() >= 12) || counter.get() >= 200) {
                    loc.getBlock().setType(lastMaterial[0]);
                    counter.set(0);
                    cancel();

                    //Light that stays at the ground for a bit
                    Location lightLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
                    Material[] lightMaterial = {
                            lightLoc.getBlock().getType(),
                            lightLoc.add(1, 0, 0).getBlock().getType(),
                            lightLoc.add(-2, 0, 0).getBlock().getType(),
                            lightLoc.add(1, 0, 1).getBlock().getType(),
                            lightLoc.add(0, 0, -2).getBlock().getType()
                    };

                    Block[] lightBlock = {
                            lightLoc.getBlock(),
                            lightLoc.add(1, 0, 0).getBlock(),
                            lightLoc.add(-2, 0, 0).getBlock(),
                            lightLoc.add(1, 0, 1).getBlock(),
                            lightLoc.add(0, 0, -2).getBlock()
                    };

                    for(Block b : lightBlock) {
                        b.setType(Material.LIGHT);
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i < lightBlock.length; i++) {
                                Block b = lightBlock[i];
                                b.setType(lightMaterial[i]);
                            }
                        }
                    }.runTaskLater(Plugin.instance, 2 * 20);


                    //damage nearby entities
                    ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 15, 15, 15);
                    for(Entity entity : nearbyEntities) {
                        if(entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 3));
                            } else {
                                if(livingEntity.getUniqueId() != pathway.getUuid())
                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
                            }
                        }
                    }

                    //Particles on ground
                    AtomicDouble radius = new AtomicDouble();
                    AtomicInteger factor = new AtomicInteger();
                    radius.set(1.8);
                    loc.add(0, 1, 0);
                    Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.5f);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            radius.set(radius.get() + 0.75);
                            for(int i = 0; i < 100; i++) {
                                factor.incrementAndGet();
                                double x = radius.get() * Math.cos(factor.get());
                                double z = radius.get() * Math.sin(factor.get());
                                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, 0.1, 0, 0.1, 0.15);
                                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x + 0.2, loc.getY(), loc.getZ() + z + 0.2, 3, dustRipple);
                            }

                            if(radius.get() >= 9) {
                                cancel();
                                pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                            }
                        }
                    }.runTaskTimer(Plugin.instance, 0, 1);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }
}
