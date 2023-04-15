package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;


public class HolySong extends Ability {

    public HolySong(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        p.getWorld().playSound(p, Sound.MUSIC_DISC_MELLOHI, 10f, 1f);
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                p.getWorld().spawnParticle(Particle.NOTE, p.getLocation(), 50, 5, 5, 5);
                counter++;
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 0, false, false, false));
                if(counter >= 95) {
                    counter = 0;
                    cancel();
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }
            }
        }.runTaskTimer(Plugin.instance, 1, 20);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.MUSIC_DISC_PIGSTEP, "Holy Song", "30", identifier, 9, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
