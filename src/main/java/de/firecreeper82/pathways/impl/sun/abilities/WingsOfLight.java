package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class WingsOfLight extends Ability {

    public WingsOfLight(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    boolean x = true;
    boolean o = false;

    private final boolean[][] shape = {
            {o, o, o, x, o, o, o, o, o, o, o, o, x, o, o, o},
            {o, o, x, x, o, o, o, o, o, o, o, o, x, x, o, o},
            {o, o, x, x, o, o, o, o, o, o, o, o, x, x, o, o},
            {o, x, x, x, x, o, o, o, o, o, o, x, x, x, x, o},
            {o, x, x, x, x, o, o, o, o, o, o, x, x, x, x, o},
            {o, o, x, x, x, x, o, o, o, o, x, x, x, x, o, o},
            {o, o, x, x, x, x, x, o, o, x, x, x, x, x, o, o},
            {o, o, o, x, x, x, x, x, x, x, x, x, x, o, o, o},
            {o, o, o, o, o, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, o, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, x, x, x, o, o, x, x, x, o, o, o, o},
            {o, o, o, x, x, x, x, o, o, x, x, x, x, o, o, o},
            {o, o, o, x, x, x, o, o, o, o, x, x, x, o, o, o},
            {o, o, o, x, x, o, o, o, o, o, o, x, x, o, o, o},
            {o, o, o, x, x, o, o, o, o, o, o, x, x, o, o, o},
    };

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if (pathway.getSequence().getCurrentSequence() > 1) {
            p.setVelocity(new Vector(0, 1, 0));
            pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
            new BukkitRunnable() {
                int counter = 0;
                int counterVelocity = 0;

                @Override
                public void run() {
                    counter++;

                    if (counter >= 20) {
                        pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 500);
                        counter = 0;
                    }

                    if (counterVelocity < 4)
                        counterVelocity++;
                    else if (counterVelocity == 4) {
                        p.setVelocity(new Vector(0, 0, 0));
                        counterVelocity = 5;
                    }

                    Location loc = p.getLocation();
                    drawParticles(loc);
                    p.setGravity(false);


                    if (pathway.getBeyonder().getSpirituality() <= 500 || !pathway.getBeyonder().online) {
                        p.setGravity(true);
                        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                        cancel();
                    }

                    if (!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                        p.setGravity(true);
                        cancel();
                    }
                }
            }.runTaskTimer(Plugin.instance, 0, 1);
            return;
        }

        p.setFallDistance(0);
        p.setVelocity(p.getEyeLocation().getDirection().normalize().multiply(1.75));
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                if (counter > 20) {
                    cancel();
                    return;
                }

                counter++;
                drawParticles(p.getLocation());
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public void leftClick() {
        p = pathway.getBeyonder().getPlayer();
        if (pathway.getSequence().getCurrentSequence() > 1)
            return;

        p.setVelocity(new Vector(0, 0, 0));
        p.setFallDistance(0);
        p.setGravity(!p.hasGravity());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (p.hasGravity()) {
                    cancel();
                    return;
                }

                drawParticles(p.getLocation());
                p.setVelocity(new Vector(0, 0, 0));
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.FEATHER, "Wings of Light", "none", identifier, sequence, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }

    private void drawParticles(Location loc) {
        double space = 0.24;
        double defX = loc.getX() - (space * shape[0].length / 2) + space;
        double x = defX;
        double y = loc.clone().getY() + 2.8;
        double fire = -((loc.getYaw() + 180) / 60);
        fire += (loc.getYaw() < -180 ? 3.25 : 2.985);

        for (boolean[] booleans : shape) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) {

                    Location target = loc.clone();
                    target.setX(x);
                    target.setY(y);

                    Vector v = target.toVector().subtract(loc.toVector());
                    Vector v2 = VectorUtils.getBackVector(loc);
                    v = VectorUtils.rotateAroundAxisY(v, fire);
                    v2.setY(0).multiply(-0.5);

                    loc.add(v);
                    loc.add(v2);
                    for (int k = 0; k < 3; k++)
                        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.ELECTRIC_SPARK, loc, 1, 0.02, 0.02, 0.02, 0);
                    loc.subtract(v2);
                    loc.subtract(v);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }
}
