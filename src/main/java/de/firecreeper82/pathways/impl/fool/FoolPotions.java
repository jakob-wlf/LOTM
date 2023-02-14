package de.firecreeper82.pathways.impl.fool;

import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class FoolPotions extends Potion {

    public FoolPotions() {
        name = "sun";
        potionRecipes = new HashMap<>();
        ItemStack[] recipe9 = {
                new ItemStack(Material.AMETHYST_SHARD),
                new ItemStack(Material.SUNFLOWER)
        };
        potionRecipes.put(9, recipe9);
        ItemStack[] recipe8 = {
                new ItemStack(Material.NETHER_GOLD_ORE),
                new ItemStack(Material.MAGMA_BLOCK)
        };
        potionRecipes.put(8, recipe8);
        ItemStack[] recipe7 = {
                new ItemStack(Material.GOLD_INGOT),
                new ItemStack(Material.RAW_GOLD)
        };
        potionRecipes.put(7, recipe7);
        ItemStack[] recipe6 = {
                new ItemStack(Material.COPPER_INGOT),
                new ItemStack(Material.RAW_COPPER)
        };
        potionRecipes.put(6, recipe6);
        ItemStack[] recipe5 = {
                new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.RAW_IRON)
        };
        potionRecipes.put(5, recipe5);
        ItemStack[] recipe4 = {
                new ItemStack(Material.NETHERITE_INGOT),
                new ItemStack(Material.NETHERITE_SCRAP)
        };
        potionRecipes.put(4, recipe4);
        ItemStack[] recipe3 = {
                new ItemStack(Material.DIAMOND),
                new ItemStack(Material.EMERALD)
        };
        potionRecipes.put(3, recipe3);
        ItemStack[] recipe2 = {
                new ItemStack(Material.BLAZE_ROD),
                new ItemStack(Material.GOLD_NUGGET)
        };
        potionRecipes.put(2, recipe2);
        ItemStack[] recipe1 = {
                new ItemStack(Material.REDSTONE_TORCH),
                new ItemStack(Material.REDSTONE_ORE)
        };
        potionRecipes.put(1, recipe1);
    }

    @Override
    public ItemStack[] getSequencePotion(int sequence) {
        return potionRecipes.get(sequence);
    }

    @Override
    public ItemStack returnPotionForSequence(int sequence) {
        return Potion.createPotion(
                "§6",
                sequence,
                Objects.requireNonNull(Pathway.getNamesForPathway(name)).get(sequence),
                Color.fromBGR(32, 165, 218),
                ""
        );
    }
}
