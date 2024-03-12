package dev.ua.ikeepcalm.mystical.pathways.door.abilities;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.utils.GeneralPurposeUtil;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.Ability;
import dev.ua.ikeepcalm.mystical.parents.Items;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.door.DoorItems;
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

            int spiritCounter = 1;

            @Override
            public void run() {
                GeneralPurposeUtil.drawParticlesForNearbyPlayers(Particle.ENCHANTMENT_TABLE, p.getEyeLocation(), 50, 1.1, 1.1, 1.1, 0);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(LordOfTheMinecraft.instance, p);
                }

                p.setAllowFlight(true);
                p.setFlying(true);

                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5, 1, false, false, false));
                p.setFireTicks(0);

                if (!pathway.getSequence().getUsesAbilities()[identifier - 1] || pathway.getBeyonder().getSpirituality() <= 220) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(LordOfTheMinecraft.instance, p);
                    }
                    p.setAllowFlight(couldFly);
                    p.setFlySpeed(flySpeed);
                    p.setFireTicks(0);
                    cancel();
                    return;
                }

                counter--;

                if (counter <= 0) {
                    counter = 20;
                    pathway.getSequence().removeSpirituality(250 * spiritCounter);
                    spiritCounter++;
                }
            }
        }.runTaskTimer(LordOfTheMinecraft.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.NETHER_STAR, "Conceptualization", "420/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
