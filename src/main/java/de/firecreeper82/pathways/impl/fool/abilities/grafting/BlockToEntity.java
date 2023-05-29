package de.firecreeper82.pathways.impl.fool.abilities.grafting;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class BlockToEntity {

    public BlockToEntity(Entity entity, Material material, Player player) {
        Random random = new Random();
        new BukkitRunnable() {
            @Override
            public void run() {
                Block block;
                ArrayList<Block> validBlocks = new ArrayList<>();
                for (Block b : getNearbyBlocks(entity.getLocation(), 20)) {
                    if (b.getType() != material)
                        continue;

                    validBlocks.add(b);
                }

                if (validBlocks.isEmpty())
                    return;

                block = validBlocks.get(random.nextInt(validBlocks.size()));

                for (int i = 0; i < 50; i++) {
                    if (block.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR)
                        break;
                    block = validBlocks.get(random.nextInt(validBlocks.size()));
                }

                World world = entity.getWorld();
                FallingBlock fallingBlock = world.spawnFallingBlock(block.getLocation().add(0, 2.5, 0), material.createBlockData());
                fallingBlock.setDropItem(false);
                fallingBlock.setGravity(false);
                fallingBlock.setHurtEntities(true);

                new BukkitRunnable() {
                    int counter = 0;
                    final double offsetX = random.nextDouble(-1, 1);
                    final double offsetZ = random.nextDouble(-1, 1);
                    final double offsetY = random.nextDouble(-.75, .1);

                    @Override
                    public void run() {
                        if (entity.getLocation().distance(fallingBlock.getLocation()) >= 3.5) {
                            Vector dir = entity.getLocation().add(offsetX, entity.getHeight() + offsetY, offsetZ).toVector().subtract(fallingBlock.getLocation().toVector()).normalize().multiply(.75);
                            fallingBlock.setVelocity(dir);
                        } else {
                            Vector dir = entity.getLocation().add(offsetX, entity.getHeight() + offsetY, offsetZ).toVector().subtract(fallingBlock.getLocation().toVector()).normalize().multiply(.2);
                            fallingBlock.setVelocity(dir);

                            if (counter % 8 == 0 && entity instanceof Damageable damageable)
                                damageable.damage(4);
                        }
                        counter++;
                        if (counter > 20 * 30 * 2 || !entity.isValid()) {
                            fallingBlock.remove();
                            cancel();
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 0);
                block.setType(Material.AIR);

                if (!entity.isValid())
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 8);
    }

    @SuppressWarnings("all")
    private List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
}
