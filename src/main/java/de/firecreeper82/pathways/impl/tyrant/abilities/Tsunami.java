package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class Tsunami extends NPCAbility implements Listener {



    public Tsunami(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if(!npc)
            p = pathway.getBeyonder().getPlayer();

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);


        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        Vector dir = p.getLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null)
            return;

        outerloop:
        for (int i = 0; i < 60; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (entity.getType() == EntityType.ARMOR_STAND || entity == p)
                    continue;
                break outerloop;
            }

            loc.add(dir);

            if (loc.getBlock().getType().isSolid()) {
                break;
            }
        }

        useNPCAbility(loc, p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.WATER_BUCKET, "Tsunami", "750", identifier, sequence, p.getName());
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        if(loc.getWorld() == null)
            return;


        Vector dir = caster.getLocation().getDirection().clone().normalize().setY(0).normalize();
        Location sideLoc = caster.getLocation();
        sideLoc.setPitch(0);
        sideLoc.setYaw(sideLoc.getYaw() + 90);
        Vector side = sideLoc.getDirection().normalize();

        Location startLoc = loc.clone().subtract(dir.clone().multiply(32.5)).subtract(side.clone().multiply(30));


        final List<Block> waterBlocksBefore = Util.getWaterBlocksInSquare(loc.getBlock(), 45);

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                for (int j = -10; j < Math.min(Math.sqrt(counter) * 1.5, 30); j++) {
                    Location tempLoc = startLoc.clone().add(0, j, 0);
                    for (int i = -30; i < 30; i++) {
                        tempLoc.add(side);
                        Util.drawParticlesForNearbyPlayers(Particle.DRIP_WATER, tempLoc, 1, .5, .5, .5, 0);
                        Location waterLoc = tempLoc.clone();
                        if(!waterLoc.getBlock().getType().isSolid()) {
                            waterLoc.getBlock().setType(Material.WATER);
                        }
                    }
                }
                startLoc.add(dir);
                counter++;

                if(counter > 65) {
                    cancel();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            final List<Block> waterBlocksAfter = Util.getWaterBlocksInSquare(loc.getBlock(), 45).stream().filter(block -> !waterBlocksBefore.contains(block)).collect(Collectors.toList());
                            for(Block b : waterBlocksAfter) {
                                b.setType(Material.AIR);
                            }
                        }
                    }.runTaskLater(Plugin.instance, 20 * 3);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void removeWater(Block b, int x, int y, int z) {
        if(b.getLocation().add(x, y,z).getBlock().getType() == Material.WATER)
            b.getLocation().add(x, y,z).getBlock().setType(Material.AIR);
    }
}
