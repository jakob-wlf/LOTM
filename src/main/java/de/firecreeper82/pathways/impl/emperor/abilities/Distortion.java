package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Distortion extends Recordable {
    private Boolean distortEnv = false;

    public Distortion(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void leftClick() {
        p = pathway.getBeyonder().getPlayer();
        if (distortEnv) {
            distortEnv = false;
            String distortMode = "living entities !";
            p.sendMessage("You are now distorting " + distortMode);
        } else {
            distortEnv = true;
            String distortMode = "the surrounding environment !";
            p.sendMessage("You are now distorting " + distortMode);
        }
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        if (!recorded) pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
        destroy(beyonder, recorded);

            /*List<Entity> near = p.getNearbyEntities(50.0D, 50.0D, 50.0D);
            for(Entity entity : near) {
                if(entity instanceof Player) {
                    Player nearPlayer = (Player) entity;
                    //do stuff here
                }
            }*/

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null) return;

        LivingEntity target = null;

        outerloop:
        for (int i = 0; i < 15; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (!(entity instanceof LivingEntity e) || entity == p) continue;
                target = e;
                break outerloop;
            }

            loc.add(dir);
        }

        if (target == null) {
            p.sendMessage("§cCouldn't find the target!");
            return;
        }

        LivingEntity finalTarget = target;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!distortEnv) {
                    if (!finalTarget.isValid() || !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                        cancel();
                        return;
                    }


                    if (!finalTarget.isValid() || !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                        cancel();
                        return;
                    }

                    Location entityLoc = finalTarget.getLocation().clone();
                    entityLoc.add(0, 0.75, 0);

                    if (entityLoc.getWorld() != null) {
                        entityLoc.getWorld().spawnParticle(Particle.CRIT_MAGIC, entityLoc.getX(), entityLoc.getY(), entityLoc.getZ(), 1, 0, 0, 0, 0);
                        PotionEffect weakness = finalTarget.getPotionEffect(PotionEffectType.WEAKNESS);
                        PotionEffect slowness = finalTarget.getPotionEffect(PotionEffectType.SLOW);
                        PotionEffect resistance = p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                        //adds weakness to the target

                        if (weakness != null) {
                            finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, weakness.getDuration() + 80, weakness.getAmplifier() + 1, false, false, false));
                        } else {
                            finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, 1, false, false, false));
                        }
                        //adds slowness to the target
                        if (slowness != null) {
                            finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slowness.getDuration() + 80, slowness.getAmplifier() + 1, false, false, false));
                        } else {
                            finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 1, false, false, false));
                        }
                        //adds res to the lawyer beyonder
                        if (resistance != null) {
                            if (resistance.getAmplifier() > 0 && resistance.getDuration() > 0) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, resistance.getDuration() + 80, resistance.getAmplifier() + 1, false, false, false));
                            } else {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 1, false, false, false));


                            }
                        }
                    } else {
                        List<Entity> near = p.getNearbyEntities(25, 25, 25);
                        List<Player> nearbyPlayer = new ArrayList<>() {
                        };
                        for (Entity entity : near) {
                            if (entity instanceof Player) {
                                nearbyPlayer.add((Player) entity);
                                //do stuff here
                            }
                        }
                        for (Player player: nearbyPlayer){
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 800, 1));
                        }

                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.LANTERN, "Distortion", "Varying", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}