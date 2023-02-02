package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class HolySong extends Ability {

    public HolySong(int identifier, Pathway pathway, Player p) {
        super(identifier, pathway, p);
    }

    @Override
    public void useAbility() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        p.getWorld().playSound(p, Sound.MUSIC_DISC_MELLOHI, 10f, 1f);
        AtomicInteger counter = new AtomicInteger();
        new BukkitRunnable() {
            @Override
            public void run() {
                p.getWorld().spawnParticle(Particle.NOTE, p.getLocation(), 50, 5, 5, 5);
                counter.getAndIncrement();
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 1, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 0, false, false, false));
                if(counter.get() >= 95) {
                    counter.set(0);
                    cancel();
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }
            }
        }.runTaskTimer(Plugin.instance, 1, 20);
    }
}
