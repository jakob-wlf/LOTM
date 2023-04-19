package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Conceptualization extends Ability {

    public Conceptualization(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        boolean couldFly = p.getAllowFlight();
        boolean invulnerable = p.isInvulnerable();

        new BukkitRunnable() {
            @Override
            public void run() {
                p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, p.getEyeLocation(), 300, 1.1, 1.1, 1.1, 0);

                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(Plugin.instance, p);
                }

                p.setAllowFlight(true);
                p.setFlying(true);
                p.setInvulnerable(true);

                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5, 1, false, false, false));
                p.setFireTicks(0);

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(Plugin.instance, p);
                    }
                    p.setAllowFlight(couldFly);
                    p.setInvulnerable(invulnerable);
                    p.setFireTicks(0);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.NETHER_STAR, "Conceptualization", "1100", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}