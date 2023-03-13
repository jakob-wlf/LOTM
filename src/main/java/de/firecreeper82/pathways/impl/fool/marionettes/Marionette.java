package de.firecreeper82.pathways.impl.fool.marionettes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

import java.util.Objects;

public class Marionette extends Zombie {

    public Marionette(Location location) {
        super(EntityType.ZOMBIE, ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle());

        setPos(location.getX(), location.getY(), location.getZ());
        level.addFreshEntity(this);
    }

    public static Marionette spawnMarionette(Location loc) {
        return new Marionette(loc);
    }
}
