package dev.ua.ikeepcalm.pathways.impl.demoness.abilities;

import dev.ua.ikeepcalm.Plugin;
import dev.ua.ikeepcalm.pathways.Ability;
import dev.ua.ikeepcalm.pathways.Items;
import dev.ua.ikeepcalm.pathways.Pathway;
import dev.ua.ikeepcalm.pathways.impl.demoness.DemonessItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Invisibility extends Ability {

    public Invisibility(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                p.setInvisible(true);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(Plugin.instance, p);
                }

                if (pathway == null || pathway.getSequence() == null) {
                    cancel();
                    return;
                }

                if (pathway.getBeyonder().getSpirituality() <= 8)
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

                counter++;

                if (counter >= 20) {
                    counter = 0;
                    pathway.getSequence().removeSpirituality(8);
                }

                if (!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(Plugin.instance, p);
                    }
                    cancel();
                    p.setInvisible(false);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.GHAST_TEAR, "Invisibility", "8/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
