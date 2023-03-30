package de.firecreeper82.handlers.mobs;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Objects;

public class BeyonderMobs {


    private final HashMap<String, Entity> mobs = new HashMap<>();

    public BeyonderMobs() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(String id : mobs.keySet()) {
                    switch (id) {
                        case "wraith" -> wraith(mobs.get(id));
                        case "gargoyle" -> gargoyle(mobs.get(id));
                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 40);
    }

    public void addMob(@NonNull Entity mob, @NonNull String id) {
        mobs.put(id, mob);
    }

    private void wraith(Entity entity) {

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

                drawWings(entity.getLocation());
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
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
                        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc, 1, 0.02, 0.02, 0.02, dust);
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
