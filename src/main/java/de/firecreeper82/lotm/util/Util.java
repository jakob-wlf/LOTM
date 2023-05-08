package de.firecreeper82.lotm.util;

import jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;


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

    public static ArrayList<Block> getNearbyBlocksInSphere(Location location, int radius, boolean empty, boolean ignoreAir) {
        ArrayList<Block> blocks = new ArrayList<>();

        int bx = location.getBlockX();
        int by = location.getBlockY();
        int bz = location.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < radius * radius && (!empty && distance < (radius - 1) * (radius - 1))) {
                        Block block = new Location(location.getWorld(), x, y, z).getBlock();
                        if ((block.getType() != Material.AIR && block.getType() != Material.CAVE_AIR) || !ignoreAir)
                            blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

    public static ArrayList<Block> getBlocksInSquare(Block start, int radius) {
        if (radius < 0) {
            return new ArrayList<>(0);
        }
        int iterations = (radius * 2) + 1;
        ArrayList<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Entity> getEntitiesInRadius(Player player, double radius) {
        List<Entity> nearbyEntities = new ArrayList<>();
        Location playerLocation = player.getLocation();

        for (Entity entity : Objects.requireNonNull(playerLocation.getWorld()).getEntities()) {
            Location entityLocation = entity.getLocation();

            // Calculate the distance between the player and the entity
            double distance = entityLocation.distance(playerLocation);

            // Check if the entity is within the specified radius
            if (distance <= radius) {
                nearbyEntities.add(entity);
            }
        }

        return nearbyEntities;
    }

    public static double getEquipmentBonus(Player player) {
        ItemStack itemStack = player.getInventory().getItem(EquipmentSlot.HAND);
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta instanceof Damageable damageable) {

                int maxDurability = itemStack.getType().getMaxDurability();
                int currentDamage = damageable.getDamage();
                if (maxDurability > 0 && currentDamage >= 0 && currentDamage <= maxDurability) {
                    return (double) (maxDurability - currentDamage) / maxDurability;
                }
            }
        }
        return 0.0;
    }

    public static int getEnchantmentBonus(Player player) {
        ItemStack itemStack = player.getInventory().getItem(EquipmentSlot.HAND);

        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta != null) {

                return itemMeta.getEnchantLevel(Enchantment.DAMAGE_ALL);
            }
        }
        return 0;
    }

    public static HashMap<PotionEffectType, Integer> getPotionCombatFormula(Player player) {
        HashMap<PotionEffectType, Integer> potions = new HashMap<>();

        PotionEffect increaseDamageEffect = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
        PotionEffect weaknessEffect = player.getPotionEffect(PotionEffectType.WEAKNESS);

        if (increaseDamageEffect != null) {
            potions.put(PotionEffectType.INCREASE_DAMAGE, increaseDamageEffect.getAmplifier());
        } else {
            potions.put(PotionEffectType.INCREASE_DAMAGE, 0);
        }

        if (weaknessEffect != null) {
            potions.put(PotionEffectType.WEAKNESS, weaknessEffect.getAmplifier());
        } else {
            potions.put(PotionEffectType.WEAKNESS, 0);
        }
        return potions;
    }

    public static double calculatePlayerDamage(Player player) {
        // Retrieve player's attack damage attribute
        var attackDamage = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getValue();
        var equipmentBonus = getEquipmentBonus(player);
        var baseDamagePlayer = equipmentBonus + attackDamage;
        var potionEffectBonus = getPotionCombatFormula(player);
        double enchantmentBonus = getEnchantmentBonus(player);
        return (baseDamagePlayer + enchantmentBonus) * (1 + potionEffectBonus.get(PotionEffectType.INCREASE_DAMAGE)) * (1 + potionEffectBonus.get(PotionEffectType.WEAKNESS));
    }
}
