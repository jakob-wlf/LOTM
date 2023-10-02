package de.firecreeper82.pathways.impl.fool.abilities.marionetteAbilities;

import de.firecreeper82.handlers.mobs.beyonders.RogueBeyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SpiritBodyThreads extends NPCAbility implements Listener {


    private boolean controlling;
    private Entity currentEntity;
    private int index;
    private final int[] maxDistance;

    private final int[] convertTimePerLevel;

    List<Entity> nearbyEntities;
    private final List<Marionette> marionettes;

    private final Particle.DustOptions dustGray, dustWhite, dustPurple, dustBlue;

    public SpiritBodyThreads(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        if (!npc) {
            p = pathway.getBeyonder().getPlayer();
            items.addToSequenceItems(identifier - 1, sequence);
        }

        controlling = false;
        currentEntity = null;
        index = 0;

        marionettes = new ArrayList<>();

        dustGray = new Particle.DustOptions(Color.fromRGB(80, 80, 80), .75f);
        dustWhite = new Particle.DustOptions(Color.fromRGB(255, 255, 255), .75f);
        dustPurple = new Particle.DustOptions(Color.fromRGB(221, 0, 255), .75f);
        dustBlue = new Particle.DustOptions(Color.fromRGB(0, 128, 255), .75f);

        convertTimePerLevel = new int[] {
                0,
                3,
                5,
                7,
                8,
                12
        };

        maxDistance = new int[] {
                100000000,
                750,
                500,
                125,
                75,
                12
        };

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

        if(marionettes.stream().anyMatch(marionette -> marionette.getEntity() == currentEntity))
            return;

        ((LivingEntity) currentEntity).damage(0, p);

        controlling = true;
        int convertTimeSeconds = convertTimePerLevel[pathway.getSequence().getCurrentSequence()];

        startControlling(convertTimeSeconds);
        drawSpiralAroundTarget(convertTimeSeconds);
    }

    private void startControlling(int convertTimeSeconds) {
        new BukkitRunnable() {
            int counter = convertTimeSeconds * 20;
            @Override
            public void run() {
                if(currentEntity == null || !currentEntity.isValid() || !controlling || p == null || !p.isValid()) {
                    controlling = false;
                    cancel();
                    return;
                }

                drawLineToEntity(p.getEyeLocation(), currentEntity.getLocation().add(0, .5, 0), dustPurple);
                giveEffectsToTarget(counter);

                counter--;

                if(counter <= 0) {
                    controlling = false;
                    turnIntoMarionette();
                    cancel();
                }
            }

        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void turnIntoMarionette() {
        if(currentEntity == null || !currentEntity.isValid())
            return;

        boolean isBeyonder = (
                Plugin.beyonders.containsKey(currentEntity.getUniqueId()) ||
                Plugin.currentRogueBeyonders
                        .stream()
                        .anyMatch(rogueBeyonder -> rogueBeyonder.getEntity() == currentEntity)
        );

        int pathway = -1;
        int sequence = -1;
        String name = currentEntity.getName();

        if(isBeyonder) {
            if(Plugin.beyonders.containsKey(currentEntity.getUniqueId())) {
                pathway = Plugin.beyonders.get(currentEntity.getUniqueId()).getPathway().getPathwayInt();
                sequence = Plugin.beyonders.get(currentEntity.getUniqueId()).getPathway().getSequence().getCurrentSequence();
            }
            else {
                RogueBeyonder rogueBeyonder = Plugin.currentRogueBeyonders
                        .stream()
                        .filter(rb -> rb.getEntity() == currentEntity)
                        .findFirst()
                        .orElseThrow(() ->
                                new RuntimeException("Rogue Beyonder not found"));

                pathway = rogueBeyonder.getPathway();
                sequence = rogueBeyonder.getSequence();
                name = rogueBeyonder.getName();
            }
        }

        Marionette marionette = new Marionette(
                isBeyonder,
                sequence,
                pathway,
                currentEntity.getType(),
                p.getUniqueId(),
                currentEntity.getLocation(),
                name,
                this,
                ((LivingEntity) currentEntity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()
        );

        marionettes.add(marionette);

        if(CitizensAPI.getNPCRegistry().isNPC(currentEntity))
            CitizensAPI.getNPCRegistry().getNPC(currentEntity).destroy();
        else if(!(currentEntity instanceof Player playerTarget))
            currentEntity.remove();
        else
            playerTarget.setHealth(0);
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
        int multiplier = (int) (Math.round(8d / progress) * 1.5);
        ((LivingEntity) currentEntity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, multiplier));
    }

    @Override
    public void onHold() {
        int currentSequence = pathway.getSequence().getCurrentSequence();

        if (p == null || nearbyEntities == null || currentEntity == null || controlling)
            return;

        Location startLoc = p.getEyeLocation();

        if(!currentEntity.isValid()) {
            index = 0;
            getNearbyEntities();
        }

        if (currentEntity.getLocation().distance(startLoc) > maxDistance[currentSequence]) {
            nearbyEntities.remove(currentEntity);
            if (nearbyEntities.isEmpty())
                currentEntity = null;
            else {
                index = 0;
                currentEntity = nearbyEntities.get(0);
            }
        }

        nearbyEntities.removeIf(entity -> entity.getLocation().distance(startLoc) > maxDistance[currentSequence]);

        if (nearbyEntities.isEmpty())
            return;

        for (Entity entity : nearbyEntities) {
            if (entity == p)
                continue;

            if(marionettes.stream().anyMatch(marionette -> marionette.getEntity() == entity))
                drawLineToEntity(startLoc, entity.getLocation().add(0, .5, 0), dustBlue);
            else if (entity == currentEntity)
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

        while (marionettes.stream().anyMatch(marionette -> marionette.getEntity() == nearbyEntities.get(index))) {
            index++;
            if (index >= nearbyEntities.size()) {
                index = 0;
                getNearbyEntities();
            }
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
        int currentSequence = pathway.getSequence().getCurrentSequence();
        nearbyEntities = p.getNearbyEntities(maxDistance[currentSequence], maxDistance[currentSequence], maxDistance[currentSequence])
                .stream()
                .filter(entity -> entity instanceof LivingEntity && !(entity instanceof ArmorStand))
                .sorted(Comparator.comparing(
                        entity -> entity.getLocation().distance(p.getEyeLocation())))
                .collect(Collectors.toList());

        if (nearbyEntities.isEmpty())
            return;

        index = 0;
        currentEntity = nearbyEntities.get(0);
    }

    public void removeMarionette(Marionette marionette) {
        marionettes.remove(marionette);
    }
}
