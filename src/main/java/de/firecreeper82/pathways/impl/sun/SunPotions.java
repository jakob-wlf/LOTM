package de.firecreeper82.pathways.impl.sun;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.pathways.Characteristic;
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
        putMainIntoHashMap(6, BeyonderItems.getCrystallizedRoot(), BeyonderItems.getBirdFeather());
        putMainIntoHashMap(2, Plugin.instance.getCharacteristic().getCharacteristic(2, "sun", stringColor));
        putMainIntoHashMap(1, Plugin.instance.getCharacteristic().getCharacteristic(2, "sun", stringColor));

        putSupplIntoHashMap(9, new ItemStack(Material.GRASS), new ItemStack(Material.SUNFLOWER));
        putSupplIntoHashMap(8, new ItemStack(Material.SUNFLOWER), new ItemStack(Material.SWEET_BERRIES));
        putSupplIntoHashMap(7, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.FEATHER));
        putSupplIntoHashMap(6, BeyonderItems.getSunflower(), new ItemStack(Material.FERN), new ItemStack(Material.GLOW_BERRIES));
        putSupplIntoHashMap(2);
        putSupplIntoHashMap(1);



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
