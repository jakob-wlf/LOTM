package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class BeamOfLight extends NPCAbility {
    public BeamOfLight(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useNPCAbility(Location target, Entity caster, double multiplier) {
        Location loc = caster.getLocation().add(0, 1, 0);
        Vector direction = loc.getDirection().normalize().multiply(.5);
        World world = caster.getWorld();

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;

                Location tempLoc = loc.clone();

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);

                for(int i = 0; i < 48; i++) {
                    tempLoc.add(direction);
                    world.spawnParticle(Particle.REDSTONE, tempLoc, 2, 0, 0, 0, dust);
                }

                if(counter > 25)
                    cancel();
            }

        }.runTaskTimer(Plugin.instance, 0, 0);

        new BukkitRunnable() {

            final int circlePoints = 25;
            double radius = .15;

            final Location loc = caster.getLocation().add(0, 1, 0);
            final World world = caster.getWorld();

            final double pitch = (loc.getPitch() + 90.0F) * 0.017453292F;
            final double yaw = -loc.getYaw() * 0.017453292F;

            final double increment = (2 * Math.PI) / circlePoints;


            @Override
            public void run() {
                Location tempLoc = loc.clone();

                for(int i = 0; i < 48; i++) {
                    tempLoc.add(direction);

                    //Particle effects
                    //Calls rotateAroundAxis() functions from VectorUtils class
                    for (int j = 0; j < circlePoints; j++) {
                        //use i instead of j for cool looking effect, maybe later
                        double angle = j * increment;
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);

                        Vector vec = new Vector(x, 0, z);
                        VectorUtils.rotateAroundAxisX(vec, pitch);
                        VectorUtils.rotateAroundAxisY(vec, yaw);
                        tempLoc.add(vec);

                        world.spawnParticle(Particle.END_ROD, tempLoc, 1, .05, .05, .05, 0);
                        if(tempLoc.getBlock().getType().getHardness() >= 0) {
                            if(random.nextInt(3) == 0)
                                tempLoc.getBlock().setType(Material.FIRE);
                            else
                                tempLoc.getBlock().setType(Material.AIR);
                        }

                        tempLoc.subtract(vec);
                    }

                    if(world.getNearbyEntities(tempLoc, 4, 4, 4).isEmpty())
                        continue;

                    for(Entity e : world.getNearbyEntities(tempLoc, 4, 4, 4)) {
                        if(!(e instanceof LivingEntity livingEntity) || e == caster)
                            continue;
                        if(livingEntity.getCategory() == EntityCategory.UNDEAD)
                            livingEntity.damage(18 * multiplier, caster);
                        livingEntity.damage(10 * multiplier, caster);
                    }

                }

                radius += .25;

                if(radius > 1.75) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 25, 0);

        new BukkitRunnable() {

            final int circlePoints = 20;
            double radius = .15;

            final Location loc = caster.getLocation().add(0, 1, 0);
            final World world = caster.getWorld();

            final double pitch = (loc.getPitch() + 90.0F) * 0.017453292F;
            final double yaw = -loc.getYaw() * 0.017453292F;

            final double increment = (2 * Math.PI) / circlePoints;


            @Override
            public void run() {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);

                Location tempLoc = loc.clone();

                for (int i = 0; i < 48; i++) {
                    tempLoc.add(direction);

                    //Particle effects
                    //Calls rotateAroundAxis() functions from VectorUtils class
                    for (int j = 0; j < circlePoints; j++) {
                        //use i instead of j for cool looking effect, maybe later
                        double angle = j * increment;
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);

                        Vector vec = new Vector(x, 0, z);
                        VectorUtils.rotateAroundAxisX(vec, pitch);
                        VectorUtils.rotateAroundAxisY(vec, yaw);
                        tempLoc.add(vec);

                        world.spawnParticle(Particle.REDSTONE, tempLoc, 5, .15, .15, .15, dust);

                        tempLoc.subtract(vec);
                    }
                }

                radius += .6;

                if (radius > 2.5)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 24, 0);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        double multiplier = getMultiplier();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Location loc = p.getEyeLocation();
        Vector direction = loc.getDirection().normalize().multiply(.5);
        World world = p.getWorld();

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;

                Location tempLoc = loc.clone();

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);

                for(int i = 0; i < 48; i++) {
                    tempLoc.add(direction);
                    world.spawnParticle(Particle.REDSTONE, tempLoc, 2, 0, 0, 0, dust);
                }

                if(counter > 25)
                    cancel();
            }

        }.runTaskTimer(Plugin.instance, 0, 0);

        new BukkitRunnable() {

            final int circlePoints = 25;
            double radius = .15;

            final Location loc = p.getEyeLocation();
            final World world = p.getWorld();

            final double pitch = (loc.getPitch() + 90.0F) * 0.017453292F;
            final double yaw = -loc.getYaw() * 0.017453292F;

            final double increment = (2 * Math.PI) / circlePoints;


            @Override
            public void run() {
                Location tempLoc = loc.clone();

                for(int i = 0; i < 48; i++) {
                    tempLoc.add(direction);

                    //Particle effects
                    //Calls rotateAroundAxis() functions from VectorUtils class
                    for (int j = 0; j < circlePoints; j++) {
                        //use i instead of j for cool looking effect, maybe later
                        double angle = j * increment;
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);

                        Vector vec = new Vector(x, 0, z);
                        VectorUtils.rotateAroundAxisX(vec, pitch);
                        VectorUtils.rotateAroundAxisY(vec, yaw);
                        tempLoc.add(vec);

                        world.spawnParticle(Particle.END_ROD, tempLoc, 1, .05, .05, .05, 0);
                        if(tempLoc.getBlock().getType().getHardness() >= 0) {
                            if(random.nextInt(3) == 0)
                                tempLoc.getBlock().setType(Material.FIRE);
                            else
                                tempLoc.getBlock().setType(Material.AIR);
                        }

                        tempLoc.subtract(vec);
                    }

                    if(world.getNearbyEntities(tempLoc, 4, 4, 4).isEmpty())
                        continue;

                    for(Entity e : world.getNearbyEntities(tempLoc, 4, 4, 4)) {
                        if(!(e instanceof LivingEntity livingEntity) || e == p)
                            continue;
                        if(livingEntity.getCategory() == EntityCategory.UNDEAD)
                            livingEntity.damage(18 * multiplier);
                        livingEntity.damage(10 * multiplier);
                    }

                }

                radius += .25;

                if(radius > 1.75) {
                    cancel();
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }
            }
        }.runTaskTimer(Plugin.instance, 25, 0);

        new BukkitRunnable() {

            final int circlePoints = 20;
            double radius = .15;

            final Location loc = p.getEyeLocation();
            final World world = p.getWorld();

            final double pitch = (loc.getPitch() + 90.0F) * 0.017453292F;
            final double yaw = -loc.getYaw() * 0.017453292F;

            final double increment = (2 * Math.PI) / circlePoints;


            @Override
            public void run() {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);

                Location tempLoc = loc.clone();

                for (int i = 0; i < 48; i++) {
                    tempLoc.add(direction);

                    //Particle effects
                    //Calls rotateAroundAxis() functions from VectorUtils class
                    for (int j = 0; j < circlePoints; j++) {
                        //use i instead of j for cool looking effect, maybe later
                        double angle = j * increment;
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);

                        Vector vec = new Vector(x, 0, z);
                        VectorUtils.rotateAroundAxisX(vec, pitch);
                        VectorUtils.rotateAroundAxisY(vec, yaw);
                        tempLoc.add(vec);

                        world.spawnParticle(Particle.REDSTONE, tempLoc, 5, .15, .15, .15, dust);

                        tempLoc.subtract(vec);
                    }
                }

                radius += .6;

                if (radius > 2.5)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 24, 0);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.GOLDEN_HOE, "Beam of Light", "2000", identifier, 3, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}