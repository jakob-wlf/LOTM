package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class ElectricShock extends Ability {

    public ElectricShock(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        Location location = p.getEyeLocation();

        Random random = new Random();

        if(location.getWorld() == null)
            return;

        Vector v = p.getEyeLocation().getDirection();
        outerloop: for(int i = 0; i < 30; i++) {
            for(Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
                if(!(entity instanceof LivingEntity target) ||entity == p)
                    continue;
                target.damage(4, p);
                break outerloop;
            }

            location.add(v);
            location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location, 2, random.nextDouble(-.2, .2), random.nextDouble(-.2, .2), random.nextDouble(-.2, .2), 0);
        }
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.IRON_NUGGET, "Electric Shock", "40", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
