package dev.ua.ikeepcalm.handlers.mobs.abilities.sun;

import dev.ua.ikeepcalm.pathways.Items;
import dev.ua.ikeepcalm.pathways.MobUsableAbility;
import dev.ua.ikeepcalm.pathways.Pathway;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class BeamOfLight extends MobUsableAbility {
    public BeamOfLight(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    public BeamOfLight(int frequency) {
        super(frequency);
    }

    @Override
    public void useAbility() {

    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public void useAbility(Location startLoc, Location endLoc, double multiplier, Entity user, Entity target) {

    }
}
