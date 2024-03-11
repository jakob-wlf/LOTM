package dev.ua.ikeepcalm.pathways.impl.door.abilities;

import dev.ua.ikeepcalm.Plugin;
import dev.ua.ikeepcalm.pathways.Ability;
import dev.ua.ikeepcalm.pathways.Items;
import dev.ua.ikeepcalm.pathways.Pathway;
import dev.ua.ikeepcalm.pathways.impl.door.DoorItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Divine extends Ability {

    public Divine(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        Plugin.instance.getDivination().divine(pathway.getBeyonder());
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.DIAMOND, "Divination", "15", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
