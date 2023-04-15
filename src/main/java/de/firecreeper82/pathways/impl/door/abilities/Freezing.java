package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Freezing extends Ability {
    public Freezing(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if(loc.getWorld() == null)
            return;

        LivingEntity target = null;

        outerloop: for(int i = 0; i < 25; i++) {
            for(Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if(!(entity instanceof LivingEntity e) || entity == p)
                    continue;
                target = e;
                break outerloop;
            }

            loc.add(dir);
        }

        if(target == null) {
            p.sendMessage("§cCouldn't find the target!");
            return;
        }

        LivingEntity finalTarget = target;
        finalTarget.setFreezeTicks(200);
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 5, false, false, false));

                loc.getWorld().spawnParticle(Particle.SNOWBALL, finalTarget.getLocation().add(0, 1, 0), 10, .25, .25, .25, 0);

                counter++;
                if(counter >= 20 * 5) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);

    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.SNOWBALL, "Freezing", "40", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
