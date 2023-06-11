package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.Objects;

public class UnshadowedSpear extends Ability {
    public Block lastLightBlock;
    public Material lastMaterial;

    public UnshadowedSpear(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        p = pathway.getBeyonder().getPlayer();

        double multiplier = getMultiplier();

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
        Vector dir = loc.toVector().subtract(spearLocation.toVector()).normalize().multiply(2);
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
                                    ((Damageable) entity).damage(25 * multiplier, p);
                                else
                                    ((Damageable) entity).damage(14 * multiplier, p);

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
                                                Particle.DustOptions dustSphere = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                                                Objects.requireNonNull(sphereLoc.getWorld()).spawnParticle(Particle.REDSTONE, sphereLoc, 4, 0.15, 0.15, 0.15, 0, dustSphere);
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
                                    Particle.DustOptions dustSphere = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                                    Objects.requireNonNull(sphereLoc.getWorld()).spawnParticle(Particle.REDSTONE, sphereLoc, 1, 0.25, 0.25, 0.25, 0, dustSphere);

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
                                                        ((Damageable) entity).damage(18 * multiplier, p);
                                                    else
                                                        ((Damageable) entity).damage(8 * multiplier, p);
                                                }
                                            }
                                        }
                                    }

                                    if (sphereLoc.getBlock().getType().getHardness() > -1)
                                        sphereLoc.getBlock().setType(Material.AIR);
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
                    spearLocation.getWorld().spawnParticle(Particle.END_ROD, spearLocation, 1000, 0.4, 0.4, 0.4, 0.5);
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
        }.runTaskLater(Plugin.instance, 15);
    }

    public void buildSpear(Location loc, Vector direc) {

        Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), .3f);

        for (int i = 0; i < 6; i++) {
            loc.subtract(direc);
        }

        lastLightBlock.setType(lastMaterial);
        lastLightBlock = loc.getBlock();
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
                Objects.requireNonNull(playerLoc.getWorld()).spawnParticle(Particle.REDSTONE, playerLoc.clone(), 1, 0, 0, 0, 0, dustRipple);
                playerLoc.add(vec);
            }
            playerLoc.subtract(dir);
        }

        direc.multiply(0.125);
        for (int i = 0; i < 64; i++) {
            Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc.clone().subtract(.03, .03, .03), 30, 0.03, 0.03, 0.03, 0, dustRipple);
            loc.add(direc);
        }

        circlePoints = 20;
        radius = 0.25;
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
                Objects.requireNonNull(playerLoc.getWorld()).spawnParticle(Particle.REDSTONE, playerLoc.clone().subtract(0, 0.1, 0), 1, 0, 0, 0, 0, dustRipple);
                playerLoc.subtract(vec);
            }
            playerLoc.add(dir);
        }
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.SPECTRAL_ARROW, "Unshadowed Spear", "1300", identifier, 4, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
