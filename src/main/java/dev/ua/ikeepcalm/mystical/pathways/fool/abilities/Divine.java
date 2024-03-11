package dev.ua.ikeepcalm.mystical.pathways.fool.abilities;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.mystical.Ability;
import dev.ua.ikeepcalm.mystical.Items;
import dev.ua.ikeepcalm.mystical.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.fool.FoolItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Divine extends Ability {

    public Divine(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        LordOfTheMinecraft.instance.getDivination().divine(pathway.getBeyonder());
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.AMETHYST_SHARD, "Divination", "15", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
