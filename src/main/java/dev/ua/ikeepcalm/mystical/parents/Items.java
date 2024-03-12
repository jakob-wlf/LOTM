package dev.ua.ikeepcalm.mystical.parents;

import cz.foresttech.api.ColorAPI;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.Ability;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Items {

    protected Pathway pathway;
    protected ArrayList<ItemStack> items;
    protected HashMap<Integer, Integer> sequenceItems;
    protected HashMap<Integer, String[]> abilityInfo;

    public void addToSequenceItems(int index, int value) {
        sequenceItems.put(index, value);
    }

    public Items(Pathway pathway) {
        this.pathway = pathway;
    }

    public abstract ArrayList<ItemStack> returnItemsFromSequence(int sequence);

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemStack> items) {
        this.items = items;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public void setPathway(Pathway pathway) {
        this.pathway = pathway;
    }

    @SuppressWarnings("unused")
    public HashMap<Integer, Integer> getSequenceItems() {
        return sequenceItems;
    }

    @SuppressWarnings("unused")
    public void setSequenceItems(HashMap<Integer, Integer> sequenceItems) {
        this.sequenceItems = sequenceItems;
    }

    public int getSequenceOfAbility(Ability a) {
        return sequenceItems.get(a.getIdentifier() - 1);
    }

    public abstract void createItems();

    public abstract void initializeAbilityInfos();

    protected String[] formatAbilityInfo(String pathwayColor, String sequenceName, String... s) {
        String[] formatted = new String[3 + s.length];
        formatted[0] = ColorAPI.colorize(pathwayColor + sequenceName + " -- Вміння");
        formatted[1] = ColorAPI.colorize("§6-----------------------------");
        for (int i = 0; i < s.length; i++) {
            s[i] = ColorAPI.colorize(s[i]);
        }
        formatted[2 + s.length] = ColorAPI.colorize("§6-----------------------------");

        System.arraycopy(s, 0, formatted, 2, s.length);

        return formatted;
    }

    public HashMap<Integer, String[]> getAbilityInfo() {
        return abilityInfo;
    }
}
