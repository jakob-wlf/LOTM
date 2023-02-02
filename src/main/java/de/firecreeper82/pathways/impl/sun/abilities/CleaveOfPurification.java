package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class CleaveOfPurification extends Ability {
    public CleaveOfPurification(int identifier, Pathway pathway, Player p) {
        super(identifier, pathway, p);
    }

    @Override
    public void useAbility() {
        Location loc = p.getLocation().add(0, 1, 0);
        Vector vector = loc.getDirection();

        for(int i = 0; i < 5; i++) {
            loc.add(vector);

            //Spawn Particles
            if(i == 2) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.5f);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 10, 0.15, 0.15, 0.15, 0);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 80, 0.2, 0.2, 0.2, dust);
            }

            if(i < 2) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 2f);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 10, 0.25, 0.25, 0.25, 0);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 80, 0.3, 0.3, 0.3, dust);
            }

            if(loc.getWorld().getNearbyEntities(loc, 1, 1, 1).isEmpty())
                continue;

            for(Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if(entity.getUniqueId() == pathway.getUuid())
                    continue;
                Location entLoc = entity.getLocation();
                if(entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2));
                    } else {
                        if (livingEntity.getUniqueId() != pathway.getUuid())
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
                    }
                    entLoc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, entLoc, 200, 0.2, 0.2, 0.2, 0.15);
                }
            }
        }
    }
}
