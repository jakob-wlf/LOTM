package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FogOfHistory extends Ability {


    public FogOfHistory(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.QUARTZ, "Fog of History", "100", identifier, 3, pathway.getBeyonder().getPlayer().getName());
    }
}
