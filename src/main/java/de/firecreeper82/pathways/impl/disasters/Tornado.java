package de.firecreeper82.pathways.impl.disasters;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class Tornado extends Disaster {

    public Tornado(LivingEntity p) {
        super(p);
    }

    @Override
    public void spawnDisaster(LivingEntity p, Location loc) {
        Location location = loc.clone();
        World world = location.getWorld();

        if (world == null)
            return;

        //Particles
        new BukkitRunnable() {
            int counter = 0;

            final Random random = new Random();
            Vector currentDirection = new Vector(random.nextDouble(3) - 1.5, 0, random.nextDouble(3) - 1.5).normalize().multiply(.2);

            //Variables for entity movement
            final double spiralRadiusVel = 1.25;

            double spiralVel = 0;
            double spiralXVel;
            double spiralZVel;

            @Override
            public void run() {

                //variables for tornado rendering
                double spiralRadius = .5;

                double spiral = 0;
                double height = 0;
                double spiralX;
                double spiralZ;

                //Tornado rendering
                while (height < 22) {
                    spiralX = spiralRadius * Math.cos(spiral);
                    spiralZ = spiralRadius * Math.sin(spiral);
                    spiral += .25;
                    height += .15;

                    world.spawnParticle(Particle.CLOUD, location.getX() + spiralX, location.getY() + height, location.getZ() + spiralZ, 2, 1.25, 1.75, 1.25, 0);

                    if (height >= 22) {
                        break;
                    }

                    spiralRadius += .075;
                }

                //random tornado movement
                location.add(currentDirection);
                while (location.getBlock().getType().isSolid()) {
                    location.add(0, 1, 0);
                }

                Location tempLoc = location.clone();
                tempLoc.subtract(0, 1, 0);
                int whileCounter = 250;
                while (!tempLoc.getBlock().getType().isSolid() && whileCounter >= 0) {
                    tempLoc.subtract(0, 1, 0);
                    whileCounter--;
                }

                if(whileCounter <= 1)
                    return;

                location.setY(tempLoc.getY() + 1);

                if (random.nextInt(30) == 0)
                    currentDirection = new Vector(random.nextDouble(3) - 1.5, 0, random.nextDouble(3) - 1.5).normalize().multiply(.2);

                //Entity movement
                spiralXVel = spiralRadiusVel * Math.cos(spiralVel) * .8;
                spiralZVel = spiralRadiusVel * Math.sin(spiralVel) * .8;
                spiralVel += .25;

                //Apply velocity to entities
                for (Entity e : world.getNearbyEntities(location, 9.5, 20, 9.5)) {
                    if (e == p || e.getType() == EntityType.ARMOR_STAND)
                        continue;

                    if (e instanceof LivingEntity livingEntity)
                        livingEntity.damage(4, p);

                    Location pLoc = e.getLocation().clone();
                    pLoc.setY(location.getY());
                    if (pLoc.distance(location) > 8.5) {
                        e.setVelocity(location.clone().toVector().subtract(pLoc.toVector()).normalize().multiply(.35));
                    } else {
                        e.setVelocity(new Vector(spiralXVel, .25, spiralZVel));
                    }
                }

                //Falling Blocks
                Block block = tempLoc.getBlock();
                Location blockLoc = tempLoc.clone();
                blockLoc.add(0, 1, 0);
                FallingBlock fallingBlock = world.spawnFallingBlock(blockLoc, block.getBlockData());
                fallingBlock.setVelocity(new Vector(0, .1, 0));
                fallingBlock.setDropItem(false);
                block.setType(Material.AIR);

                counter++;
                if (counter >= 500) {
                    cancel();
                }

            }
        }.runTaskTimer(Plugin.instance, 0, 0);

    }

    @Override
    public ItemStack getItem() {
        return UtilItems.getTornado();
    }
}
