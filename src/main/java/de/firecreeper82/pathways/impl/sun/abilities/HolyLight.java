package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Objects;

public class HolyLight extends Ability {

    public HolyLight(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        p = pathway.getBeyonder().getPlayer();

        double multiplier = getMultiplier();

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 15);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        Location loc = lastBlock.getLocation();
        loc.add(0, 14, 0);

        final Material[] lastMaterial = {loc.getBlock().getType()};
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                for (double i = 0; i < 3.2; i += 0.8) {
                    for (int j = 0; j < 50; j++) {
                        double x = i * Math.cos(j);
                        double z = i * Math.sin(j);
                        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, dust);
                        loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY() + 1, loc.getZ() + z, 1, 0, 0, 0, 0);
                    }
                }

                loc.getBlock().setType(lastMaterial[0]);
                loc.subtract(0, 0.75, 0);
                lastMaterial[0] = loc.getBlock().getType();
                loc.getBlock().setType(Material.LIGHT);

                if ((lastMaterial[0].isSolid() && counter >= 18.6) || counter >= 200) {
                    loc.getBlock().setType(lastMaterial[0]);
                    counter = 0;
                    cancel();
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;

                    //damage nearby entities
                    ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc.subtract(5, 0, 5), 10, 10, 10);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof LivingEntity livingEntity) {
                            if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                ((Damageable) entity).damage(15 * multiplier, p);
                            } else {
                                if (entity != p)
                                    ((Damageable) entity).damage(8 * multiplier, p);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.GLOWSTONE_DUST, "Holy Light", "45", identifier, 8, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
