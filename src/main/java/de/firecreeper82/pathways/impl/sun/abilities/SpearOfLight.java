package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.Objects;

public class SpearOfLight extends Recordable {
    public Block lastLightBlock;
    public Material lastMaterial;

    public SpearOfLight(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        if (!recorded)
            pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        destroy(beyonder, recorded);

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 40);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (!lastBlock.getType().isSolid()) {
                continue;
            }
            break;
        }

        double distance = lastBlock.getLocation().distance(p.getEyeLocation());

        Location loc = p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(distance)).clone();

        float angle = p.getEyeLocation().getYaw() / 60;

        Location spearLocation = p.getEyeLocation().subtract(Math.cos(angle), 0, Math.sin(angle));
        Vector dir = loc.toVector().subtract(spearLocation.toVector()).normalize();
        Vector direction = dir.clone();

        lastLightBlock = spearLocation.getBlock();
        lastMaterial = lastLightBlock.getType();

        buildSpear(spearLocation.clone(), dir);

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                spearLocation.add(direction);
                buildSpear(spearLocation.clone(), direction.clone());

                if (!Objects.requireNonNull(spearLocation.getWorld()).getNearbyEntities(spearLocation, 5, 5, 5).isEmpty()) {
                    for (Entity entity : spearLocation.getWorld().getNearbyEntities(spearLocation, 5, 5, 5)) {
                        if (entity instanceof LivingEntity) {
                            // Ignore player that initiated the shot
                            if (entity == p) {
                                continue;
                            }
                            Vector particleMinVector = new Vector(
                                    spearLocation.getX() - 0.25,
                                    spearLocation.getY() - 0.25,
                                    spearLocation.getZ() - 0.25);
                            Vector particleMaxVector = new Vector(
                                    spearLocation.getX() + 0.25,
                                    spearLocation.getY() + 0.25,
                                    spearLocation.getZ() + 0.25);

                            //entity hit
                            if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {

                                spearLocation.getWorld().spawnParticle(Particle.END_ROD, spearLocation, 200, 0, 0, 0, 0.5);

                                entity.setVelocity(entity.getVelocity().add(spearLocation.getDirection().normalize().multiply(1.5)));
                                if (((LivingEntity) entity).getCategory() == EntityCategory.UNDEAD)
                                    ((Damageable) entity).damage(85 * multiplier, p);
                                else
                                    ((Damageable) entity).damage(45 * multiplier, p);
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 15));

                                Location sphereLoc = ((LivingEntity) entity).getEyeLocation().clone();

                                new BukkitRunnable() {
                                    double sphereRadius = 1;

                                    @Override
                                    public void run() {
                                        for (double i = 0; i <= Math.PI; i += Math.PI / 25) {
                                            double radius = Math.sin(i) * sphereRadius;
                                            double y = Math.cos(i) * sphereRadius;
                                            for (double a = 0; a < Math.PI * 2; a += Math.PI / 25) {
                                                double x = Math.cos(a) * radius;
                                                double z = Math.sin(a) * radius;
                                                sphereLoc.add(x, y, z);
                                                Objects.requireNonNull(sphereLoc.getWorld()).spawnParticle(Particle.END_ROD, sphereLoc, 4, 0.15, 0.15, 0.15, 0);

                                                //damage entities
                                                if (!sphereLoc.getWorld().getNearbyEntities(sphereLoc, 2, 2, 2).isEmpty()) {
                                                    for (Entity entity : sphereLoc.getWorld().getNearbyEntities(sphereLoc, 5, 5, 5)) {
                                                        if (entity instanceof LivingEntity) {
                                                            // Ignore player that initiated the shot
                                                            if (entity == p) {
                                                                continue;
                                                            }
                                                            Vector particleMinVector = new Vector(
                                                                    sphereLoc.getX() - 0.25,
                                                                    sphereLoc.getY() - 0.25,
                                                                    sphereLoc.getZ() - 0.25);
                                                            Vector particleMaxVector = new Vector(
                                                                    sphereLoc.getX() + 0.25,
                                                                    sphereLoc.getY() + 0.25,
                                                                    sphereLoc.getZ() + 0.25);

                                                            //entity hit
                                                            if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                                                                if (((LivingEntity) entity).getCategory() == EntityCategory.UNDEAD)
                                                                    ((Damageable) entity).damage(65 * multiplier, p);
                                                                else
                                                                    ((Damageable) entity).damage(30 * multiplier, p);
                                                            }
                                                        }
                                                    }
                                                }

                                                sphereLoc.subtract(x, y, z);
                                            }
                                        }
                                        sphereRadius += 0.2;
                                        if (sphereRadius >= 7) {
                                            lastLightBlock.setType(lastMaterial);
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(Plugin.instance, 0, 0);
                                cancel();
                                return;
                            }
                        }
                    }
                }

                //hits solid block
                if (spearLocation.getBlock().getType().isSolid()) {
                    Location sphereLoc = spearLocation.clone();
                    new BukkitRunnable() {
                        double sphereRadius = 1;

                        @Override
                        public void run() {
                            for (double i = 0; i <= Math.PI; i += Math.PI / 27) {
                                double radius = Math.sin(i) * sphereRadius;
                                double y = Math.cos(i) * sphereRadius;
                                for (double a = 0; a < Math.PI * 2; a += Math.PI / 27) {
                                    double x = Math.cos(a) * radius;
                                    double z = Math.sin(a) * radius;
                                    sphereLoc.add(x, y, z);
                                    Objects.requireNonNull(sphereLoc.getWorld()).spawnParticle(Particle.END_ROD, sphereLoc, 1, 0.1, 0.1, 0.1, 0);

                                    //damage entities
                                    if (!sphereLoc.getWorld().getNearbyEntities(sphereLoc, 2, 2, 2).isEmpty()) {
                                        for (Entity entity : sphereLoc.getWorld().getNearbyEntities(sphereLoc, 5, 5, 5)) {
                                            if (entity instanceof LivingEntity) {
                                                // Ignore player that initiated the shot
                                                if (entity == p) {
                                                    continue;
                                                }
                                                Vector particleMinVector = new Vector(
                                                        sphereLoc.getX() - 0.25,
                                                        sphereLoc.getY() - 0.25,
                                                        sphereLoc.getZ() - 0.25);
                                                Vector particleMaxVector = new Vector(
                                                        sphereLoc.getX() + 0.25,
                                                        sphereLoc.getY() + 0.25,
                                                        sphereLoc.getZ() + 0.25);

                                                //entity hit
                                                if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                                                    if (((LivingEntity) entity).getCategory() == EntityCategory.UNDEAD)
                                                        ((Damageable) entity).damage(65 * multiplier, p);
                                                    else
                                                        ((Damageable) entity).damage(30 * multiplier, p);
                                                }
                                            }
                                        }
                                    }
                                    sphereLoc.subtract(x, y, z);
                                }
                            }
                            sphereRadius += 0.2;
                            if (sphereRadius >= 10) {
                                lastLightBlock.setType(lastMaterial);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Plugin.instance, 0, 0);
                    spearLocation.getWorld().spawnParticle(Particle.FLAME, spearLocation, 1000, 0.4, 0.4, 0.4, .15);
                    cancel();
                }
                if (counter >= 100) {
                    lastLightBlock.setType(lastMaterial);
                    cancel();
                    return;
                }
                counter++;
            }
        }.runTaskTimer(Plugin.instance, 5, 0);

        new BukkitRunnable() {
            public void run() {
                pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
            }
        }.runTaskLater(Plugin.instance, 20 * 3);
    }

    public void buildSpear(Location loc, Vector direc) {

        for (int i = 0; i < 6; i++) {
            loc.subtract(direc);
        }

        lastLightBlock.setType(lastMaterial);
        lastLightBlock = loc.getBlock();
        lastMaterial = lastLightBlock.getType();
        loc.getBlock().setType(Material.LIGHT);

        int circlePoints = 10;
        double radius = 0.2;
        Location playerLoc = loc.clone();
        Vector dir = loc.clone().getDirection().normalize().multiply(0.15);
        double pitch = (playerLoc.getPitch() + 90.0F) * 0.017453292F;
        double yaw = -playerLoc.getYaw() * 0.017453292F;
        double increment = (2 * Math.PI) / circlePoints;
        for (int k = 0; k < 5; k++) {
            radius -= 0.009;
            for (int i = 0; i < circlePoints; i++) {
                double angle = i * increment;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Vector vec = new Vector(x, 0, z);
                VectorUtils.rotateAroundAxisX(vec, pitch);
                VectorUtils.rotateAroundAxisY(vec, yaw);
                playerLoc.subtract(vec);
                Objects.requireNonNull(playerLoc.getWorld()).spawnParticle(Particle.ELECTRIC_SPARK, playerLoc.clone(), 1, 0, 0, 0, 0);
                playerLoc.add(vec);
            }
            playerLoc.subtract(dir);
        }

        direc.multiply(0.125);
        for (int i = 0; i < 96; i++) {
            Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.ELECTRIC_SPARK, loc.clone(), 10, .03, .03, .03, 0);
            loc.add(direc);
        }

        circlePoints = 20;
        radius = 0.3;
        playerLoc = loc.clone();
        dir = loc.clone().getDirection().normalize().multiply(0.15);
        pitch = (playerLoc.getPitch() + 90.0F) * 0.017453292F;
        yaw = -playerLoc.getYaw() * 0.017453292F;
        increment = (2 * Math.PI) / circlePoints;
        for (int k = 0; k < 13; k++) {
            radius -= 0.019;
            for (int i = 0; i < circlePoints; i++) {
                double angle = i * increment;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Vector vec = new Vector(x, 0, z);
                VectorUtils.rotateAroundAxisX(vec, pitch);
                VectorUtils.rotateAroundAxisY(vec, yaw);
                playerLoc.add(vec);
                Objects.requireNonNull(playerLoc.getWorld()).spawnParticle(Particle.ELECTRIC_SPARK, playerLoc.clone(), 1, 0, 0, 0, 0);
                playerLoc.subtract(vec);
            }
            playerLoc.add(dir);
        }
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.SPECTRAL_ARROW, "Spear of Light", "7500", identifier, 2, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
