package dev.ua.ikeepcalm.entities.beyonders;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.entities.abilities.PassiveAbilities;
import dev.ua.ikeepcalm.entities.custom.CustomEntity;
import dev.ua.ikeepcalm.utils.MobEffectsUtil;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.MobAbility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class BeyonderMobs {

    public void addMob(Entity entity, CustomEntity customEntity) {
        Random random = new Random();
        if (!customEntity.repeatingParticles())
            MobEffectsUtil.playParticleEffect(entity.getLocation(), customEntity.particle(), entity);

        PassiveAbilities.passiveAbility(customEntity.id(), entity);
        new BukkitRunnable() {
            Player target;

            @Override
            public void run() {
                if (!entity.isValid()) {
                    cancel();
                    return;
                }

                //Playing the particle effect
                if (customEntity.repeatingParticles())
                    MobEffectsUtil.playParticleEffect(entity.getLocation(), customEntity.particle(), entity);

                //Setting the target for the entity
                if (target == null && entity.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if (target == null) {
                    for (Entity e : entity.getNearbyEntities(40, 40, 40)) {
                        if (!(e instanceof Player))
                            continue;
                        target = (Player) e;
                    }
                }

                for (ArrayList<Entity> list : LordOfTheMinecraft.instance.getConcealedEntities()) {
                    if (list.contains(target)) {
                        target = null;
                        return;
                    }
                }

                if (target == null)
                    return;

                if (entity instanceof Mob mob)
                    mob.setTarget(target);

                //Use abilities
                for (MobAbility ability : customEntity.abilities()) {
                    if (random.nextInt(ability.getFrequency()) != 0)
                        continue;

                    ability.useAbility(entity.getLocation(), target.getLocation(), 1, entity, target);
                }
            }
        }.runTaskTimer(LordOfTheMinecraft.instance, 0, 0);
    }
}
