package de.firecreeper82.handlers.blocks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public record CustomBlock(@NonNull Material type, @NonNull ItemStack drop, int rarity, @NonNull String id) {
}
