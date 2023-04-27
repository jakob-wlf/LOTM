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
        float flySpeed = p.getFlySpeed();

        p.setFlySpeed(Math.min(flySpeed * 2, 1));

        new BukkitRunnable() {
            int counter = 20;
            @Override
            public void run() {
                p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, p.getEyeLocation(), 300, 1.1, 1.1, 1.1, 0);

                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(Plugin.instance, p);
                }

                p.setAllowFlight(true);
                p.setFlying(true);

                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5, 1, false, false, false));
                p.setFireTicks(0);

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1] || pathway.getBeyonder().getSpirituality() <= 220) {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(Plugin.instance, p);
                    }
                    p.setAllowFlight(couldFly);
                    p.setFlySpeed(flySpeed);
                    p.setFireTicks(0);
                    cancel();
                    return;
                }

                counter--;

                if(counter <= 0) {
                    counter = 20;
                    pathway.getSequence().removeSpirituality(420);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.NETHER_STAR, "Conceptualization", "420/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
