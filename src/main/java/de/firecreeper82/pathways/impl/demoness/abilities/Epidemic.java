package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Epidemic extends Ability {

    public Epidemic(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);

        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        World world = p.getWorld();

        new BukkitRunnable() {
            @Override
            public void run() {
                world.spawnParticle(Particle.SMOKE_NORMAL, p.getEyeLocation(), 500, 40, 40, 40, 0);

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1])
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.GUNPOWDER, "Epidemic", "25/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
