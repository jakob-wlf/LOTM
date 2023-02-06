package de.firecreeper82.pathways;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Items {

    public Pathway pathway;
    public ArrayList<ItemStack> items;
    public HashMap<Integer, Integer> sequenceItems;

    public Items(Pathway pathway) {
        this.pathway = pathway;
    }

    public ArrayList<ItemStack> returnItemsFromSequence(int sequence) {
        return null;
    }

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

    public HashMap<Integer, Integer> getSequenceItems() {
        return sequenceItems;
    }

    public void setSequenceItems(HashMap<Integer, Integer> sequenceItems) {
        this.sequenceItems = sequenceItems;
    }

    public void createItems() {
    }
}
