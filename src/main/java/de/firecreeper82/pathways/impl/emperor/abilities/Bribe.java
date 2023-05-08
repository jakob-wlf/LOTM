package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
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
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Bribe extends Recordable {
    private final HashMap<Integer, String> bribeMode = new HashMap<>();

    private int bribeselected = 0;

    public Bribe(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
        bribeMode.put(0, "Weaken");
        bribeMode.put(1, "Charm");
        bribeMode.put(2, "Arrogance");
        bribeMode.put(3, "Connect");
    }

    @Override
    public void leftClick() {
        p = pathway.getBeyonder().getPlayer();
        if (!(p.isSneaking())) {
            if (bribeselected != 3) {
                bribeselected++;
            } else {
                bribeselected = 0;
            }
        } else {
            if (bribeselected != 0) {
                bribeselected--;
            } else {
                bribeselected = 3;
            }
        }

        p.sendMessage("§fSet bribe mode to:" + (bribeMode.get(bribeselected)));
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        if (!recorded) pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        destroy(beyonder, recorded);
        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null) return;

        LivingEntity target = null;

        outerloop:
        for (int i = 0; i < 10; i++) {
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

        if (!finalTarget.isValid() || !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
            pathway.getSequence().getUsesAbilities()[identifier - 1] = false;

            return;
        }
        Location entityLoc = finalTarget.getLocation().clone();
        entityLoc.add(0, 0.75, 0);

        if (entityLoc.getWorld() != null) {
            entityLoc.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, entityLoc.getX(), entityLoc.getY(), entityLoc.getZ(), 1, 0, 0, 0, 0);

            switch (bribeselected) {
                case 0 -> {
                    //bribe weaken
                    finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 2, false, false, false));
                    entityLoc.getWorld().spawnParticle(Particle.CRIT, entityLoc.getX(), entityLoc.getY(), entityLoc.getZ(), 1, 0, 0, 0, 0);
                }
                case 1 -> {
                    //bribe charm
                    finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1, false, false, false));
                    entityLoc.getWorld().spawnParticle(Particle.HEART, entityLoc.getX(), entityLoc.getY(), entityLoc.getZ(), 1, 0, 0, 0, 0);
                }
                case 2 -> {
                    //bribe arrogance
                    finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1, false, false, false));
                    entityLoc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, entityLoc.getX(), entityLoc.getY(), entityLoc.getZ(), 1, 0, 0, 0, 0);
                }
                case 3 -> {
                    //bribe Connect
                    finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2000, 1, false, false, false));
                    entityLoc.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, entityLoc.getX(), entityLoc.getY(), entityLoc.getZ(), 1, 0, 0, 0, 0);
                }
                default -> {
                    //in case bribeSelected is null;
                    bribeselected = 0;
                    useAbility();
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.EMERALD, "Bribe", "120", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
