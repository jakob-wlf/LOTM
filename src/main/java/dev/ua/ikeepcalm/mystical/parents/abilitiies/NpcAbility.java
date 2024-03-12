package dev.ua.ikeepcalm.mystical.parents.abilitiies;

import dev.ua.ikeepcalm.mystical.parents.Items;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class NpcAbility extends Ability {
    public NpcAbility(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    public abstract void useNPCAbility(Location loc, Entity caster, double multiplier);
}
