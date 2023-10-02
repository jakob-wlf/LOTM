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
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

    private final Particle.DustOptions dustGray, dustWhite, dustPurple;

    public SpiritBodyThreads(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        if (!npc) {
            p = pathway.getBeyonder().getPlayer();
            items.addToSequenceItems(identifier - 1, sequence);
        }

        controlling = false;
        currentEntity = null;
        index = 0;

        dustGray = new Particle.DustOptions(Color.fromRGB(80, 80, 80), .75f);
        dustWhite = new Particle.DustOptions(Color.fromRGB(255, 255, 255), .75f);
        dustPurple = new Particle.DustOptions(Color.fromRGB(221, 0, 255), 1f);

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

        ((LivingEntity) currentEntity).damage(0, p);

        controlling = true;
        int convertTimeSeconds = 10;

        startControlling(convertTimeSeconds);
        drawSpiralAroundTarget(convertTimeSeconds);
    }

    private void startControlling(int convertTimeSeconds) {
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if(currentEntity == null || !currentEntity.isValid() || !controlling || p == null || !p.isValid()) {
                    controlling = false;
                    cancel();
                    return;
                }

                drawLineToEntity(p.getEyeLocation(), currentEntity.getLocation().add(0, .5, 0), dustPurple);
                giveEffectsToTarget(counter);

                counter++;

                if(counter >= (convertTimeSeconds * 20)) {
                    controlling = false;
                    cancel();
                }
            }

        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void drawSpiralAroundTarget(int convertTimeSeconds) {
        new BukkitRunnable() {
            long counter = 10L * convertTimeSeconds;
            double spiralRadius = 2;

            double spiral = 0;
            double height = 0;
            double spiralX;
            double spiralZ;

            @Override
            public void run() {
                Location entityLoc = currentEntity.getLocation().clone();
                entityLoc.add(0, 0.75, 0);

                spiralX = spiralRadius * Math.cos(spiral);
                spiralZ = spiralRadius * Math.sin(spiral);
                spiral += 0.25;
                height += .05;
                if (height >= 2.5)
                    height = 0;
                if (entityLoc.getWorld() != null)
                    entityLoc.getWorld().spawnParticle(Particle.REDSTONE, spiralX + entityLoc.getX(), height + entityLoc.getY(), spiralZ + entityLoc.getZ(), 5, dustPurple);

                counter--;
                spiralRadius -= (1.5 / (10L * convertTimeSeconds));

                if (!controlling)
                    cancel();
                if (counter <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }

    private void giveEffectsToTarget(int progress) {
        ((LivingEntity) currentEntity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2));
    }

    @Override
    public void onHold() {
        if (p == null || nearbyEntities == null || currentEntity == null || controlling)
            return;

        Location startLoc = p.getEyeLocation();

        if(!currentEntity.isValid()) {
            index = 0;
            getNearbyEntities();
        }

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
                drawLineToEntity(startLoc, entity.getLocation().add(0, .5, 0), dustWhite);
            else
                drawLineToEntity(startLoc, entity.getLocation().add(0, .5, 0), dustGray);
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

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!controlling || e.getEntity() != currentEntity)
            return;
        controlling = false;
    }

    private void getNearbyEntities() {
        nearbyEntities = p.getNearbyEntities(maxDistance, maxDistance, maxDistance)
                .stream()
                .filter(entity -> entity instanceof LivingEntity)
                .sorted(Comparator.comparing(
                        entity -> entity.getLocation().distance(p.getEyeLocation())))
                .collect(Collectors.toList());

        if (nearbyEntities.isEmpty())
            return;

        index = 0;
        currentEntity = nearbyEntities.get(0);
    }
}
