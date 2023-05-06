package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BrainWash extends Recordable {
    public BrainWash(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
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
        for (int i = 0; i < 5; i++) {
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
                    entityLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, entityLoc.getX(), entityLoc.getY(), entityLoc.getZ(), 1, 0, 0, 0, 0);
                    finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 140, 1, false, false, false));

                }
            }

        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.LANTERN, "BrainWash", "40", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}

