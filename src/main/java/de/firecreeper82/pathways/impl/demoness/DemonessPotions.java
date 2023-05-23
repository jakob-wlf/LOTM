package de.firecreeper82.pathways.impl.demoness;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class DemonessPotions extends Potion {

    public DemonessPotions() {
        name = "demoness";
        stringColor = "§d";
        mainIngredients = new HashMap<>();
        supplementaryIngredients = new HashMap<>();

        putMainIntoHashMap(9, Plugin.instance.getCharacteristic().getCharacteristic(9, "demoness", stringColor));
        putMainIntoHashMap(8, Plugin.instance.getCharacteristic().getCharacteristic(8, "demoness", stringColor));
        putMainIntoHashMap(7, Plugin.instance.getCharacteristic().getCharacteristic(7, "demoness", stringColor));
        putMainIntoHashMap(6, Plugin.instance.getCharacteristic().getCharacteristic(6, "demoness", stringColor));
        putMainIntoHashMap(5, Plugin.instance.getCharacteristic().getCharacteristic(5, "demoness", stringColor));
        putMainIntoHashMap(4, Plugin.instance.getCharacteristic().getCharacteristic(4, "demoness", stringColor));
        putMainIntoHashMap(3, Plugin.instance.getCharacteristic().getCharacteristic(3, "demoness", stringColor));
        putMainIntoHashMap(2, Plugin.instance.getCharacteristic().getCharacteristic(2, "demoness", stringColor));
        putMainIntoHashMap(1, Plugin.instance.getCharacteristic().getCharacteristic(1, "demoness", stringColor));

        putSupplIntoHashMap(9);
        putSupplIntoHashMap(8);
        putSupplIntoHashMap(7);
        putSupplIntoHashMap(6);
        putSupplIntoHashMap(5);
        putSupplIntoHashMap(4);
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
