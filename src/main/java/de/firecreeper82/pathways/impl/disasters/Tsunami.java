package de.firecreeper82.pathways.impl.disasters;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Tsunami extends Disaster{
    public Tsunami(Player p) {
        super(p);
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

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                if(counter >= 120) {
                    cancel();
                    return;
                }

                for(int i = -10; i < Math.min(Math.sqrt(counter), 20); i++) {
                    for(int j = -64; j < 65; j++) {
                        Location tempLoc = startLoc.clone();
                        tempLoc.add(0, i, 0);
                        tempLoc.add(dirRight.clone().multiply(j));
                        if(!tempLoc.getBlock().getType().isSolid())
                            tempLoc.getBlock().setType(Material.WATER);
                    }
                }

                startLoc.subtract(dir);
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }

    @Override
    public ItemStack getItem() {
        return UtilItems.getLightning();
    }
}
