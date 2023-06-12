package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ColdWind extends NPCAbility {

    private final boolean npc;

    public ColdWind(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        this.npc = npc;
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        Vector dir = caster.getLocation().getDirection().normalize().multiply(1.25);

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 0;

            int npcTimer = 20 * 5;

            @Override
            public void run() {
                npcTimer--;

                for (Entity entity : caster.getNearbyEntities(9, 9, 9)) {
                    entity.setVelocity(dir);
                    entity.setFreezeTicks(20 * 8);
                }

                for (int i = 0; i < 30; i++) {
                    Location tempLoc = caster.getLocation().add(0, 1, 0).add(random.nextInt(16) - 8, random.nextInt(10) - 5, random.nextInt(16) - 8);
                    caster.getWorld().spawnParticle(Particle.SNOWFLAKE, tempLoc, 0, dir.getX(), dir.getY(), dir.getZ(), .4);
                }

                if(!npc) {
                    if (!pathway.getSequence().getUsesAbilities()[identifier - 1] || pathway.getBeyonder().getSpirituality() <= 10) {
                        cancel();
                        return;
                    }

                    counter++;

                    if (counter >= 20) {
                        counter = 0;
                        pathway.getSequence().removeSpirituality(10);
                    }
                }
                else if(npcTimer <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        useNPCAbility(p.getLocation(), p, 1);
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.FEATHER, "Cold Wind", "10/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
