package de.firecreeper82.pathways;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;
import java.util.HashMap;

public abstract class Potion {
    protected String name;
    protected String stringColor;

    protected HashMap<Integer, ItemStack[]> mainIngredients;
    protected HashMap<Integer, ItemStack[]> supplementaryIngredients;

    public ItemStack[] getMainIngredients(int sequence) {
        return mainIngredients.get(sequence);
    }

    public ItemStack[] getSupplIngredients(int sequence) {
        return supplementaryIngredients.get(sequence);
    }

    public abstract ItemStack returnPotionForSequence(int sequence);

    public static ItemStack createPotion(String stringColor, int sequence, String name, Color color, String ritual) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setDisplayName(stringColor + "Sequence " + sequence + ": " + name);
        potionMeta.setColor(color);
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        String[] lore;
        if (ritual.equals("")) {
            lore = new String[]{
                    "§5Drink this Potion to gain the powers",
                    "§5of a Sequence " + sequence + ": " + name
            };
        } else {
            lore = new String[]{
                    "§5Drink this Potion to gain the powers",
                    "§5of a Sequence " + sequence + ": " + name,
                    "§5Ritual: " + ritual
            };
        }
        potionMeta.setLore(Arrays.asList(lore));
        potion.setItemMeta(potionMeta);
        return potion;
    }

    public String getName() {
        return name;
    }

    public String getStringColor() {
        return stringColor;
    }

    public void putMainIntoHashMap(int sequence, ItemStack... ingredients) {
        mainIngredients.put(sequence, ingredients);
    }

    public void putSupplIntoHashMap(int sequence, ItemStack... ingredients) {
        supplementaryIngredients.put(sequence, ingredients);
    }

}
