package dev.ua.ikeepcalm.entities.beyonders;

import jline.internal.Nullable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public abstract class Spirit {

    protected final int spawnRate;
    protected final EntityType entityType;
    protected final boolean visible;
    protected final int spawnCount;
    protected final ItemStack drop;
    protected final boolean undead;
    protected final String name;

    protected final double health;
    protected final float particleOffset;
    protected final LivingEntity entity;

    public Spirit(LivingEntity entity, double health, float particleOffset, int spawnRate, EntityType entityType, boolean visible, int spawnCount, @Nullable ItemStack drop, boolean undead, String name) {
        this.entity = entity;
        this.health = health;
        this.particleOffset = particleOffset;
        this.drop = drop;

        this.spawnRate = spawnRate;
        this.entityType = entityType;
        this.visible = visible;
        this.spawnCount = spawnCount;
        this.undead = undead;
        this.name = name;
    }

    public abstract Spirit initNew(LivingEntity entity);

    public abstract void start();

    public abstract void tick();

    public void stop() {
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public boolean isInvisible() {
        return !visible;
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public boolean isUndead() {
        return undead;
    }

    public String getName() {
        return name;
    }
}
