package dev.ua.ikeepcalm.mystical.pathways.disasters;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.utils.GeneralItemsUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Lightning extends Disaster {
    public Lightning(Player p) {
        super(p);
    }

    @Override
    public void spawnDisaster(LivingEntity e, Location loc) {
        Location startLoc = loc.clone();
        World world = startLoc.getWorld();

        if (world == null)
            return;

        new BukkitRunnable() {

            int counter = 0;

            @Override
            public void run() {
                counter++;

                world.setClearWeatherDuration(0);
                world.setStorm(true);
                world.setThundering(true);
                world.setThunderDuration(10 * 60 * 20);

                Random random = new Random();
                for (int i = 0; i < 8; i++) {
                    world.strikeLightning(startLoc.clone().add(random.nextInt(25) - 12.5, 0, random.nextInt(25) - 12.5));
                }

                if (counter >= 100) {
                    cancel();
                }
            }
        }.runTaskTimer(LordOfTheMinecraft.instance, 0, 2);
    }

    @Override
    public ItemStack getItem() {
        return GeneralItemsUtil.getLightning();
    }
}
