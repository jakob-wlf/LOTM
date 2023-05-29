package de.firecreeper82.pathways.impl.fool.abilities.grafting;


import de.firecreeper82.lotm.Plugin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class EntityToLocation {

    private boolean stopped;
    private final Entity entity;

    public EntityToLocation(Entity entity, Location location) {
        this.entity = entity;
        stopped = false;
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;

                if (stopped || counter > 20 * 60 * 60 || entity == null || location == null || !entity.isValid()) {
                    cancel();
                    return;
                }

                if (location.distance(entity.getLocation()) > 3 || location.getWorld() != entity.getWorld()) {
                    entity.teleport(location);
                } else {
                    Vector dir = location.toVector().subtract(entity.getLocation().toVector());
                    entity.setVelocity(dir);
                }

            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    public void stop() {
        stopped = true;
    }

    public Entity getEntity() {
        return entity;
    }
}