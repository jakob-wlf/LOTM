package de.firecreeper82.handlers.spirits;

import de.firecreeper82.handlers.spirits.impl.*;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.lotm.util.Util;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class SpiritHandler implements Listener, CommandExecutor {

    private final EntityType[] spawnTypes;

    private final ArrayList<Spirit> spirits;
    private final ArrayList<Spirit> spiritEntities;

    public SpiritHandler() {
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        Objects.requireNonNull(Plugin.instance.getCommand("spirit")).setExecutor(this);

        spirits = new ArrayList<>();
        spiritEntities = new ArrayList<>();
        spawnTypes = new EntityType[]{
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.PIGLIN,
                EntityType.MAGMA_CUBE,
                EntityType.HOGLIN,
                EntityType.PIGLIN,
                EntityType.STRIDER,
                EntityType.GHAST,
                EntityType.BLAZE,
                EntityType.WITHER_SKELETON,
                EntityType.SKELETON,
                EntityType.PIGLIN_BRUTE,
                EntityType.WITHER_SKELETON
        };

        init();
    }

    private void init() {
        spirits.add(new FriendlySpirit(null, 15, .5f, 1, EntityType.ALLAY, false, 2, BeyonderItems.getSpiritRemains(), false, "§bSpirit"));
        spirits.add(new WeakSpirit(null, 22, .25f, 5, EntityType.VEX, false, 1, BeyonderItems.getSpiritRemains(), false, "§5Weak Spirit"));
        spirits.add(new MediumSpirit(null, 22, 2.5f, 40, EntityType.GHAST, true, 1, null, false, "§0Angry Spirit"));
        spirits.add(new Giant(null, 50, 1, 15, EntityType.GIANT, true, 1, null, true, "§2Undead Giant"));
        spirits.add(new SkeletonHorse(null, 30, .6f, 3, EntityType.SKELETON_HORSE, true, 2, null, true, "§fHorse Spirit"));
        spirits.add(new UndeadHorse(null, 30, .6f, 6, EntityType.ZOMBIE_HORSE, true, 1, null, true, "§2Undead Horse"));
        spirits.add(new TallSkeleton(null, 55, 1, 8, EntityType.SKELETON, true, 1, null, true, "§fBone Amalgamation"));
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if (!e.getEntity().getWorld().getName().equalsIgnoreCase("world_nether") || !Arrays.asList(spawnTypes).contains(e.getEntity().getType()))
            return;

        Random random = new Random();

        for (Spirit spirit : spirits) {
            if (spirit.isUndead() && e.getLocation().getBlock().getBiome() != Biome.SOUL_SAND_VALLEY)
                continue;

            if (random.nextInt(spirit.getSpawnRate()) == 0) {
                for (int i = 0; i < spirit.getSpawnCount(); i++) {
                    if (spiritEntities.size() > 300) {
                        spiritEntities.get(0).getEntity().remove();
                    }

                    LivingEntity entity = (LivingEntity) e.getEntity().getWorld().spawnEntity(e.getLocation(), spirit.getEntityType(), false);

                    entity.setInvisible(spirit.isInvisible());
                    entity.setCustomName(spirit.getName());

                    Spirit newSpirit = spirit.initNew(entity);
                    spiritEntities.add(newSpirit);

                    newSpirit.start();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!entity.isValid()) {
                                cancel();
                                newSpirit.stop();
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
        for (Spirit spirit : spiritEntities) {
            if (spirit.getEntity() != e.getEntity() || spirit.getDrop() == null)
                continue;

            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), spirit.getDrop());
        }
    }

    @Override
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command cmd, @NonNull String label, String[] args) {
        Player p = (Player) s;

        int index = Util.parseInt(args[0]);
        Spirit spirit = spirits.get(index);

        LivingEntity entity = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), spirit.getEntityType(), false);

        entity.setInvisible(spirit.isInvisible());
        entity.setMetadata("spirit", new FixedMetadataValue(Plugin.instance, true));

        Spirit newSpirit = spirit.initNew(entity);
        spiritEntities.add(newSpirit);

        newSpirit.start();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!entity.isValid()) {
                    cancel();
                    return;
                }

                newSpirit.tick();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);

        return true;
    }
}
