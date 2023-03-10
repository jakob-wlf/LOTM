package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SpiritBodyThreads extends Ability {
    public SpiritBodyThreads(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {

    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.LEAD, "Spirit Body Threads", "100", identifier, 5, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void onHold() {
        Player p = pathway.getBeyonder().getPlayer();
        for(Entity e : p.getNearbyEntities(60, 35, 60)) {
            if(e == p)
                return;
            Location entityLoc = e.getLocation();
            Location playerLoc = p.getEyeLocation();
            Vector dir = entityLoc.toVector().subtract(playerLoc.toVector()).normalize().multiply(0.5);
            int counter = 0;
            while(playerLoc.distance(entityLoc) > 1.5 && counter < 150) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(58, 58, 58), 1f);
                p.spawnParticle(Particle.REDSTONE, playerLoc, 1, dust);
                playerLoc.add(dir);
                counter++;
            }
        }
    }
}
