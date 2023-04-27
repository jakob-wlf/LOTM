package de.firecreeper82.handlers.spirits.impl;

import de.firecreeper82.handlers.spirits.Spirit;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class TallSkeleton extends Spirit {


    public TallSkeleton(LivingEntity entity, double health, float particleOffset, int spawnRate, EntityType entityType, boolean visible, int spawnCount, ItemStack drop, boolean undead, String name) {
        super(entity, health, particleOffset, spawnRate, entityType, visible, spawnCount, drop, undead, name);
    }

    @Override
    public Spirit initNew(LivingEntity entity) {
        return new TallSkeleton(entity, health, particleOffset, spawnRate, entityType, visible, spawnCount, drop, undead, name);
    }

    private Skeleton passenger1;
    private Skeleton passenger2;

    @Override
    public void start() {
        passenger1 = (Skeleton) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.SKELETON, false);
        passenger1.setCustomName(name);
        passenger1.setInvulnerable(true);
        Objects.requireNonNull(passenger1.getEquipment()).clear();

        passenger2 = (Skeleton) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.SKELETON, false);
        passenger2.setCustomName(name);
        passenger2.setInvulnerable(true);
        Objects.requireNonNull(passenger2.getEquipment()).clear();

        entity.addPassenger(passenger1);
        passenger1.addPassenger(passenger2);

        Objects.requireNonNull(entity.getEquipment()).clear();
    }

    @Override
    public void tick() {

    }

    @Override
    public void stop() {
        passenger1.remove();
        passenger2.remove();
    }
}
