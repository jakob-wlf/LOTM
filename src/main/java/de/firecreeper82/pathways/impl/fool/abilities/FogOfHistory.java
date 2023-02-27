package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class FogOfHistory extends Ability {

    @SuppressWarnings("all")
    private final ArrayList<ItemStack> summonedItems;

    public FogOfHistory(int identifier, Pathway pathway) {
        super(identifier, pathway);
        summonedItems = new ArrayList<>();
    }

    @Override
    public void useAbility() {

    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.QUARTZ, "Fog of History", "100", identifier, 3, pathway.getBeyonder().getPlayer().getName());
    }
}
