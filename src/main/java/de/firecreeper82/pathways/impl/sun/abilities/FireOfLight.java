package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Objects;

public class FireOfLight extends NPCAbility {

    private final boolean npc;

    public FireOfLight(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        this.npc = npc;
    }

    @Override
    public void useNPCAbility(Location target, Entity caster, double multiplier) {
        if(!target.getBlock().getType().isSolid())
            target.getBlock().setType(Material.FIRE);
        target.add(1, 0, 0);
        if(!target.getBlock().getType().isSolid())
            target.getBlock().setType(Material.FIRE);
        target.add(-2, 0, 0);
        if(!target.getBlock().getType().isSolid())
            target.getBlock().setType(Material.FIRE);
        target.add(1, 0, -1);
        if(!target.getBlock().getType().isSolid())
            target.getBlock().setType(Material.FIRE);
        target.add(0, 0, 2);
        if(!target.getBlock().getType().isSolid())
            target.getBlock().setType(Material.FIRE);
        target.subtract(0, 0, 1);

        target.add(0.5, 0.5, 0.5);

        final Material[] lightBlock = {target.getBlock().getType()};
        target.getBlock().setType(Material.LIGHT);

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;

                Objects.requireNonNull(target.getWorld()).spawnParticle(Particle.FLAME, target, 50, 0.75, 0.75, 0.75, 0);
                target.getWorld().spawnParticle(Particle.END_ROD, target, 8, 0.75, 0.75, 0.75, 0);

                //damage nearby entities
                ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) target.getWorld().getNearbyEntities(target, 2, 2, 2);
                for(Entity entity : nearbyEntities) {
                    if(entity instanceof LivingEntity livingEntity) {
                        if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                            ((Damageable) entity).damage(10 * multiplier, caster);
                            livingEntity.setFireTicks(10 * 20);
                        }
                        if(entity != caster)
                            livingEntity.setFireTicks(10 * 20);

                    }
                }

                if(counter >= 5 * 20) {
                    target.getBlock().setType(Material.AIR);
                    cancel();
                    if(!npc)
                        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    target.getBlock().setType(lightBlock[0]);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public void useAbility() {
        double multiplier = getMultiplier();

        p = pathway.getBeyonder().getPlayer();
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

        useNPCAbility(loc, p, multiplier);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.BLAZE_POWDER, "Fire of Light", "20", identifier, 7, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}