package de.firecreeper82.handlers.blocks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public record CustomBlock(Material type, ItemStack drop, int rarity, String id) {
}
