package de.firecreeper82.handlers.mobs;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
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
                    }

                    mobs.remove(set.getKey(), set.getValue());
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 40);
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
