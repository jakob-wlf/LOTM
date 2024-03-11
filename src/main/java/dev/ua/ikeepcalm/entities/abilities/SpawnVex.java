package dev.ua.ikeepcalm.entities.abilities;

import dev.ua.ikeepcalm.mystical.MobAbility;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SpawnVex extends MobAbility {

    public SpawnVex(int frequency) {
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
        Random random = new Random();
        for (int i = 0; i < random.nextInt(5) + 1; i++) {
            Location spawnLoc = startLoc.add(random.nextInt(6) - 3, random.nextInt(6) - 3, random.nextInt(6) - 3);
            user.getWorld().spawnEntity(spawnLoc, EntityType.VEX);
        }
    }
}
