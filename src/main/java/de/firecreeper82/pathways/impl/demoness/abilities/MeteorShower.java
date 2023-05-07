package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MeteorShower extends Ability {

    public MeteorShower(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        Location loc = p.getEyeLocation();
        Vector dir = p.getEyeLocation().getDirection().normalize();
        World world = loc.getWorld();

        if(world == null)
            return;

        for(int i = 0; i < 200; i++) {
            loc.add(dir);

            if(loc.getBlock().getType().isSolid())
                break;

            if(!world.getNearbyEntities(loc, 1.2, 1.2, 1.2).isEmpty() && !world.getNearbyEntities(loc, 1.2, 1.2, 1.2).contains(p))
                break;
        }

        new de.firecreeper82.pathways.impl.disasters.MeteorShower(p).spawnDisaster(p, loc);
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.FIRE_CHARGE, "Meteor Shower", "200000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
