package de.firecreeper82.pathways.impl.door;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class DoorPotions extends Potion {

    public DoorPotions() {
        name = "door";
        stringColor = "§b";
        mainIngredients = new HashMap<>();
        supplementaryIngredients = new HashMap<>();

        putMainIntoHashMap(9, BeyonderItems.getSunflower(), BeyonderItems.getSirenRock());
        putMainIntoHashMap(8, BeyonderItems.getMagmaHeart());
        putMainIntoHashMap(7, BeyonderItems.getLavosSquidBlood(), BeyonderItems.getMeteoriteCrystal());
        putMainIntoHashMap(6, BeyonderItems.getWraithDust(), BeyonderItems.getBirdFeather());
        putMainIntoHashMap(5, BeyonderItems.getRedRoosterComb(), BeyonderItems.getWhiteBrillianceRock());
        putMainIntoHashMap(4, Plugin.instance.getCharacteristic().getCharacteristic(3, "door", stringColor));
        putMainIntoHashMap(3, Plugin.instance.getCharacteristic().getCharacteristic(3, "door", stringColor));
        putMainIntoHashMap(2, Plugin.instance.getCharacteristic().getCharacteristic(2, "door", stringColor));
        putMainIntoHashMap(1, Plugin.instance.getCharacteristic().getCharacteristic(1, "door", stringColor));

        putSupplIntoHashMap(9, new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.OAK_DOOR));
        putSupplIntoHashMap(8, new ItemStack(Material.BIRCH_DOOR), new ItemStack(Material.GUNPOWDER));
        putSupplIntoHashMap(7, new ItemStack(Material.BEEF), new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK));
        putSupplIntoHashMap(6, BeyonderItems.getSunflower(), new ItemStack(Material.FERN), new ItemStack(Material.GLOW_BERRIES));
        putSupplIntoHashMap(5, new ItemStack(Material.MAGMA_CREAM), new ItemStack(Material.SUNFLOWER));
        putSupplIntoHashMap(4);
        putSupplIntoHashMap(3);
        putSupplIntoHashMap(2);
        putSupplIntoHashMap(1);
    }

    @Override
    public ItemStack returnPotionForSequence(int sequence) {
        return Potion.createPotion(
                "§b",
                sequence,
                Objects.requireNonNull(Pathway.getNamesForPathway(name)).get(sequence),
                Color.fromBGR(32, 165, 218),
                ""
        );
    }
}
