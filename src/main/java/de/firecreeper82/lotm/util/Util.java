package de.firecreeper82.lotm.util;

import jline.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;


public class Util {

    @SuppressWarnings("all")
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException exception) {
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

    public static void drawSphere(Location loc, int sphereRadius, int detail, Particle.DustOptions dust, @Nullable Material material, double offset) {
        //Spawn particles
        for (double i = 0; i <= Math.PI; i += Math.PI / detail) {
            double radius = Math.sin(i) * sphereRadius;
            double y = Math.cos(i) * sphereRadius;
            for (double a = 0; a < Math.PI * 2; a += Math.PI / detail) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                loc.add(x, y, z);
                if (loc.getWorld() == null)
                    return;
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, offset, offset, offset, 0, dust);
                if (material != null && (loc.getBlock().getType().getHardness() >= 0 || loc.getBlock().getType() == Material.BARRIER) && (!loc.getBlock().getType().isSolid() || loc.getBlock().getType() == Material.BARRIER)) {
                    loc.getBlock().setType(material);
                }
                loc.subtract(x, y, z);
            }
        }
    }
    public static void drawParticleSphere(Location loc, double sphereRadius, int detail, @Nullable Particle.DustOptions dust, @Nullable Material material, double offset, Particle particle) {
        //Spawn particles
        for (double i = 0; i <= Math.PI; i += Math.PI / detail) {
            double radius = Math.sin(i) * sphereRadius;
            double y = Math.cos(i) * sphereRadius;
            for (double a = 0; a < Math.PI * 2; a += Math.PI / detail) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                loc.add(x, y, z);
                if (loc.getWorld() == null)
                    return;

                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(player.getWorld() != loc.getWorld() || player.getLocation().distance(loc) > 80)
                        continue;
                    if(dust != null)
                        player.spawnParticle(Particle.REDSTONE, loc, 1, offset, offset, offset, 0, dust);
                    else
                        player.spawnParticle(particle, loc, 1, offset, offset, offset, 0);
                }
                if (material != null && (loc.getBlock().getType().getHardness() >= 0 || loc.getBlock().getType() == Material.BARRIER) && (!loc.getBlock().getType().isSolid() || loc.getBlock().getType() == Material.BARRIER)) {
                    loc.getBlock().setType(material);
                }
                loc.subtract(x, y, z);
            }
        }
    }


    @SuppressWarnings("unused")
    public static ArrayList<Block> getBlocksInCircleRadius(Block start, int radius, boolean ignoreAir) {

        Location loc = start.getLocation();

        ArrayList<Block> blocks = new ArrayList<>();

        for (int i = radius; i > -radius; i--) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if ((x * x) + (z * z) <= Math.pow(radius, 2)) {
                        Block block = start.getWorld().getBlockAt((int) loc.getX() + x, (int) loc.getY() + i, (int) loc.getZ() + z);
                        if (block.getType() != Material.AIR && block.getType() != Material.CAVE_AIR || !ignoreAir)
                            blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static boolean testForValidEntity(Entity entity, Entity caster, boolean noArmorStand, boolean noCaster) {
        if(!(entity instanceof LivingEntity))
            return false;

        if(noArmorStand && entity.getType() == EntityType.ARMOR_STAND)
            return false;

        return !noCaster || entity != caster;
    }

    public static ArrayList<Block> getNearbyBlocksInSphere(Location location, int radius, boolean empty, boolean ignoreAir, boolean smoothEdges) {
        ArrayList<Block> blocks = new ArrayList<>();

        int bx = location.getBlockX();
        int by = location.getBlockY();
        int bz = location.getBlockZ();

        Random random = new Random();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < radius * radius && (!empty && distance < (radius - 1) * (radius - 1))) {
                        Block block = new Location(location.getWorld(), x, y, z).getBlock();

                        if (!smoothEdges && (x >= bx + radius - (radius / 10) || z >= bz + radius - (radius / 10)) && random.nextInt(3) != 0)
                            continue;

                        if ((block.getType() != Material.AIR && block.getType() != Material.CAVE_AIR) || !ignoreAir)
                            blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

    public static void drawParticlesForNearbyPlayers(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getWorld() != loc.getWorld() || p.getLocation().distance(loc) > 100)
                continue;
            p.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, speed);
        }
    }

    public static void drawDustsForNearbyPlayers(Location loc, int count, double offsetX, double offsetY, double offsetZ, Particle.DustOptions dust) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getWorld() != loc.getWorld() || p.getLocation().distance(loc) > 100)
                continue;
            p.spawnParticle(Particle.REDSTONE, loc, count, offsetX, offsetY, offsetZ, dust);
        }
    }

    public static ArrayList<Block> getBlocksInSquare(Block start, int radius, boolean ignoreAir) {
        if (radius < 0) {
            return new ArrayList<>(0);
        }
        int iterations = (radius * 2) + 1;
        ArrayList<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if ((start.getRelative(x, y, z).getType() != Material.AIR && start.getRelative(x, y, z).getType() != Material.CAVE_AIR) || !ignoreAir)
                        blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        return blocks;
    }
}
