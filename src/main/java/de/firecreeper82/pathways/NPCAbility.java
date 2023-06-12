package de.firecreeper82.pathways;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class NPCAbility extends Ability{
    public NPCAbility(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    public abstract void useNPCAbility(Location loc, Entity caster, double multiplier);
}
