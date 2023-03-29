package de.firecreeper82.pathways.impl.fool;

import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.lotm.util.UtilItems;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class FoolPotions extends Potion {

    public FoolPotions() {
        name = "fool";
        mainIngredients = new HashMap<>();
        supplementaryIngredients = new HashMap<>();

        putMainIntoHashMap(9, BeyonderItems.getLavosSquidBlood(), BeyonderItems.getStellarAquaCrystal());
        putMainIntoHashMap(8, BeyonderItems.getGoatHorn(), BeyonderItems.getRose());
        putMainIntoHashMap(7, BeyonderItems.getRoot(), BeyonderItems.getPanther());
        putMainIntoHashMap(6, BeyonderItems.getPituitaryGland(), BeyonderItems.getShadowCharacteristic());

        putSupplIntoHashMap(9, new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.MUTTON));
        putSupplIntoHashMap(8, UtilItems.getWaterPotion(), new ItemStack(Material.SUNFLOWER), new ItemStack(Material.GRASS));
        putSupplIntoHashMap(7, UtilItems.getMundanePotion(), new ItemStack(Material.AMETHYST_SHARD), new ItemStack(Material.GLOW_INK_SAC));
        putSupplIntoHashMap(6, new ItemStack(Material.SOUL_SAND), new ItemStack(Material.SOUL_SAND), new ItemStack(Material.SOUL_SAND));


        ItemStack[] recipe5 = {
                new ItemStack(Material.GUNPOWDER),
                new ItemStack(Material.GHAST_TEAR)
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

    public void putMainIntoHashMap(int sequence, ItemStack... ingredients) {
        mainIngredients.put(sequence, ingredients);
    }

    public void putSupplIntoHashMap(int sequence, ItemStack... ingredients) {
        supplementaryIngredients.put(sequence, ingredients);
    }


    @Override
    public ItemStack returnPotionForSequence(int sequence) {
        return Potion.createPotion(
                "§5",
                sequence,
                Objects.requireNonNull(Pathway.getNamesForPathway(name)).get(sequence),
                Color.fromBGR(128, 0, 128),
                ""
        );
    }
}
