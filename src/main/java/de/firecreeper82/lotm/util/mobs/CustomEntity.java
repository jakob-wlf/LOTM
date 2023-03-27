package de.firecreeper82.lotm.util.mobs;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public record CustomEntity(String name, String id, int rarity, ItemStack drop,
                           EntityType entityType, Integer maxHealth) {

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ItemStack getDrop() {
        return drop;
    }
}
