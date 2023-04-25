package de.firecreeper82.handlers.spirits;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public abstract class Spirit implements Listener {

    protected int spawnRate;
    protected LivingEntity entity;
    protected boolean hostile;
    protected double health;
    protected boolean visible;
    protected float particleOffset;

    protected boolean canceled;

    public Spirit(int spawnRate, boolean hostile, double health, boolean visible, float particleOffset) {
        this.spawnRate = spawnRate;
        this.hostile = hostile;
        this.health = health;
        this.visible = visible;
        this.particleOffset = particleOffset;

        canceled = false;

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    protected abstract void start();

    protected abstract void tick();

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        EntityType[] spawnTypes = new EntityType[] {
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.PIGLIN,
                EntityType.MAGMA_CUBE,
                EntityType.HOGLIN,
                EntityType.PIGLIN,
                EntityType.STRIDER,
                EntityType.GHAST
        };
        if(!Arrays.asList(spawnTypes).contains(e.getEntity().getType()))
            return;

        Random random = new Random();

        if(random.nextInt(spawnRate) != 0)
            return;

        entity = (LivingEntity) ((hostile) ?  e.getEntity().getWorld().spawnEntity(e.getLocation(), EntityType.VEX) : e.getEntity().getWorld().spawnEntity(e.getLocation(), EntityType.ALLAY));
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
        entity.setHealth(health);

        entity.setInvisible(!visible);

        start();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isValid())
                    canceled = true;

                if(canceled)
                    cancel();

                tick();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }
}
