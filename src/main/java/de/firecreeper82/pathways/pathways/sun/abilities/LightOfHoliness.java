package de.firecreeper82.pathways.pathways.sun.abilities;

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
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class LightOfHoliness extends Ability {
    public LightOfHoliness(int identifier, Pathway pathway, Player p) {
        super(identifier, pathway, p);
    }

    @Override
    public void useAbility() {
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
        loc.add(0, 19, 0);

        //Runnable
        AtomicInteger counter = new AtomicInteger();
        final Material[] lastMaterial = {loc.getBlock().getType()};
        new BukkitRunnable() {
            @Override
            public void run() {
                counter.getAndIncrement();

                //Particles
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + 3.8, loc.getY(), loc.getZ(), 20, 0.15, 0, 0.15, 0, dust);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() - 3.8, loc.getY(), loc.getZ(), 20, 0.15, 0, 0.15, 0, dust);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ() + 3.8, 20, 0.15, 0, 0.15, 0, dust);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ() - 3.8, 20, 0.15, 0, 0.15, 0, dust);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + 3, loc.getY(), loc.getZ() + 3, 20, 0.15, 0, 0.15, 0, dust);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() - 3, loc.getY(), loc.getZ() - 3, 20, 0.15, 0, 0.15, 0, dust);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() - 3, loc.getY(), loc.getZ() + 3, 20, 0.15, 0, 0.15, 0, dust);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + 3, loc.getY(), loc.getZ() - 3, 20, 0.15, 0, 0.15, 0, dust);


                for(double i = 0; i < 3.8; i+=0.4) {
                    for(int j = 0; j < 40; j++) {
                        double x = i * Math.cos(j);
                        double z = i * Math.sin(j);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, 0.1, 0.2, 0.1, 0);
                        if((int) loc.getY() % 2 == 0)
                            loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY() + 0.25, loc.getZ() + z, 1, 0.2, 0, 0.2, 0);
                    }
                }

                //light at current loc
                loc.getBlock().setType(lastMaterial[0]);
                loc.subtract(0, 1, 0);
                lastMaterial[0] = loc.getBlock().getType();
                loc.getBlock().setType(Material.LIGHT);

                //Reached ground
                if((lastMaterial[0].isSolid() && counter.get() >= 17) || counter.get() >= 200) {
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

                    //Replace the ground with burned stuff
                    int burnRadius = 8;
                    for(int i = 2; i > -4; i--)
                        for (int x = -burnRadius; x <= burnRadius; x++) {
                            for (int z = -burnRadius; z <= burnRadius; z++) {
                                if( (x*x) + (z*z) <= Math.pow(burnRadius, 2)) {
                                    Block block = p.getWorld().getBlockAt((int) loc.getX() + x, (int) loc.getY() + i, (int) loc.getZ() + z);
                                    if(block.getType() == Material.DIRT || block.getType() == Material.DIRT_PATH || block.getType() == Material.COARSE_DIRT || block.getType() == Material.ROOTED_DIRT || block.getType() == Material.GRASS_BLOCK)
                                        block.setType(Material.NETHERRACK);
                                    if(block.getType() == Material.STONE || block.getType() == Material.COBBLESTONE || block.getType() == Material.DIORITE || block.getType() == Material.ANDESITE || block.getType() == Material.GRANITE || block.getType() == Material.DEEPSLATE || block.getType() == Material.TUFF || block.getType() == Material.CALCITE || block.getType() == Material.GRAVEL)
                                        block.setType(Material.BASALT);
                                    if(block.getType() == Material.WATER)
                                        block.setType(Material.AIR);
                                    if(block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) {
                                        Random rand = new Random();
                                        if(rand.nextInt(4) == 0) {
                                            block.setType(Material.FIRE);
                                        }
                                    }
                                    if(block.getType() == Material.SAND || block.getType() == Material.RED_SAND)
                                        block.setType(Material.GLASS);
                                }
                            }
                        }


                    //damage nearby entities
                    ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 15, 15, 15);
                    for(Entity entity : nearbyEntities) {
                        if(entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 5));
                                entity.setFireTicks(100);
                            } else {
                                if(livingEntity.getUniqueId() != pathway.getUuid()) {
                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 3));
                                    entity.setFireTicks(100);
                                }
                            }
                        }
                    }

                    //Particles on ground
                    AtomicDouble radius = new AtomicDouble();
                    AtomicDouble radiusFlame = new AtomicDouble();
                    radius.set(1.8);
                    radiusFlame.set(1.8);
                    loc.add(0, 1, 0);
                    Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.5f);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            radiusFlame.set(radiusFlame.get() + 0.75);
                            for(int j = 0; j < 150; j++) {
                                double x = radiusFlame.get() * Math.cos(j);
                                double z = radiusFlame.get() * Math.sin(j);
                                loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY(), loc.getZ() + z, 5, 0, 0, 0, 0);
                            }

                            if(radiusFlame.get() >= 11) {
                                cancel();
                            }
                        }
                    }.runTaskTimer(Plugin.instance, 0, 1);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            radius.set(radius.get() + 0.75);
                            for(int i = 0; i < 150; i++) {
                                double x = radius.get() * Math.cos(i);
                                double z = radius.get() * Math.sin(i);
                                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, 0.1, 0, 0.1, 0.15);
                                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x + 0.2, loc.getY(), loc.getZ() + z + 0.2, 3, dustRipple);
                            }

                            if(radius.get() >= 14) {
                                cancel();
                                pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                            }
                        }
                    }.runTaskTimer(Plugin.instance, 4, 1);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }
}
