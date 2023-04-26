package de.firecreeper82.handlers.spirits.impl;

import de.firecreeper82.handlers.spirits.Spirit;
import de.firecreeper82.lotm.Plugin;
import jline.internal.Nullable;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class MediumSpirit extends Spirit {

    public MediumSpirit(LivingEntity entity, double health, float particleOffset, int spawnRate, EntityType entityType, boolean visible, int spawnCount, @Nullable ItemStack drop) {
        super(entity, health, particleOffset, spawnRate, entityType, visible, spawnCount, drop);
    }

    @Override
    public Spirit initNew(LivingEntity entity) {
        return new MediumSpirit(entity, health, particleOffset, spawnRate, entityType, visible, spawnCount, drop);
    }

    private Particle.DustOptions dust;

    @Override
    public void start() {
        dust = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 5);
    }

    @Override
    public void tick() {
        for(Entity nearby : entity.getNearbyEntities(35, 35, 35)) {
            if(!(nearby instanceof Player p))
                continue;

            p.spawnParticle(Particle.REDSTONE, entity.getLocation(), 60, particleOffset, particleOffset, particleOffset, dust);
        }
    }

    @EventHandler
    public void onAttack(ProjectileLaunchEvent e) {
        if(!Objects.requireNonNull(e.getLocation().getWorld()).getNearbyEntities(e.getLocation(), 5, 5, 5).contains(entity))
            return;

        e.setCancelled(true);
    }
}
