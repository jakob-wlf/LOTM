package de.firecreeper82.pathways;


import jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;


public abstract class MobUsableAbility extends Ability{

    protected int frequency;

    public MobUsableAbility(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    public MobUsableAbility(int frequency) {
        super(0, null, 0, null);
        this.frequency = frequency;
    }

    public abstract void useAbility(Location startLoc, Location endLoc, double multiplier, Entity user, @Nullable Entity target);

    public int getFrequency() {
        return frequency;
    }
}
