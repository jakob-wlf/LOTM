package de.firecreeper82.pathways.impl.sun.abilities;

import com.google.common.util.concurrent.AtomicDouble;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HolyOath extends Ability {
    public HolyOath(int identifier, Pathway pathway, Player p) {
        super(identifier, pathway, p);
    }

    @Override
    public void useAbility() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Location loc = p.getLocation();

        //Particle effects
        double radius = 1;
        for(double y = 0; y <= 2; y+=0.05) {
            double x = radius * Math.cos(y * 20);
            double z = radius * Math.sin(y * 20);
            double x2 = radius * Math.sin(y * 20);
            double z2 = radius * Math.cos(y * 20);
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.25f);
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY() + y, loc.getZ() + z, 10, dust);
            loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x2, loc.getY() + y, loc.getZ() + z2, 2, 0, 0, 0, 0);
        }

        //Short light placement
        Material[] lightMaterial = {loc.add(0, 1, 0).getBlock().getType()};
        Block[] lightBlock = {loc.add(0, 1, 0).getBlock()};
        loc.getBlock().setType(Material.LIGHT);

        //Potion effects every second
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                    return;
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 3, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 2, false, false, false));

                pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 5);

                if(pathway.getBeyonder().getSpirituality() <= 5) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 20);

        //Particle effects while active
        AtomicDouble counter = new AtomicDouble();
        counter.set(0);
        AtomicDouble counterY = new AtomicDouble();
        counterY.set(0);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                    return;
                }

                counter.set(counter.get() + 0.25);
                counterY.set(counterY.get() + 0.25);

                double radiusActive = 0.75;
                double x = radiusActive * Math.cos(counter.get());
                double z = radiusActive * Math.sin(counter.get());

                Location pLoc = p.getLocation();

                pLoc.getWorld().spawnParticle(Particle.END_ROD, pLoc.getX() + x, pLoc.getY() + counterY.get(), pLoc.getZ() + z, 20, 0, 0, 0, 0);

                if(counterY.get() >= 2)
                    counterY.set(0);

            }
        }.runTaskTimer(Plugin.instance, 0, 1);

        //remove light
        new BukkitRunnable() {
            @Override
            public void run() {
                lightBlock[0].setType(lightMaterial[0]);
            }
        }.runTaskLater(Plugin.instance, 40);
    }
}
