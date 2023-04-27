package de.firecreeper82.handlers.spirits.impl;

import de.firecreeper82.handlers.spirits.Spirit;
import jline.internal.Nullable;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FriendlySpirit extends Spirit {

    public FriendlySpirit(LivingEntity entity, double health, float particleOffset, int spawnRate, EntityType entityType, boolean visible, int spawnCount, @Nullable ItemStack drop, boolean undead, String name) {
        super(entity, health, particleOffset, spawnRate, entityType, visible, spawnCount, drop, undead, name);
    }

    @Override
    public Spirit initNew(LivingEntity entity) {
        return new FriendlySpirit(entity, health, particleOffset, spawnRate, entityType, visible, spawnCount, drop, undead, name);
    }

    private Particle.DustOptions dust;

    @Override
    public void start() {
        Random random = new Random();
        dust = new Particle.DustOptions(Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 2);
    }

    @Override
    public void tick() {
        for(Entity nearby : entity.getNearbyEntities(35, 35, 35)) {
            if(!(nearby instanceof Player p))
                continue;

            p.spawnParticle(Particle.REDSTONE, entity.getLocation(), 20, particleOffset, particleOffset, particleOffset, dust);
        }
    }
}
