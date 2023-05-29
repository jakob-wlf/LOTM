package de.firecreeper82.pathways.impl.fool.abilities.grafting;


import de.firecreeper82.lotm.Plugin;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class EntityToEntity {

    private boolean stopped;
    private final Entity entity;

    public EntityToEntity(Entity entity, Entity target) {
        this.entity = entity;
        stopped = false;
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;

                if (stopped || counter > 20 * 60 * 60 || entity == null || !entity.isValid() || target == null || !target.isValid()) {
                    cancel();
                    return;
                }

                if (target.getLocation().distance(entity.getLocation()) > 3 || target.getLocation().getWorld() != entity.getWorld()) {
                    entity.teleport(target.getLocation());
                } else {
                    Vector dir = target.getLocation().toVector().subtract(entity.getLocation().toVector());
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