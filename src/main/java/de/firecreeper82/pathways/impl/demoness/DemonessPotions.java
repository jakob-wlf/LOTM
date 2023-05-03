package de.firecreeper82.pathways.impl.demoness;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class DemonessPotions extends Potion {

    public DemonessPotions() {
        name = "demoness";
        stringColor = "§d";
        mainIngredients = new HashMap<>();
        supplementaryIngredients = new HashMap<>();

        putMainIntoHashMap(9, BeyonderItems.getSunflower(), BeyonderItems.getSirenRock());
        putMainIntoHashMap(8, BeyonderItems.getMagmaHeart());
        putMainIntoHashMap(7, BeyonderItems.getRoosterComb(), BeyonderItems.getSpiritTreeFruit());
        putMainIntoHashMap(6, BeyonderItems.getCrystallizedRoot(), BeyonderItems.getBirdFeather());
        putMainIntoHashMap(5, BeyonderItems.getRedRoosterComb(), BeyonderItems.getWhiteBrillianceRock());
        putMainIntoHashMap(4, BeyonderItems.getTailFeather(), BeyonderItems.getHolyBrillianceRock());
        putMainIntoHashMap(3, Plugin.instance.getCharacteristic().getCharacteristic(3, "sun", stringColor));
        putMainIntoHashMap(2, Plugin.instance.getCharacteristic().getCharacteristic(2, "sun", stringColor));
        putMainIntoHashMap(1, Plugin.instance.getCharacteristic().getCharacteristic(1, "sun", stringColor));

        putSupplIntoHashMap(9, new ItemStack(Material.GRASS), new ItemStack(Material.SUNFLOWER));
        putSupplIntoHashMap(8, new ItemStack(Material.SUNFLOWER), new ItemStack(Material.SWEET_BERRIES));
        putSupplIntoHashMap(7, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.FEATHER));
        putSupplIntoHashMap(6, BeyonderItems.getSunflower(), new ItemStack(Material.FERN), new ItemStack(Material.GLOW_BERRIES));
        putSupplIntoHashMap(5, new ItemStack(Material.MAGMA_CREAM), new ItemStack(Material.SUNFLOWER));
        putSupplIntoHashMap(4, BeyonderItems.getRedRoosterComb(), BeyonderItems.getWhiteBrillianceRock());
        putSupplIntoHashMap(3);
        putSupplIntoHashMap(2);
        putSupplIntoHashMap(1);
    }

    @Override
    public ItemStack returnPotionForSequence(int sequence) {
        return Potion.createPotion(
                "§d",
                sequence,
                Objects.requireNonNull(Pathway.getNamesForPathway(name)).get(sequence),
                Color.fromBGR(32, 165, 218),
                ""
        );
    }
}
