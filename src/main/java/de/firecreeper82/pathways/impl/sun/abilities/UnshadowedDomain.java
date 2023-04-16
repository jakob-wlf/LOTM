package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class UnshadowedDomain extends Recordable {
    public UnshadowedDomain(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        if(!recorded)
            pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        destroy(beyonder, recorded);

        Location loc = p.getLocation();
        ArrayList<Block> blocks = new ArrayList<>();

        int radius = 32;
        for(int i = 15; i > -15; i--) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if( (x*x) + (z*z) <= Math.pow(radius, 2)) {
                        Block block = p.getWorld().getBlockAt((int) loc.getX() + x, (int) loc.getY() + i, (int) loc.getZ() + z);
                        if(block.getType() == Material.AIR && block.getLocation().clone().subtract(0, 1, 0).getBlock().getType().isSolid()) {
                            block.setType(Material.LIGHT);
                            blocks.add(block);
                        }
                    }
                }
            }
        }

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;

                Particle.DustOptions dustSphere = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc, 65, 40, 40, 40, 0, dustSphere);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 65, 40, 40, 40, 0);

                for(Entity entity : loc.getWorld().getNearbyEntities(loc, 30, 30, 30)) {
                    if(entity instanceof LivingEntity) {
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 1));
                    }
                }

                if(counter > 20 * 20) {
                    for(Block b : blocks) {
                        b.setType(Material.AIR);
                    }
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);

    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.SHROOMLIGHT, "Unshadowed Domain", "350", identifier, 4, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
