package dev.ua.ikeepcalm.entities.abilities.sun;

import dev.ua.ikeepcalm.mystical.Items;
import dev.ua.ikeepcalm.mystical.MobAbility;
import dev.ua.ikeepcalm.mystical.Pathway;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class BeamOfLight extends MobAbility {
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
