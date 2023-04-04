package de.firecreeper82.pathways.impl.sun;

import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class SunPotions extends Potion {

    public SunPotions() {
        name = "sun";
        stringColor = "§6";
        mainIngredients = new HashMap<>();
        supplementaryIngredients = new HashMap<>();

        putMainIntoHashMap(9, BeyonderItems.getSunflower(), BeyonderItems.getSirenRock());
        putMainIntoHashMap(8, BeyonderItems.getMagmaHeart());
        putMainIntoHashMap(7, BeyonderItems.getRoosterComb(), BeyonderItems.getSpiritTreeFruit());

        putSupplIntoHashMap(9, new ItemStack(Material.GRASS), new ItemStack(Material.SUNFLOWER));
        putSupplIntoHashMap(8, new ItemStack(Material.SUNFLOWER), new ItemStack(Material.SWEET_BERRIES));
        putSupplIntoHashMap(7, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.FEATHER));


        ItemStack[] recipe6 = {
                new ItemStack(Material.COPPER_INGOT),
                new ItemStack(Material.RAW_COPPER)
        };
        mainIngredients.put(6, recipe6);
        ItemStack[] recipe5 = {
                new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.RAW_IRON)
        };
        mainIngredients.put(5, recipe5);
        ItemStack[] recipe4 = {
                new ItemStack(Material.NETHERITE_INGOT),
                new ItemStack(Material.NETHERITE_SCRAP)
        };
        mainIngredients.put(4, recipe4);
        ItemStack[] recipe3 = {
                new ItemStack(Material.DIAMOND),
                new ItemStack(Material.EMERALD)
        };
        mainIngredients.put(3, recipe3);
        ItemStack[] recipe2 = {
                new ItemStack(Material.BLAZE_ROD),
                new ItemStack(Material.GOLD_NUGGET)
        };
        mainIngredients.put(2, recipe2);
        ItemStack[] recipe1 = {
                new ItemStack(Material.REDSTONE_TORCH),
                new ItemStack(Material.REDSTONE_ORE)
        };
        mainIngredients.put(1, recipe1);
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
