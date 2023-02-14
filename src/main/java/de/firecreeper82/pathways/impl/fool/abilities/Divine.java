package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Divination;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Divine extends Ability {

    public Divine(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        Divination.divine(pathway.getBeyonder());
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.AMETHYST_SHARD, "Divination", "25", identifier, 9, pathway.getBeyonder().getPlayer().getName());
    }
}
