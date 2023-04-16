package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import de.firecreeper82.pathways.impl.fool.FoolItems;
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
