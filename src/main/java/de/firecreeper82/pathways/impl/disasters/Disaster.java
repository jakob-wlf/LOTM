package de.firecreeper82.pathways.impl.disasters;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public abstract class Disaster {

    protected Player p;

    public Disaster(Player p) {
        this.p = p;
    }

    public abstract void spawnDisaster(Player p, Location loc);

    public abstract ItemStack getItem();
}
