package de.firecreeper82.handlers.mobs;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public record CustomEntity(String name, String id, int rarity, ItemStack drop,
                           EntityType entityType, Integer maxHealth) {
}
