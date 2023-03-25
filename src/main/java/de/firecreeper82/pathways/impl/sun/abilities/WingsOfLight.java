package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;

public class WingsOfLight extends Ability {

    private final HashMap<Integer, Double> velocityMultiplier;

    public WingsOfLight(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        velocityMultiplier = new HashMap<>();
        velocityMultiplier.put(3, .5);
        velocityMultiplier.put(2, 1d);
        velocityMultiplier.put(1, 2d);
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
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;

                if(counter >= 20) {
                    pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 500);
                    counter = 0;
                }

                Location loc = p.getLocation();
                drawParticles(loc);
                    p.setGliding(true);
                    p.setVelocity(p.getLocation().getDirection().normalize().multiply(velocityMultiplier.get(pathway.getSequence().getCurrentSequence())));

                if(pathway.getBeyonder().getSpirituality() <= 500 || !pathway.getBeyonder().online || p.getLocation().clone().subtract(0, 1, 0).getBlock().getType().isSolid()) {
                    p.setGliding(false);
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    p.setGliding(false);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.FEATHER, "Wings of Light", "500/s", identifier, 3, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
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
                    Vector v2 = getBackVector(loc);
                    v = rotateAroundAxisY(v, fire);
                    v2.setY(0).multiply(-0.5);

                    loc.add(v);
                    loc.add(v2);
                    for (int k = 0; k < 3; k++)
                        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.ELECTRIC_SPARK, loc, 5, 0.02, 0.02, 0.02, 0);
                    loc.subtract(v2);
                    loc.subtract(v);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }

    public static Vector rotateAroundAxisY(Vector v, double fire) {
        double x, z, cos, sin;
        cos = Math.cos(fire);
        sin = Math.sin(fire);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static Vector getBackVector(Location loc) {
        final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 90))));
        final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 90))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }
}
