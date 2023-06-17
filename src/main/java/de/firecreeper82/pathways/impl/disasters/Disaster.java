package de.firecreeper82.pathways.impl.disasters;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public abstract class Disaster {

    protected LivingEntity e;

    public Disaster(LivingEntity e) {
        this.e = e;
    }

    public abstract void spawnDisaster(LivingEntity e, Location loc);

    public abstract ItemStack getItem();
}
