package de.firecreeper82.pathways.impl.fool.abilities.marionetteAbilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SpiritBodyThreads extends NPCAbility implements Listener {


    private boolean controlling;
    private Entity currentEntity;
    private int index;
    int maxDistance = 50;

    List<Entity> nearbyEntities;

    public SpiritBodyThreads(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        if (!npc) {
            p = pathway.getBeyonder().getPlayer();
            items.addToSequenceItems(identifier - 1, sequence);
        }

        controlling = false;
        currentEntity = null;
        index = 0;

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {

    }

    @Override
    public void useAbility() {
        if (controlling) {
            controlling = false;
            return;
        }

        controlling = true;

        new BukkitRunnable() {
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(20, 20, 20), 1.25f);
            @Override
            public void run() {
                if(currentEntity == null || !currentEntity.isValid() || !controlling || p == null || !p.isValid()) {
                    controlling = false;
                    cancel();
                    return;
                }
                drawLineToEntity(p.getEyeLocation(), currentEntity.getLocation().add(0, .5, 0), dust);
            }

        }.runTaskTimer(Plugin.instance, 0, 0);

    }

    @Override
    public void onHold() {
        if (p == null || nearbyEntities == null || currentEntity == null || controlling)
            return;

        Location startLoc = p.getEyeLocation();
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(221, 0, 255), .75f);
        Particle.DustOptions dustForSelected = new Particle.DustOptions(Color.fromRGB(255, 255, 255), .75f);

        if (currentEntity.getLocation().distance(startLoc) > maxDistance) {
            nearbyEntities.remove(currentEntity);
            if (nearbyEntities.isEmpty())
                currentEntity = null;
            else {
                index = 0;
                currentEntity = nearbyEntities.get(0);
            }
        }

        nearbyEntities.removeIf(entity -> entity.getLocation().distance(startLoc) > maxDistance);

        if (nearbyEntities.isEmpty())
            return;

        for (Entity entity : nearbyEntities) {
            if (entity == p)
                continue;

            if (entity == currentEntity)
                drawLineToEntity(startLoc, entity.getLocation().add(0, .5, 0), dustForSelected);
            else
                drawLineToEntity(startLoc, entity.getLocation().add(0, .5, 0), dust);
        }
    }

    @Override
    public void leftClick() {
        index++;
        if (index >= nearbyEntities.size()) {
            index = 0;
            getNearbyEntities();
        }

        currentEntity = nearbyEntities.get(index);
        controlling = false;
    }

    private void drawLineToEntity(Location startLoc, Location target, Particle.DustOptions dust) {
        Location loc = startLoc.clone();
        Vector dir = target
                .toVector()
                .subtract(loc.toVector())
                .normalize()
                .multiply(.75);

        for (int i = 0; i < target.distance(startLoc); i++) {
            Util.drawDustsForNearbyPlayers(
                    loc,
                    1,
                    0,
                    0,
                    0,
                    dust
            );
            loc.add(dir);
        }
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.LEAD, "Spirit Body Threads", "100", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent e) {
        if (e.getPlayer() != p)
            return;

        ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if (item == null || !item.isSimilar(getItem()))
            return;

        getNearbyEntities();
    }

    private void getNearbyEntities() {
        nearbyEntities = p.getNearbyEntities(maxDistance, maxDistance, maxDistance)
                .stream()
                .sorted(Comparator.comparing(
                        entity -> entity.getLocation().distance(p.getEyeLocation())))
                .collect(Collectors.toList());

        if (nearbyEntities.isEmpty())
            return;

        index = 0;
        currentEntity = nearbyEntities.get(0);
    }
}
