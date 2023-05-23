package de.firecreeper82.handlers.spirits;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SpiritWorld {
    public SpiritWorld() {
        Random random = new Random();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getWorld().getName().equalsIgnoreCase("world_nether"))
                        continue;
                    p.spawnParticle(Particle.REDSTONE, p.getEyeLocation(), 50, 20, 20, 20, new Particle.DustOptions(Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)), random.nextFloat(.8f, 2.5f)));
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }
}
