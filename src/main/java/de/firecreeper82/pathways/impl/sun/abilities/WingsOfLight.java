package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class WingsOfLight extends Ability {
    public WingsOfLight(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.FEATHER, "Wings of Light", "5000/s", identifier, 3, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
