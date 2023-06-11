package de.firecreeper82.pathways.impl.door.abilities;


import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Record extends Ability {


    public Record(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.BOOK, "Record (temporarily removed)", "15/s", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
