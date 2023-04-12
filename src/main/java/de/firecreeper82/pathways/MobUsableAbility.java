package de.firecreeper82.pathways;


import org.bukkit.Location;

public abstract class MobUsableAbility extends Ability{

    public MobUsableAbility(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    public MobUsableAbility() {
        super(0, null, 0, null);
    }

    public abstract void useAbility(Location location);
}
