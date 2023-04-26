package de.firecreeper82.handlers.spirits;

import de.firecreeper82.handlers.spirits.impl.FriendlySpirit;
import de.firecreeper82.lotm.Plugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SpiritHandler implements Listener {

    private final ArrayList<Spirit> spirits;
    private final EntityType[] spawnTypes;

    public SpiritHandler() {
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        spirits = new ArrayList<>();
        spawnTypes = new EntityType[] {
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.PIGLIN,
                EntityType.MAGMA_CUBE,
                EntityType.HOGLIN,
                EntityType.PIGLIN,
                EntityType.STRIDER,
                EntityType.GHAST,
                EntityType.BLAZE,
                EntityType.WITHER_SKELETON
        };

        init();
    }

    private void init() {
        spirits.add(new FriendlySpirit(null, 15, .5f, 1, false, false));
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if(!Arrays.asList(spawnTypes).contains(e.getEntity().getType()))
            return;

        Random random = new Random();

        for(Spirit spirit : spirits) {
            if(random.nextInt(spirit.getSpawnRate()) == 0) {
                LivingEntity entity = (LivingEntity) ((spirit.isHostile()) ?  e.getEntity().getWorld().spawnEntity(e.getLocation(), EntityType.VEX) : e.getEntity().getWorld().spawnEntity(e.getLocation(), EntityType.ALLAY));

                entity.setInvisible(!spirit.isVisible());

                Spirit newSpirit = spirit.initNew(entity);

                newSpirit.start();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        newSpirit.tick();
                    }
                }.runTaskTimer(Plugin.instance, 0, 0);
            }
        }
    }
}
