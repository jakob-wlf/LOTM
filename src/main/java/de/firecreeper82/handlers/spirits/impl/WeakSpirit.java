package de.firecreeper82.handlers.spirits.impl;

import de.firecreeper82.handlers.spirits.Spirit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeakSpirit extends Spirit {

    public WeakSpirit(LivingEntity entity, double health, float particleOffset, int spawnRate, boolean hostile, boolean visible, int spawnCount, ItemStack drop) {
        super(entity, health, particleOffset, spawnRate, hostile, visible, spawnCount, drop);
    }

    @Override
    public Spirit initNew(LivingEntity entity) {
        return new WeakSpirit(entity, health, particleOffset, spawnRate, hostile, visible, spawnCount, drop);
    }

    private Particle.DustOptions dust;

    @Override
    public void start() {
        dust = new Particle.DustOptions(Color.fromRGB(196, 21, 14), 2);
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
