package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import de.firecreeper82.pathways.impl.tyrant.TyrantSequence;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class LightningBall extends NPCAbility {

    public LightningBall(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if (!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        if (!npc)
            p = pathway.getBeyonder().getPlayer();

    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        useNPCAbility(Util.getTargetLoc(200, p), p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.PURPLE_DYE, "Lightning Ball", "5000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        Location startLoc = caster.getLocation().add(0, .75, 0);
        Vector dir = loc.toVector().subtract(caster.getLocation().add(0, .75, 0).toVector()).normalize().multiply(2);

        Random random = new Random();

        Particle.DustOptions dustBlue = new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.5f);
        Particle.DustOptions dustPurple = new Particle.DustOptions(Color.fromRGB(87, 20, 204), 1.5f);

        if (startLoc.getWorld() == null)
            return;

        new BukkitRunnable() {
            int counter = 200;

            @Override
            public void run() {
                Util.drawParticleSphere(startLoc, 2, 10, dustBlue, null, .05, Particle.REDSTONE);
                Util.drawParticleSphere(startLoc, 2, 10, dustPurple, null, .05, Particle.REDSTONE);

                if (startLoc.getBlock().getType().isSolid() || (!startLoc.getWorld().getNearbyEntities(startLoc, 1, 1, 1).isEmpty() && !startLoc.getWorld().getNearbyEntities(startLoc, 1, 1, 1).contains(caster))) {
                    new BukkitRunnable() {
                        int counter = 16;

                        @Override
                        public void run() {
                            TyrantSequence.spawnLighting(startLoc.clone().add(random.nextInt(-1, 1), 0, random.nextInt(-1, 1)), caster, getMultiplier(), false, false, 1);

                            counter--;
                            if (counter <= 0)
                                cancel();
                        }
                    }.runTaskTimer(Plugin.instance, 0, 4);
                    counter = 0;
                }

                startLoc.add(dir);

                counter--;
                if (counter <= 0)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }
}
