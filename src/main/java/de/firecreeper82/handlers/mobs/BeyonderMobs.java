package de.firecreeper82.handlers.mobs;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class BeyonderMobs {


    private final HashMap<String, Entity> mobs = new HashMap<>();

    public BeyonderMobs() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<String, Entity> set : mobs.entrySet()) {
                    switch (set.getKey()) {
                        case "wraith" -> wraith(set.getValue());
                        case "gargoyle" -> gargoyle(set.getValue());
                        case "bane" -> bane(set.getValue());
                        case "plunderer" -> plunderer((Vex) set.getValue());
                        case "wolf" -> wolf((Wolf) set.getValue());
                    }

                    mobs.remove(set.getKey(), set.getValue());
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 40);
    }

    private void wolf(Wolf wolf) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(216, 216, 216), 50f);

        new BukkitRunnable() {
            Player target;
            @Override
            public void run() {
                if(!wolf.isValid()) {
                    cancel();
                }

                wolf.setCollarColor(DyeColor.RED);
                wolf.setTamed(false);
                wolf.setAngry(true);
                wolf.setInterested(false);

                Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getBaseValue() * 5);

                wolf.getWorld().spawnParticle(Particle.REDSTONE, wolf.getLocation(), 10, 1, 1, 1, dust);

                if(target == null && wolf.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if(target == null) {
                    for(Entity e : wolf.getNearbyEntities(40, 40, 40)) {
                        if(!(e instanceof Player))
                            continue;
                        target = (Player) e;
                    }
                }

                if(target == null)
                    return;

                wolf.setTarget(target);

            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    public void addMob(@NonNull Entity mob, @NonNull String id) {
        mobs.put(id, mob);
    }

    private void wraith(Entity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                    return;
                }

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)), .6f);
                entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 50, .5, .5, .5, dust);
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void plunderer(Vex vex) {
        Random random = new Random();
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(154, 0, 194), 1.25f);

        new BukkitRunnable() {
            Player target;
            @Override
            public void run() {
                if(!vex.isValid()) {
                    cancel();
                }

                if(target == null && vex.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if(target == null) {
                    for(Entity e : vex.getNearbyEntities(40, 40, 40)) {
                        if(!(e instanceof Player))
                            continue;
                        target = (Player) e;
                    }
                }

                if(target == null)
                    return;

                vex.setTarget(target);

                if(random.nextInt(25) == 0) {
                    switch (random.nextInt(10)) {
                        case 0, 1, 2 -> {
                            for(int i = 0; i < random.nextInt(5) + 1; i++) {
                                Location spawnLoc = vex.getLocation().add(random.nextInt(6) - 3, random.nextInt(6) - 3, random.nextInt(6) - 3);
                                vex.getWorld().spawnEntity(spawnLoc, EntityType.VEX);
                            }
                        }

                        case 3, 4, 5, 6, 7, 8, 9 -> {
                            if(target == null)
                                break;


                            Location loc = vex.getLocation().clone();
                            Vector vector = (target.getLocation().toVector().subtract(loc.toVector())).normalize();
                            final Player playerTarget = target;
                            new BukkitRunnable() {
                                int counter = 0;

                                @Override
                                public void run() {
                                    vex.getWorld().spawnParticle(Particle.REDSTONE, loc, 4, .05, .05, .05, dust);
                                    loc.add(vector);

                                    for(Entity e : vex.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                                        if(e != playerTarget)
                                            continue;
                                        playerTarget.damage(4, vex);
                                        playerTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
                                        playerTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2));
                                    }

                                    counter++;
                                    if(counter > 50)
                                        cancel();
                                }
                            }.runTaskTimer(Plugin.instance, 0, 4);
                        }
                    }
                }


            }
        }.runTaskTimer(Plugin.instance, 0, 0);

        drawSpiral(vex, 0, 0);
        drawSpiral(vex, .25, 10);

    }

    private void drawSpiral(Entity entity, double heightOffset, int delay) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(154, 0, 194), 1.25f);
        new BukkitRunnable() {
            final double spiralRadius = 1;

            double spiral = 0;
            double height = heightOffset;
            double spiralX;
            double spiralZ;
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                }

                Location entityLoc = entity.getLocation().clone();
                entityLoc.add(0, 0.75, 0);

                spiralX = spiralRadius * Math.cos(spiral);
                spiralZ = spiralRadius * Math.sin(spiral);
                spiral += 0.25;
                height += .05;
                if(height >= 2.5)
                    height = 0;
                if(entityLoc.getWorld() != null)
                    entityLoc.getWorld().spawnParticle(Particle.REDSTONE, spiralX + entityLoc.getX(), height + entityLoc.getY(), spiralZ + entityLoc.getZ(), 5, dust);
            }
        }.runTaskTimer(Plugin.instance, delay, 0);
    }

    private void bane(Entity entity) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(200, 60, 0), 2f);
        Random random = new Random();

        new BukkitRunnable() {
            Player target;
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                    return;
                }

                entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 60, 1, 2, 1, dust);

                if(target == null && entity.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if(target == null) {
                    for(Entity e : entity.getNearbyEntities(40, 40, 40)) {
                        if(!(e instanceof Player))
                            return;
                        target = (Player) e;
                    }
                }

                if(target == null)
                    return;

                ((Allay) entity).setTarget(target);

                if(random.nextInt(15) == 0)  {
                    Location loc = entity.getLocation().clone();
                    Vector vector = (target.getLocation().toVector().subtract(loc.toVector())).normalize();
                    new BukkitRunnable() {
                        int counter = 0;
                        @Override
                        public void run() {
                            entity.getWorld().spawnParticle(Particle.REDSTONE, loc, 45, .5, .5, .5, dust);
                            loc.add(vector);

                            for(Entity e : entity.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                                if(e != target)
                                    continue;
                                target.damage(12, entity);
                            }

                            counter++;
                            if(counter > 50)
                                cancel();
                        }
                    }.runTaskTimer(Plugin.instance, 0, 1);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }

    private void gargoyle(Entity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                    return;
                }

                for(Entity e : entity.getNearbyEntities(20, 20, 20)) {
                    if(!(e instanceof Player))
                        continue;

                    ((Mob) entity).setTarget((Player) e);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 10);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                    return;
                }

                drawWings(entity.getLocation().add(0, .5, 0));
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }


    private final boolean x = true;
    private final boolean o = false;
    private final boolean[][] shape = {
            {o, o, o, x, o, o, o, o, o, o, o, o, x, o, o, o},
            {o, o, x, x, o, o, o, o, o, o, o, o, x, x, o, o},
            {o, o, x, x, o, o, o, o, o, o, o, o, x, x, o, o},
            {o, x, x, x, x, o, o, o, o, o, o, x, x, x, x, o},
            {o, x, x, x, x, o, o, o, o, o, o, x, x, x, x, o},
            {o, o, x, x, x, x, o, o, o, o, x, x, x, x, o, o},
            {o, o, x, x, x, x, x, o, o, x, x, x, x, x, o, o},
            {o, o, o, x, x, x, x, x, x, x, x, x, x, o, o, o},
            {o, o, o, o, o, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, o, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, x, x, x, o, o, x, x, x, o, o, o, o},
            {o, o, o, x, x, x, x, o, o, x, x, x, x, o, o, o},
            {o, o, o, x, x, x, o, o, o, o, x, x, x, o, o, o},
            {o, o, o, x, x, o, o, o, o, o, o, x, x, o, o, o},
            {o, o, o, x, x, o, o, o, o, o, o, x, x, o, o, o},
    };

    private void drawWings(Location loc) {
        double space = 0.24;
        double defX = loc.getX() - (space * shape[0].length / 2) + space;
        double x = defX;
        double y = loc.clone().getY() + 2.8;
        double fire = -((loc.getYaw() + 180) / 60);
        fire += (loc.getYaw() < -180 ? 3.25 : 2.985);

        for (boolean[] booleans : shape) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) {

                    Location target = loc.clone();
                    target.setX(x);
                    target.setY(y);

                    Vector v = target.toVector().subtract(loc.toVector());
                    Vector v2 = VectorUtils.getBackVector(loc);
                    v = VectorUtils.rotateAroundAxisY(v, fire);
                    v2.setY(0).multiply(-0.5);

                    loc.add(v);
                    loc.add(v2);

                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(50, 50, 50), .6f);
                    for (int k = 0; k < 3; k++)
                        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc, 3, 0.02, 0.02, 0.02, dust);
                    loc.subtract(v2);
                    loc.subtract(v);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }
}
