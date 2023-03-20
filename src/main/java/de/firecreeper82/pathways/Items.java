package de.firecreeper82.pathways;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Items {

    protected Pathway pathway;
    protected ArrayList<ItemStack> items;
    protected HashMap<Integer, Integer> sequenceItems;

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
        return sequenceItems.get(a.getIdentifier());
    }

    public void createItems() {
    }
}
