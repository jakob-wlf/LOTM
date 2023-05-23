package de.firecreeper82.pathways.impl.disasters;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class Tsunami extends Disaster {
    private final HashMap<Integer, Integer> blockedSides;
    private final ArrayList<Integer> skipped;

    public Tsunami(Player p) {
        super(p);
        blockedSides = new HashMap<>();
        skipped = new ArrayList<>();
    }

    @Override
    public void spawnDisaster(Player p, Location loc) {
        Location eyeLoc = p.getEyeLocation();
        eyeLoc.setPitch(0);

        Location startLoc = eyeLoc.clone();

        Vector backDir = eyeLoc.getDirection().normalize().multiply(-55);
        startLoc.add(backDir);

        Vector dir = backDir.clone().normalize();

        eyeLoc.setYaw(eyeLoc.getYaw() + 90);
        Vector dirRight = eyeLoc.clone().getDirection().normalize();

        eyeLoc.setYaw(eyeLoc.getYaw() - 90);

        if (startLoc.getWorld() == null)
            return;

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;
                if (counter >= 120) {
                    cancel();
                    return;
                }

                for (int i = -10; i < Math.min(Math.sqrt(counter) * 1.5, 3020); i++) {
                    for (int j = -64; j < 65; j++) {
                        if ((blockedSides.containsKey(j))) {
                            if (blockedSides.get(j) >= i) {
                                continue;
                            }
                        }
                        if (skipped.contains(j))
                            continue;
                        Location tempLoc = startLoc.clone();
                        tempLoc.add(0, i, 0);
                        tempLoc.add(dirRight.clone().multiply(j));
                        if (!tempLoc.getBlock().getType().isSolid() || counter < 65)
                            tempLoc.getBlock().setType(Material.WATER);
                        else {
                            if (i == Math.min(Math.sqrt(counter), 20) - 1)
                                skipped.add(j);
                            else
                                blockedSides.put(j, i);
                        }
                    }
                }

                startLoc.subtract(dir);
                for (Entity entity : startLoc.getWorld().getNearbyEntities(startLoc, 8, 8, 8)) {
                    if (!(entity instanceof LivingEntity livingEntity) || entity == p)
                        continue;

                    livingEntity.damage(7, p);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }

    @Override
    public ItemStack getItem() {
        return UtilItems.getLightning();
    }
}
