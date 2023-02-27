package de.firecreeper82.listeners;

import de.firecreeper82.lotm.Plugin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    //Check if Entity that dies is a FakePlayer from the hashMap in the Plugin class and remove him onDeath if he is
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(Plugin.fakePlayers.containsKey(e.getEntity().getUniqueId())) {
            e.setDeathMessage(null);
            Location loc = e.getEntity().getLocation();
            if(loc.getWorld() != null)
                loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().subtract(0, 0.25, 0), 100, 0.35, 1, 0.35, 0);
            ServerLevel nmsWorld = ((CraftWorld) e.getEntity().getWorld()).getHandle();
            nmsWorld.removePlayerImmediately(Plugin.fakePlayers.get(e.getEntity().getUniqueId()), Entity.RemovalReason.DISCARDED);
        }
    }
}
