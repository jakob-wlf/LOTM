package de.firecreeper82.pathways.pathways.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FireOfLight extends Ability {
    public FireOfLight(int identifier, Pathway pathway, Player p) {
        super(identifier, pathway, p);
    }

    @Override
    public void useAbility() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 15);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (!lastBlock.getType().isSolid()) {
                continue;
            }
            break;
        }

        //setting the fire
        Location loc = lastBlock.getLocation().add(0, 1, 0);
        if(!loc.getBlock().getType().isSolid())
            loc.getBlock().setType(Material.FIRE);
        loc.add(1, 0, 0);
        if(!loc.getBlock().getType().isSolid())
            loc.getBlock().setType(Material.FIRE);
        loc.add(-2, 0, 0);
        if(!loc.getBlock().getType().isSolid())
            loc.getBlock().setType(Material.FIRE);
        loc.add(1, 0, -1);
        if(!loc.getBlock().getType().isSolid())
            loc.getBlock().setType(Material.FIRE);
        loc.add(0, 0, 2);
        if(!loc.getBlock().getType().isSolid())
            loc.getBlock().setType(Material.FIRE);
        loc.subtract(0, 0, 1);

        loc.add(0.5, 0.5, 0.5);

        final Material[] lightBlock = {loc.getBlock().getType()};
        loc.getBlock().setType(Material.LIGHT);

        AtomicInteger counter = new AtomicInteger();
        new BukkitRunnable() {
            @Override
            public void run() {
                counter.getAndIncrement();

                loc.getWorld().spawnParticle(Particle.FLAME, loc, 50, 0.75, 0.75, 0.75, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 8, 0.75, 0.75, 0.75, 0);

                //damage nearby entities
                ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 2, 2, 2);
                for(Entity entity : nearbyEntities) {
                    if(entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
                        }
                        if(livingEntity.getUniqueId() != pathway.getUuid())
                            livingEntity.setFireTicks(10 * 20);

                    }
                }

                if(counter.get() >= 5 * 20) {
                    loc.getBlock().setType(Material.AIR);
                    cancel();
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    loc.getBlock().setType(lightBlock[0]);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }
}
