package de.firecreeper82.pathways.impl.fool;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class FoolSequence extends Sequence implements Listener {

    public FoolSequence(Pathway pathway, int optionalSequence) {
        super(pathway, optionalSequence);
        init();
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    @Override
    public List<Integer> getIds() {
        Integer[] ids = {4, 14};
        return Arrays.asList(ids);
    }

    public void init() {
        usesAbilities = new boolean[20];
        Arrays.fill(usesAbilities, false);

        abilities = new ArrayList<>();
        recordables = new ArrayList<>();

        sequenceEffects = new HashMap<>();
        sequenceResistances = new HashMap<>();

        initEffects();

        sequenceMultiplier = new HashMap<>();
        sequenceMultiplier.put(5, 1.5);
        sequenceMultiplier.put(4, 2.0);
        sequenceMultiplier.put(3, 2.25);
        sequenceMultiplier.put(2, 3.5);
        sequenceMultiplier.put(1, 5.0);
    }

    //Paper throw ability
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getPlayer() != getPathway().getBeyonder().getPlayer() || e.getItem() == null || currentSequence > 8 || pathway.getBeyonder().getSpirituality() < 6 || !pathway.getBeyonder().isBeyonder())
            return;
        if(e.getItem().getType() == Material.PAPER) {
            Player p = e.getPlayer();
            ItemStack removeItem = e.getItem();

            p.getInventory().remove(removeItem);
            removeItem.setAmount(removeItem.getAmount() - 1);
            p.getInventory().setItem(p.getInventory().getHeldItemSlot(), removeItem);

            Vector v = p.getEyeLocation().getDirection().normalize();
            Location startLoc = p.getEyeLocation().clone();

            removeSpirituality(5);
            new BukkitRunnable() {
                int counter = 0;
                @Override
                public void run() {
                    Objects.requireNonNull(startLoc.getWorld()).spawnParticle(Particle.CLOUD, startLoc, 1, 0, 0, 0, 0);
                    startLoc.add(v);
                    counter++;

                    if(!startLoc.getWorld().getNearbyEntities(startLoc, 5, 5,5).isEmpty()) {
                        for(Entity entity : startLoc.getWorld().getNearbyEntities(startLoc, 5, 5, 5)) {
                            Vector v1 = new Vector(
                                    startLoc.getX() + 0.25,
                                    startLoc.getY() + 0.25,
                                    startLoc.getZ() + 0.25
                            );
                            Vector v2 = new Vector(
                                    startLoc.getX() - 0.25,
                                    startLoc.getY() - 0.25,
                                    startLoc.getZ() - 0.25
                            );
                            if(entity.getBoundingBox().overlaps(v1, v2)) {
                                if(!(entity instanceof Damageable) || entity == p)
                                    continue;
                                ((Damageable) entity).damage(5 * Ability.getMultiplier(getPathway()), p);
                                cancel();
                                return;
                            }
                        }
                    }

                    if(counter >= 40 || startLoc.getBlock().getType().isSolid()) {
                        removeItem.setAmount(1);
                        if(removeItem.getType() == Material.PAPER)
                            startLoc.getWorld().dropItem(startLoc, removeItem);
                        cancel();
                    }
                }
            }.runTaskTimer(Plugin.instance, 0, 1);
        }
    }

    //Remove fall damage
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntity() != getPathway().getBeyonder().getPlayer() || e.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;

        e.setCancelled(true);
    }

    //Passive effects
    public void initEffects() {
        PotionEffect[] effects4 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 1, false, false, true),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false)
        };
        sequenceEffects.put(4, effects4);

        PotionEffect[] effects2 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.SATURATION, 60, 10, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 2, false, false, true),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 2, false, false, false)
        };
        sequenceEffects.put(2, effects2);
    }
}
