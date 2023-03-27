package de.firecreeper82.pathways.impl.fool;

import de.firecreeper82.lotm.util.BeyonderItems;
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

        ItemStack[] recipe9 = {
                BeyonderItems.getLavosSquidBlood(),
                BeyonderItems.getStellarAquaCrystal()
        };
        mainIngredients.put(9, recipe9);
        ItemStack[] recipe8 = {
                new ItemStack(Material.GOAT_HORN),
                new ItemStack(Material.ROSE_BUSH)
        };
        mainIngredients.put(8, recipe8);
        ItemStack[] recipe7 = {
                new ItemStack(Material.DARK_OAK_SAPLING),
                new ItemStack(Material.BLACK_DYE)
        };
        mainIngredients.put(7, recipe7);
        ItemStack[] recipe6 = {
                new ItemStack(Material.PHANTOM_MEMBRANE),
                new ItemStack(Material.SKELETON_SKULL)
        };
        mainIngredients.put(6, recipe6);
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

    @Override
    public ItemStack[] getSequencePotion(int sequence) {
        return mainIngredients.get(sequence);
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
