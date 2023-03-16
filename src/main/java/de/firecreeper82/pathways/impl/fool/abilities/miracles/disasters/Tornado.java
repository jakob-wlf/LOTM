package de.firecreeper82.pathways.impl.fool.abilities.miracles.disasters;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Tornado extends Disaster{

    public Tornado(Player p) {
        super(p);
    }

    @Override
    public void spawnDisaster(Player p, Location loc) {
        Location location = loc.clone();
        World world = location.getWorld();

        if(world == null)
            return;

        for(int i = 0; i < 20; i++) {
            tornadoPositive(world, location, i * 15);
            tornadoNegative(world, location, i * 15);
        }
    }

    @Override
    public ItemStack getItem() {
        return UtilItems.getTornado();
    }

    public void tornadoPositive(World world, Location location, int delay) {
        new BukkitRunnable() {
            double spiralRadius = .5;

            double spiral = 0;
            double height = 0;
            double spiralX;
            double spiralZ;

            int counter = 0;

            @Override
            public void run() {
                spiralX = spiralRadius * Math.cos(spiral);
                spiralZ = spiralRadius * Math.sin(spiral);
                spiral += 0.25;
                height += .15;

                world.spawnParticle(Particle.CLOUD, location.getX() + spiralX, location.getY() + height, location.getZ() + spiralZ, 1, .5, .5, .5, 0);

                if(height >= 20) {
                    height = 0;
                    spiralRadius = 1;
                }

                spiralRadius += .075;
                counter++;
                if(counter >= 75) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, delay, 0);
    }

    public void tornadoNegative(World world, Location location, int delay) {
        new BukkitRunnable() {
            double spiralRadius = 1;

            double spiral = 0;
            double height = 0;
            double spiralX;
            double spiralZ;

            int counter = 0;

            @Override
            public void run() {
                spiralX = spiralRadius * Math.cos(spiral);
                spiralZ = spiralRadius * Math.sin(spiral);
                spiral += 0.25;
                height += .15;

                world.spawnParticle(Particle.CLOUD, location.getX() - spiralX, location.getY() + height, location.getZ() - spiralZ, 1, .5, .5, .5, 0);

                if(height >= 20) {
                    height = 0;
                    spiralRadius = 1;
                }

                spiralRadius += .075;
                counter++;
                if(counter >= 500) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, delay, 0);
    }
}
