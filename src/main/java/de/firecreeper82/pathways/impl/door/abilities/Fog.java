package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Fog extends Ability {

    public Fog(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                p.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, p.getLocation(), 45, 3.5, 3.5, 3.5, 0);

                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10, 1, false, false, false));

                for (Entity entity : p.getNearbyEntities(3.5, 3.5, 3.5)) {
                    if (!(entity instanceof LivingEntity livingEntity))
                        continue;

                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1, false, false, false));
                }

                if (!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.LIGHT_GRAY_DYE, "Fog", "20", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
