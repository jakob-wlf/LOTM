package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;


public class HolyOath extends Ability {
    public HolyOath(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
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
            Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY() + y, loc.getZ() + z, 10, dust);
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
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 2, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 2, false, false, false));

                if(pathway.getBeyonder().getSpirituality() <= 45 || !pathway.getBeyonder().online) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }

                pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 45);

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 20);

        //Particle effects while active
        new BukkitRunnable() {
            double counter = 0;
            double counterY = 0;
            @Override
            public void run() {

                counter = counter + 0.25;
                counterY = counterY + 0.25;

                double radiusActive = 0.75;
                double x = radiusActive * Math.cos(counter);
                double z = radiusActive * Math.sin(counter);

                Location pLoc = p.getLocation();

                Objects.requireNonNull(pLoc.getWorld()).spawnParticle(Particle.END_ROD, pLoc.getX() + x, pLoc.getY() + counterY, pLoc.getZ() + z, 20, 0, 0, 0, 0);

                if(counterY >= 2)
                    counterY = 0;

                if(pathway.getBeyonder().getSpirituality() <= 5 || !pathway.getBeyonder().online) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                }

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

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.PAPER, "Holy Oath", "45/s", identifier, 7, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
