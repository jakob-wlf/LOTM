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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Conceptualize extends Ability {

    public Conceptualize(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null)
            return;

        LivingEntity target = null;

        outerloop:
        for (int i = 0; i < 50; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (!(entity instanceof LivingEntity e) || entity == p)
                    continue;
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
            int counter = 0;

            @Override
            public void run() {
                if (!finalTarget.isValid() || !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                    return;
                }

                counter++;

                finalTarget.damage(8, p);

                if (counter >= 20) {
                    counter = 0;
                    if (pathway.getBeyonder().getSpirituality() <= 110) {
                        cancel();
                        return;
                    }
                    pathway.getSequence().removeSpirituality(110);
                }

                for (int i = 0; i < 3; i++) {
                    int j = i;
                    new BukkitRunnable() {
                        final double spiralRadius = 1;

                        double spiral = 0;
                        double height = j * .25;
                        double spiralX;
                        double spiralZ;

                        @Override
                        public void run() {
                            if (!finalTarget.isValid() || !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                                pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                                cancel();
                                return;
                            }

                            Location entityLoc = finalTarget.getLocation().clone();
                            entityLoc.add(0, 0.75, 0);

                            spiralX = spiralRadius * Math.cos(spiral);
                            spiralZ = spiralRadius * Math.sin(spiral);
                            spiral += 0.05;
                            height += .01;
                            if (height >= 2.5)
                                height = 0;
                            if (entityLoc.getWorld() != null)
                                entityLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spiralX + entityLoc.getX(), height + entityLoc.getY(), spiralZ + entityLoc.getZ(), 1, 0, 0, 0, 0);
                        }
                    }.runTaskTimer(Plugin.instance, j * 10, 0);
                }

            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.FIREWORK_STAR, "Conceptualize", "110/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
