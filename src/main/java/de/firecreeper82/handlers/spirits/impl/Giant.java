package de.firecreeper82.handlers.spirits.impl;

import de.firecreeper82.handlers.spirits.Spirit;
import de.firecreeper82.lotm.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Giant extends Spirit {

    private Player target;
    private Enderman brain;
    private boolean hasAttacked;

    public Giant(LivingEntity entity, double health, float particleOffset, int spawnRate, EntityType entityType, boolean visible, int spawnCount, ItemStack drop, boolean undead, String name) {
        super(entity, health, particleOffset, spawnRate, entityType, visible, spawnCount, drop, undead, name);

        target = null;
        hasAttacked = false;
    }

    @Override
    public Spirit initNew(LivingEntity entity) {
        return new Giant(entity, health, particleOffset, spawnRate, entityType, visible, spawnCount, drop, undead, name);
    }

    @Override
    public void start() {
        entity.setAI(true);
        ((Mob) entity).setAware(true);

        brain = (Enderman) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ENDERMAN);
        brain.setInvisible(true);
        entity.addPassenger(brain);
    }

    @Override
    public void stop() {
        entity.remove();
        brain.remove();
    }

    @Override
    public void tick() {
        if (!brain.isValid())
            entity.remove();

        if (!entity.isValid())
            brain.remove();

        if (target != null) {
            if (target.getWorld() != entity.getWorld() || !target.isValid())
                target = null;
        }

        if (target == null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().getWorld() != entity.getWorld())
                    continue;
                if (p.getLocation().distance(entity.getLocation()) <= 15 && p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {
                    target = p;
                }
            }
        }

        if (target == null)
            return;

        brain.setTarget(target);

        if (hasAttacked)
            return;

        if (target.getLocation().distance(entity.getLocation()) > 2.25)
            return;

        entity.attack(target);
        hasAttacked = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                hasAttacked = false;
            }
        }.runTaskLater(Plugin.instance, 30);
    }
}
