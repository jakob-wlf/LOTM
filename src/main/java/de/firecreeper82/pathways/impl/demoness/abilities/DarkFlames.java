package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DarkFlames extends NPCAbility {

    private final boolean npc;

    public DarkFlames(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        this.npc = npc;
    }

    @Override
    public void useNPCAbility(Location target, Entity caster, double multiplier) {
        Vector vector;
        Location loc = caster.getLocation().add(0, 1.5, 0).clone();

        if(!npc)
            vector = caster.getLocation().getDirection().normalize();
        else
            vector = target.toVector().subtract(loc.toVector()).normalize().multiply(.25);

        if (loc.getWorld() == null)
            return;

        World world = loc.getWorld();

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;
                if (counter >= 50) {
                    cancel();
                    return;
                }

                loc.add(vector);
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 40, .25, .25, .25, 0);

                if(!npc) {
                    if (loc.getBlock().getType().isSolid()) {
                        loc.clone().subtract(vector).getBlock().setType(Material.SOUL_FIRE);
                        cancel();
                        return;
                    }
                }

                boolean cancelled = false;
                for (Entity entity : world.getNearbyEntities(loc, 1, 1, 1)) {
                    if ((!(entity instanceof Mob) && !(entity instanceof Player)) || entity == caster || entity.getType() == EntityType.ARMOR_STAND)
                        continue;

                    ((LivingEntity) entity).damage(15 * multiplier, caster);
                    entity.setFireTicks(20 * 20);
                    cancelled = true;
                }

                if (cancelled)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        useNPCAbility(p.getLocation(), p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.CHORUS_FRUIT, "Black Flames", "35", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
