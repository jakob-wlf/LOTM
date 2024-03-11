package dev.ua.ikeepcalm.mystical.pathways.tyrant.abilities;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.utils.MathVectorUtils;
import dev.ua.ikeepcalm.mystical.Items;
import dev.ua.ikeepcalm.mystical.NpcAbility;
import dev.ua.ikeepcalm.mystical.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.tyrant.TyrantItems;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RagingBlows extends NpcAbility {

    public RagingBlows(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if (!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        if (!npc)
            p = pathway.getBeyonder().getPlayer();
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        useNPCAbility(p.getLocation(), p, getMultiplier());
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        World world = caster.getWorld();

        new BukkitRunnable() {
            int counter = 8;

            @Override
            public void run() {
                Random random = new Random();
                Location startLoc = MathVectorUtils.getRelativeLocation(caster, random.nextDouble(1, 2), random.nextDouble(-1.5, 1.5), random.nextDouble(-.5, .5));
                for (Entity entity : world.getNearbyEntities(startLoc, 50, 50, 50)) {
                    if (!(entity instanceof Player p))
                        continue;

                    p.spawnParticle(Particle.EXPLOSION_NORMAL, startLoc, 10, 0, 0, 0, .25);
                    p.spawnParticle(Particle.CRIT, startLoc, 10, 0, 0, 0, .25);
                    p.playSound(startLoc, Sound.ENTITY_GENERIC_EXPLODE, .25f, 1f);

                    for (Entity hit : world.getNearbyEntities(startLoc, 1.2, 1.2, 1.2)) {
                        if (hit instanceof LivingEntity livingEntity && hit.getType() != EntityType.ARMOR_STAND && hit != caster)
                            livingEntity.damage(6.5 * multiplier, caster);
                    }
                }

                counter--;

                if (counter <= 0)
                    cancel();
            }
        }.runTaskTimer(LordOfTheMinecraft.instance, 0, 6);
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.BONE_MEAL, "Raging Blows", "20", identifier, sequence, p.getName());
    }
}
