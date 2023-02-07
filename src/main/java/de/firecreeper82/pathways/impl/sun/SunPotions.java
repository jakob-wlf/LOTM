package de.firecreeper82.pathways.impl.sun;

import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SunPotions extends Potion {

    public SunPotions() {
        name = "sun";
        potionRecipes = new HashMap<>();
        ItemStack[] recipe9 = {
                new ItemStack(Material.AMETHYST_SHARD),
                new ItemStack(Material.SUNFLOWER)
        };
        potionRecipes.put(9, recipe9);
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
                Pathway.getNamesForPathway(name).get(sequence),
                Color.fromBGR(0, 215, 255),
                ""
        );
    }
}
