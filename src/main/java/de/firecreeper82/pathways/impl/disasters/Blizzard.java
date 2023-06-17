package de.firecreeper82.pathways.impl.disasters;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class Blizzard extends Disaster {
    public Blizzard(LivingEntity e) {
        super(e);
    }

    @Override
    public void spawnDisaster(LivingEntity e, Location loc) {
        Location startLoc = e.getEyeLocation();
        World world = startLoc.getWorld();

        ArrayList<Block> blocks = Util.getBlocksInSquare(startLoc.getBlock(), 30, true);
        Random random = new Random();

        if (world == null)
            return;

        new BukkitRunnable() {

            int counter = 20 * 60 * 2;

            @Override
            public void run() {
                counter--;

                if (counter <= 0) {
                    cancel();
                    return;
                }

                if (counter % 10 == 0) {
                    for (Entity entity : world.getNearbyEntities(startLoc, 80, 80, 80)) {
                        if (!(entity instanceof LivingEntity livingEntity) || entity == e || entity.getType() == EntityType.ARMOR_STAND)
                            continue;

                        livingEntity.damage(15, e);
                    }
                }

                for (Entity entity : world.getNearbyEntities(startLoc, 40, 40, 40)) {
                    if (!(entity instanceof LivingEntity livingEntity) || entity == e || entity.getType() == EntityType.ARMOR_STAND)
                        continue;

                    livingEntity.setFreezeTicks(20 * 60);
                }

                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(player.getLocation().distance(loc) <= 100)
                        player.spawnParticle(Particle.SNOWFLAKE, startLoc, 500, 25, 25, 25, 0);
                }
                for (int i = 0; i < 80; i++) {
                    int temp = random.nextInt(blocks.size());
                    if (blocks.get(temp).getLocation().clone().add(0, 1, 0).getBlock().getType().isSolid())
                        continue;
                    blocks.get(temp).getLocation().clone().add(0, 1, 0).getBlock().setType(Material.SNOW);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }

    @Override
    public ItemStack getItem() {
        return UtilItems.getLightning();
    }
}
