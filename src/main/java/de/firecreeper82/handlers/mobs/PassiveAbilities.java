package de.firecreeper82.handlers.mobs;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class PassiveAbilities {

    public static void passiveAbility(String id, Entity entity) {
        switch (id) {
            case "rooster" -> rooster(entity);
        }
    }

    private static void rooster(Entity entity) {
        final HashMap<Long, Location> burnedLocations = new HashMap<>();
        Random random = new Random();
        new BukkitRunnable() {
            @Override
            public void run() {
                burnedLocations.put(System.currentTimeMillis(), entity.getLocation());

                for (Location loc : burnedLocations.values()) {
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc, 2, .05, .05, .05, 0);
                    if (loc.getWorld().getNearbyEntities(loc, 1, 1, 1).isEmpty())
                        continue;
                    for (Entity e : loc.getWorld().getNearbyEntities(loc, .5, .5, .5)) {
                        if (!(e instanceof Damageable damageable) || damageable == entity)
                            continue;
                        damageable.damage(6, entity);
                    }
                }

                try {
                    for (Long l : burnedLocations.keySet()) {
                        long temp = l;
                        if ((temp + (1000 * 5)) < System.currentTimeMillis())
                            burnedLocations.remove(l);
                    }
                } catch (Exception ignored) {
                }

                if (random.nextInt(20 * 60 * 3) == 0) {
                    entity.getWorld().dropItem(entity.getLocation(), BeyonderItems.getRoosterComb());
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);

    }
}
