package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Conceptualize extends NPCAbility {

    private final boolean npc;

    public Conceptualize(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        this.npc = npc;
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useNPCAbility(Location targetLoc, Entity caster, double multiplier) {
        Vector dir = caster.getLocation().getDirection().normalize();
        Location loc = caster.getLocation().add(0, 1.5, 0);
        if (loc.getWorld() == null)
            return;

        LivingEntity target = null;

        outerloop:
        for (int i = 0; i < 50; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if ((!(entity instanceof Mob) && !(entity instanceof Player)) || entity == caster)
                    continue;
                target = (LivingEntity) entity;
                break outerloop;
            }

            loc.add(dir);
        }

        if (target == null) {
            if(!npc)
                p.sendMessage("§cCouldn't find the target!");
            return;
        }

        LivingEntity finalTarget = target;
        new BukkitRunnable() {
            int counter = 0;
            double timer = 1.0;

            double npcTimer = 20 * 4;

            @Override
            public void run() {
                if (!finalTarget.isValid() || (!npc && !pathway.getSequence().getUsesAbilities()[identifier - 1])) {
                    if(!npc)
                        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                    return;
                }

                if(!npc)
                    counter++;

                npcTimer--;

                if(npc && npcTimer <= 0) {
                    cancel();
                }

                finalTarget.damage(8, caster);

                if (!npc && counter >= 20) {
                    counter = 0;
                    if (pathway.getBeyonder().getSpirituality() <= Math.pow(110, timer)) {
                        cancel();
                        return;
                    }
                    pathway.getSequence().removeSpirituality(Math.pow(110, timer));
                    timer += .08;
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
                            if (!finalTarget.isValid() || (!npc && !pathway.getSequence().getUsesAbilities()[identifier - 1])) {
                                if(!npc)
                                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                                cancel();
                                return;
                            }

                            if(npc && npcTimer <= 0) {
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
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        useNPCAbility(p.getEyeLocation(), p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.FIREWORK_STAR, "Conceptualize", "increasing", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
