package de.firecreeper82.handlers.mobs;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class MobParticleEffects {

    public static void playParticleEffect(Location location, String id, Entity entity) {
        switch (id) {
            case "wraith" -> wraith(location);
            case "gargoyle" -> gargoyle(location.clone().add(0, .5, 0));
            case "bane" -> bane(location);
            case "plunderer" -> plunderer(entity);
            case "wolf" -> wolf(entity);
            case "fog-wolf" -> fogWolf(entity);
            case "rooster" -> rooster(entity);
            case "divine-bird" -> bird(entity);
            case "eater" -> eater(entity);
        }
    }

    private static void eater(Entity entity) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(220, 20, 255), 2f);
        entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 30, 1, 1, 1, dust);
    }

    private static void rooster(Entity entity) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
        entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 5, 1, 2, 1, dust);
    }

    private static void bird(Entity entity) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
        entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 5, 1, 2, 1, dust);
    }

    private static void wraith(Location location) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)), .6f);
        Objects.requireNonNull(location.getWorld()).spawnParticle(Particle.REDSTONE, location, 50, .5, .5, .5, dust);
    }

    private static void bane(Location location) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(200, 60, 0), 2f);
        Objects.requireNonNull(location.getWorld()).spawnParticle(Particle.REDSTONE, location, 60, 1, 2, 1, dust);
    }

    private static void plunderer(Entity entity) {
        drawSpiral(entity, 0, 0);
        drawSpiral(entity, .25, 10);
    }

    private static void wolf(Entity entity) {
        if (!(entity instanceof Wolf wolf))
            return;

        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(216, 216, 216), 50f);
        wolf.setCollarColor(DyeColor.RED);
        wolf.setTamed(false);
        wolf.setAngry(true);
        wolf.setInterested(false);

        Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getBaseValue() * 10);
        Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).getBaseValue() * 6);

        wolf.getWorld().spawnParticle(Particle.REDSTONE, wolf.getLocation(), 10, 1, 1, 1, dust);

    }

    private static void fogWolf(Entity entity) {
        if (!(entity instanceof Wolf wolf))
            return;

        wolf.setCollarColor(DyeColor.RED);
        wolf.setTamed(false);
        wolf.setAngry(true);
        wolf.setInterested(false);

        Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getBaseValue() * 10);
    }

    private static final boolean x = true;
    private static final boolean o = false;
    private static final boolean[][] wingsShape = {
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

    private static void gargoyle(Location loc) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(50, 50, 50), .6f);
        drawWings(loc, dust);
    }

    private static void drawWings(Location loc, Particle.DustOptions dust) {
        double space = 0.24;
        double defX = loc.getX() - (space * wingsShape[0].length / 2) + space;
        double x = defX;
        double y = loc.clone().getY() + 2.8;
        double fire = -((loc.getYaw() + 180) / 60);
        fire += (loc.getYaw() < -180 ? 3.25 : 2.985);

        for (boolean[] booleans : wingsShape) {
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

    private static void drawSpiral(Entity entity, double heightOffset, int delay) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(154, 0, 194), 1.25f);
        new BukkitRunnable() {
            final double spiralRadius = 1;

            double spiral = 0;
            double height = heightOffset;
            double spiralX;
            double spiralZ;

            @Override
            public void run() {
                if (!entity.isValid()) {
                    cancel();
                }

                Location entityLoc = entity.getLocation().clone();
                entityLoc.add(0, 0.75, 0);

                spiralX = spiralRadius * Math.cos(spiral);
                spiralZ = spiralRadius * Math.sin(spiral);
                spiral += 0.25;
                height += .05;
                if (height >= 2.5)
                    height = 0;
                if (entityLoc.getWorld() != null)
                    entityLoc.getWorld().spawnParticle(Particle.REDSTONE, spiralX + entityLoc.getX(), height + entityLoc.getY(), spiralZ + entityLoc.getZ(), 5, dust);
            }
        }.runTaskTimer(Plugin.instance, delay, 0);
    }
}
