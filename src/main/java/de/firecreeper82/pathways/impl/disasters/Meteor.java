package de.firecreeper82.pathways.impl.disasters;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Meteor extends Disaster{

    public Meteor(Player p) {
        super(p);
    }

    @Override
    public void spawnDisaster(Player p, Location loc) {
        this.p = p;

        float angle = p.getEyeLocation().getYaw() / 60;

        Location startLoc = loc.clone().add(-Math.cos(angle) * 20, 35, -Math.sin(angle) * 20);
        Vector vector = loc.toVector().subtract(startLoc.toVector()).normalize().multiply(.6);

        World world = startLoc.getWorld();
        if(world == null)
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                startLoc.add(vector);
                spawnFallingBlocks(startLoc);

                startLoc.add(vector);
                world.spawnParticle(Particle.FLAME, startLoc, 15, 2, 2, 2, 0);
                world.spawnParticle(Particle.LAVA, startLoc, 35, 1, 1, 1, 0);
                world.spawnParticle(Particle.SMOKE_LARGE, startLoc, 35, 2, 2, 2, 0);

                if(startLoc.getBlock().getType().isSolid()) {
                    for(FallingBlock block : currentBlock) {
                        block.setGravity(true);
                        Random random = new Random();
                        block.setVelocity(new Vector(random.nextInt(3), random.nextInt(3), random.nextInt(3)));
                    }
                    startLoc.getWorld().createExplosion(startLoc, 40, true);
                    startLoc.getWorld().createExplosion(startLoc.clone().add(12, 0, 0), 80, true);
                    startLoc.getWorld().createExplosion(startLoc.clone().add(-12, 0, 0), 80, true);
                    startLoc.getWorld().createExplosion(startLoc.clone().add(0, 0, 12), 80, true);
                    startLoc.getWorld().createExplosion(startLoc.clone().add(0, 0, -12), 80, true);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    private final List<FallingBlock> currentBlock = new ArrayList<>();

    public void spawnFallingBlocks(Location loc) {

        for(FallingBlock block : currentBlock) {
            block.remove();
        }

        Location startLoc = loc.clone();
        World world = startLoc.getWorld();
        if(world == null)
            return;

        final int[][] meteorBlocks = {
                {2, 1, -1, 0}, {2, 1, 0, 0}, {2, 1, 1, 1},
                {2, 0, -1, 0}, {2, 0, 0, 0}, {2, 0, 1, 0},
                {2, -1, -1, 1}, {2, -1, 0, 0}, {2, -1, 1, 0},

                {-2, 1, -1, 0}, {-2, 1, 0, 0}, {-2, 1, 1, 0},
                {-2, 0, -1, 0}, {-2, 0, 0, 1}, {-2, 0, 1, 1},
                {-2, -1, -1, 0}, {-2, -1, 0, 0}, {-2, -1, 1, 1},


                {-1, 1, 2, 1}, {0, 1, 2, 0}, {1, 1, 2, 0},
                {-1, 0, 2, 0}, {0, 0, 2, 0}, {1, 0, 2, 0},
                {-1, -1, 2, 1}, {0, -1, 2, 1}, {1, -1, 2, 1},

                {-1, 1, -2, 0}, {0, 1, -2, 0}, {1, 1, -2, 0},
                {-1, 0, -2, 0}, {0, 0, -2, 0}, {1, 0, -2, 1},
                {-1, -1, -2, 1}, {0, -1, -2, 1}, {1, -1, -2, 0},


                {-1, 2, 1, 0}, {0, 2, 1, 1}, {1, 2, 1, 0},
                {-1, 2, 0, 0}, {0, 2, 0, 0}, {1, 2, 0, 0},
                {-1, 2, -1, 1}, {0, 2, -1, 0}, {1, 2, -1, 0},

                {-1, -2, 1, 1, 0}, {0, -2, 1, 0}, {1, -2, 1, 0},
                {-1, -2, 0, 0}, {0, -2, 0, 1, 0}, {1, -2, 0, 0},
                {-1, -2, -1, 0}, {0, -2, -1, 1, 0}, {1, -2, -1, 0}


        };

        for(int[] i : meteorBlocks) {
            Material mat;
            if(i[3] == 1)
                mat = Material.OBSIDIAN;
            else
                mat = Material.MAGMA_BLOCK;
            FallingBlock block = world.spawnFallingBlock(startLoc.clone().add(i[0], i[1], i[2]), mat.createBlockData());
            block.setDropItem(false);
            block.setGravity(false);
            currentBlock.add(block);
        }
    }

    @Override
    public ItemStack getItem() {
        return UtilItems.getMeteor();
    }
}
