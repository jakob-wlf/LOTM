package de.firecreeper82.handlers.spirits;

import jline.internal.Nullable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class Spirit implements Listener {

    protected int spawnRate;
    protected boolean hostile;
    protected boolean visible;
    protected int spawnCount;
    protected ItemStack drop;

    protected double health;
    protected float particleOffset;
    protected LivingEntity entity;

    public Spirit(LivingEntity entity, double health, float particleOffset, int spawnRate, boolean hostile, boolean visible, int spawnCount, @Nullable ItemStack drop) {
        this.entity = entity;
        this.health = health;
        this.particleOffset = particleOffset;
        this.drop = drop;

        this.spawnRate = spawnRate;
        this.hostile = hostile;
        this.visible = visible;
        this.spawnCount = spawnCount;
    }

    public abstract Spirit initNew(LivingEntity entity);

    public abstract void start();
    public abstract void tick();

    public int getSpawnRate() {
        return spawnRate;
    }

    public boolean isHostile() {
        return hostile;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getSpawnCount() {
        return spawnCount;
    }
}
