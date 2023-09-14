package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import org.bukkit.Color;
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

import java.util.HashMap;

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

        Particle.DustOptions dustBlue = new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.5f);
        Particle.DustOptions dustPurple = new Particle.DustOptions(Color.fromRGB(87, 20, 204), 1.5f);

        new BukkitRunnable(){
            int counter = 200;
            @Override
            public void run() {
                Util.drawParticleSphere(startLoc, 2, 10, dustBlue, null, .05, Particle.REDSTONE);
                Util.drawParticleSphere(startLoc, 2, 10, dustPurple, null, .05, Particle.REDSTONE);

                startLoc.add(dir);

                counter--;
                if(counter <= 0)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }
}
