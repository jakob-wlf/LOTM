package dev.ua.ikeepcalm.mystical.pathways.door.abilities;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.mystical.parents.Items;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.NpcAbility;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.door.DoorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class Wind extends NpcAbility {

    private final boolean npc;

    public Wind(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        this.npc = npc;

        if (!npc)
            items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        Vector dir = caster.getLocation().getDirection().normalize().multiply(.5);

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 0;

            int npcCounter = 20 * 5;

            @Override
            public void run() {
                for (Entity entity : caster.getNearbyEntities(7, 7, 7)) {
                    entity.setVelocity(dir);
                }

                for (int i = 0; i < 8; i++) {
                    Location tempLoc = caster.getLocation().add(0, 1.5, 0).add(random.nextInt(10) - 5, random.nextInt(6) - 3, random.nextInt(10) - 5);
                    caster.getWorld().spawnParticle(Particle.CLOUD, tempLoc, 0, dir.getX(), dir.getY(), dir.getZ(), .4);
                }

                if (!npc && (!pathway.getSequence().getUsesAbilities()[identifier - 1] || pathway.getBeyonder().getSpirituality() <= 8)) {
                    cancel();
                    return;
                }

                counter++;

                if (npc) {
                    npcCounter--;
                    if (npcCounter <= 0)
                        cancel();
                }

                if (!npc && counter >= 20) {
                    counter = 0;
                    pathway.getSequence().removeSpirituality(8);
                }
            }
        }.runTaskTimer(LordOfTheMinecraft.instance, 0, 0);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        useNPCAbility(p.getLocation(), p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.FEATHER, "Wind", "8/s", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
