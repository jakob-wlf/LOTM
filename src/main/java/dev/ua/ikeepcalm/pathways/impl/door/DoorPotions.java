package dev.ua.ikeepcalm.pathways.impl.door;

import dev.ua.ikeepcalm.Plugin;
import dev.ua.ikeepcalm.utils.BeyonderItems;
import dev.ua.ikeepcalm.utils.UtilItems;
import dev.ua.ikeepcalm.pathways.Pathway;
import dev.ua.ikeepcalm.pathways.Potion;
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

        putMainIntoHashMap(9, BeyonderItems.getMutatedDoor());
        putMainIntoHashMap(8, BeyonderItems.getMarlinBlood(), BeyonderItems.getSpiritPouch());
        putMainIntoHashMap(7, BeyonderItems.getLavosSquidBlood(), BeyonderItems.getMeteoriteCrystal());
        putMainIntoHashMap(6, BeyonderItems.getWraithDust());
        putMainIntoHashMap(5, Plugin.instance.getCharacteristic().getCharacteristic(5, "door", stringColor));
        putMainIntoHashMap(4, Plugin.instance.getCharacteristic().getCharacteristic(4, "door", stringColor));
        putMainIntoHashMap(3, Plugin.instance.getCharacteristic().getCharacteristic(3, "door", stringColor));
        putMainIntoHashMap(2, Plugin.instance.getCharacteristic().getCharacteristic(2, "door", stringColor));
        putMainIntoHashMap(1, Plugin.instance.getCharacteristic().getCharacteristic(1, "door", stringColor));

        putSupplIntoHashMap(9, new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.OAK_DOOR));
        putSupplIntoHashMap(8, UtilItems.getMundanePotion(), new ItemStack(Material.ROSE_BUSH));
        putSupplIntoHashMap(7, new ItemStack(Material.BEEF), new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK));
        putSupplIntoHashMap(6, new ItemStack(Material.BOOK), new ItemStack(Material.FEATHER));
        putSupplIntoHashMap(5);
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
                Color.fromBGR(255, 251, 0),
                ""
        );
    }
}
