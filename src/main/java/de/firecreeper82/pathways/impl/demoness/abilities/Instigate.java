package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Instigate extends Ability {
    public Instigate(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    @Override
    public void useAbility() {

    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.STONE_SWORD, "Instigate", "65", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
