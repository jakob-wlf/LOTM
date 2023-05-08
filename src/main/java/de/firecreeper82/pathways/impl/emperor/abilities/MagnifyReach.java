package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MagnifyReach extends Ability implements Listener {
    public int Reach;
    public boolean isMagnifyingReach;

    public MagnifyReach(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void leftClick() {
        if (pathway.getSequence().getCurrentSequence() <= 4) {
            if (p.isSneaking()) {
                if (Reach == 1) {
                    p.sendMessage("Your magnify value cannot be lower than 1!");
                } else {
                    Reach--;
                    p.sendMessage("Set your magnify damage boost to " + Reach);
                }
            } else if (!p.isSneaking()) {
                if (Reach == 3) {
                    p.sendMessage("Your magnify value cannot be higher than 3!");
                } else {
                    Reach++;
                    p.sendMessage("Set your magnify damage boost to " + Reach);
                }
            }
        } else if ((pathway.getSequence().getCurrentSequence() <= 2)) {
            if (p.isSneaking()) {
                if (Reach == 1) {
                    p.sendMessage("Your magnify value cannot be lower than 1!");
                } else {
                    Reach--;
                    p.sendMessage("Set your magnify damage boost to " + Reach);
                }
            } else if (!p.isSneaking()) {
                if (Reach == 5) {
                    p.sendMessage("Your magnify value cannot be higher than 5!");
                } else {
                    Reach++;
                    p.sendMessage("Set your magnify damage boost to " + Reach);
                }
            }
        }
    }

    @Override
    public void useAbility() {
        double maxDistance = 12.0 * Reach;
        if (!isMagnifyingReach) {
            p.sendMessage(pathway.getStringColor() + "You are now magnifying your reach by a total of " + maxDistance + " blocks.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    Vector dir = p.getEyeLocation().getDirection().normalize();
                    Location loc = p.getEyeLocation();
                    if (loc.getWorld() == null) return;
                    LivingEntity target = null;
                    outerloop:
                    for (int i = 0; i < maxDistance; i++) {
                        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                            if (!(entity instanceof LivingEntity e) || entity == p) continue;
                            target = e;
                            break outerloop;
                        }
                        loc.add(dir);
                    }
                    if (target == null) {
                        return;
                    }
                    pathway.getBeyonder().targetedEntity = target;
                    pathway.getBeyonder().isMagnifyingReach = true;

                }
            }.runTaskTimer(Plugin.instance, 0, 0);
        } else {
            pathway.getBeyonder().isMagnifyingReach = false;
        }
    }




    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.GOLDEN_SWORD, "Magnify Reach", "500/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());

    }
}