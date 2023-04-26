package de.firecreeper82.handlers.spirits;

import de.firecreeper82.handlers.spirits.impl.FriendlySpirit;
import de.firecreeper82.handlers.spirits.impl.MediumSpirit;
import de.firecreeper82.handlers.spirits.impl.WeakSpirit;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SpiritHandler implements Listener {

    private final ArrayList<Spirit> spirits;
    private final EntityType[] spawnTypes;

    private final ArrayList<Spirit> spiritEntities;

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
        spiritEntities = new ArrayList<>();

        init();
    }

    private void init() {
        spirits.add(new FriendlySpirit(null, 15, .5f, 1, EntityType.ALLAY, false, 2, BeyonderItems.getSpiritRemains()));
        spirits.add(new WeakSpirit(null, 22, .25f, 5, EntityType.VEX, false, 1, BeyonderItems.getSpiritRemains()));
        spirits.add(new MediumSpirit(null, 22, 4f, 30, EntityType.GHAST, true, 1, null));
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if(!Arrays.asList(spawnTypes).contains(e.getEntity().getType()))
            return;

        Random random = new Random();

        for(Spirit spirit : spirits) {
            if(random.nextInt(spirit.getSpawnRate()) == 0) {
                for(int i = 0; i < spirit.getSpawnCount(); i++) {
                    LivingEntity entity = (LivingEntity) e.getEntity().getWorld().spawnEntity(e.getLocation(), spirit.getEntityType());

                    entity.setInvisible(!spirit.isVisible());

                    Spirit newSpirit = spirit.initNew(entity);
                    spiritEntities.add(newSpirit);

                    newSpirit.start();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(!entity.isValid()) {
                                cancel();
                                return;
                            }

                            newSpirit.tick();
                        }
                    }.runTaskTimer(Plugin.instance, 0, 0);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        for(Spirit spirit : spiritEntities) {
            if(spirit.getEntity() != e.getEntity() || spirit.getDrop() == null)
                continue;

            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), spirit.getDrop());
        }
    }
}
