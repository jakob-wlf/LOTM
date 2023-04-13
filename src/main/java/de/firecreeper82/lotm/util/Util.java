package de.firecreeper82.lotm.util;

import jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.Random;


public class Util {

    @SuppressWarnings("all")
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException exception) {
            throw new NumberFormatException();
        }
    }

    public static String capitalize(String s) {
        return (s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
    }

    public static int biasedRandomNumber(double[] probabilityDistribution, int min) {
        Random random = new Random();

        double r = random.nextDouble();
        double sum = 0.0;
        int i = 0;
        while (i < probabilityDistribution.length - 1 && r > sum + probabilityDistribution[i]) {
            sum += probabilityDistribution[i];
            i++;
        }
        return i + min;
    }

    public static void drawCircle(Location loc, int sphereRadius, int detail, Particle.DustOptions dust, @Nullable Material material) {
        //Spawn particles
        for (double i = 0; i <= Math.PI; i += Math.PI / detail) {
            double radius = Math.sin(i) * sphereRadius;
            double y = Math.cos(i) * sphereRadius;
            for (double a = 0; a < Math.PI * 2; a += Math.PI / detail) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                loc.add(x, y, z);
                if(loc.getWorld() == null)
                    return;
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, .2, .2, .2, 0, dust);
                if(material != null && (loc.getBlock().getType().getHardness() >= 0 || loc.getBlock().getType() == Material.BARRIER) && (!loc.getBlock().getType().isSolid() || loc.getBlock().getType() == Material.BARRIER)) {
                    loc.getBlock().setType(material);
                }
                loc.subtract(x, y, z);
            }
        }
    }
}
