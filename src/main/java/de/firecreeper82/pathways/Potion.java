package de.firecreeper82.pathways;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class Potion {
    public HashMap<Integer, ItemStack[]> potionRecipes;
    public abstract ItemStack[] getSequencePotion(int sequence);
}
