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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Freezing extends NPCAbility {

    private final boolean npc;

    public Freezing(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        this.npc = npc;

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useNPCAbility(Location targetLoc, Entity caster, double multiplier) {
        Vector dir = caster.getLocation().add(0, 1.5, 0).getDirection().normalize();
        Location loc = caster.getLocation().add(0, 1.5, 0);
        if (loc.getWorld() == null)
            return;

        LivingEntity target = null;

        outerloop:
        for (int i = 0; i < 25; i++) {
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
        finalTarget.setFreezeTicks(200);
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 5, false, false, false));

                loc.getWorld().spawnParticle(Particle.SNOWBALL, finalTarget.getLocation().add(0, 1, 0), 10, .25, .25, .25, 0);

                counter++;
                if (counter >= 20 * 5) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        useNPCAbility(p.getLocation(), p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.SNOWBALL, "Freezing", "40", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
