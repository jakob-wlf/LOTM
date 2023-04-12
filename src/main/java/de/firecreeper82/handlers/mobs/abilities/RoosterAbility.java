package de.firecreeper82.handlers.mobs.abilities;

import de.firecreeper82.pathways.MobUsableAbility;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class RoosterAbility extends MobUsableAbility {
    public RoosterAbility(int frequency) {
        super(frequency);
    }

    @Override
    public void useAbility() {

    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public void useAbility(Location startLoc, Location endLoc, double multiplier, Entity user, Entity target) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
        user.getWorld().spawnParticle(Particle.REDSTONE, user.getLocation(), 200, 2, 2, 2, dust);
        user.getWorld().spawnParticle(Particle.END_ROD, user.getLocation(), 350, 0.5, 0.5, 0.5, .25);
        for(Entity entity : user.getNearbyEntities(5, 5, 5)) {
            if(entity instanceof Damageable damageable)
                damageable.damage(16, user);
        }
    }
}
