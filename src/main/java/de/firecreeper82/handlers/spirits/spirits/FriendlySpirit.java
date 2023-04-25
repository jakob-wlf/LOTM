package de.firecreeper82.handlers.spirits.spirits;

import de.firecreeper82.handlers.spirits.Spirit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;

public class FriendlySpirit extends Spirit {

    public FriendlySpirit(int spawnRate, boolean hostile, double health, boolean visible, float particleOffset) {
        super(spawnRate, hostile, health, visible, particleOffset);
    }

    private Particle.DustOptions dust;

    @Override
    protected void start() {
        Random random = new Random();
        dust = new Particle.DustOptions(Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)), random.nextFloat(.3f, 3));
    }

    @Override
    protected void tick() {
        for(Entity nearby : entity.getNearbyEntities(10, 10, 10)) {
            if(nearby instanceof Player p) {
                p.spawnParticle(Particle.REDSTONE, entity.getLocation(), 15, particleOffset, particleOffset, particleOffset, dust);
            }
            break;
        }
    }
}
