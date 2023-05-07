package de.firecreeper82.pathways.impl.disasters;

import de.firecreeper82.lotm.Plugin;
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

public class MeteorShower extends Disaster{

    public MeteorShower(Player p) {
        super(p);
    }

    @Override
    public void spawnDisaster(Player p, Location loc) {
        this.p = p;

        Random random = new Random();

        for(int i = 0; i < 15; i++) {
            float angle = p.getEyeLocation().getYaw() / 60;

            int i1 = random.nextInt(-80, 80);
            int i2 = random.nextInt(-80, 80);

            Location startLoc = loc.clone().add(-Math.cos(angle) * 20 + random.nextInt(-50, 50), 35, -Math.sin(angle) * 20 + random.nextInt(-50, 50));
            Vector vector = loc.clone().add(i1, 0, i2).toVector().subtract(startLoc.toVector()).normalize().multiply(.6);

            World world = startLoc.getWorld();
            if(world == null)
                return;

            new BukkitRunnable() {
                @Override
                public void run() {
                    startLoc.add(vector);
                    spawnFallingBlocks(startLoc);

                    startLoc.add(vector);
                    world.spawnParticle(Particle.LAVA, startLoc, 35, 1, 1, 1, 0);
                    world.spawnParticle(Particle.SMOKE_NORMAL, startLoc, 35, 2, 2, 2, 0);

                    if(startLoc.getBlock().getType().isSolid()) {
                        for(FallingBlock block : currentBlock) {
                            block.setGravity(true);
                            Random random = new Random();
                            block.setVelocity(new Vector(random.nextInt(3), random.nextInt(3), random.nextInt(3)));
                        }
                        startLoc.getWorld().createExplosion(startLoc, 30, true);
                        startLoc.getWorld().createExplosion(startLoc.clone().add(12, 0, 0), 40, true);
                        startLoc.getWorld().createExplosion(startLoc.clone().add(-12, 0, 0), 40, true);
                        startLoc.getWorld().createExplosion(startLoc.clone().add(0, 0, 12), 40, true);
                        startLoc.getWorld().createExplosion(startLoc.clone().add(0, 0, -12), 40, true);
                        cancel();
                    }
                }
            }.runTaskTimer(Plugin.instance, random.nextInt(25), 1);
        }
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

        Material mat;
        mat = Material.MAGMA_BLOCK;
        FallingBlock block = world.spawnFallingBlock(startLoc, mat.createBlockData());
        block.setDropItem(false);
        block.setGravity(false);
        currentBlock.add(block);
    }

    @Override
    public ItemStack getItem() {
        return null;
    }
}
