package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Flash extends Ability {

    public Flash(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();

        for (int i = 0; i < 20; i++) {
            if (loc.getBlock().getType().isSolid())
                break;
            loc.add(dir);
        }

        loc.subtract(dir);

        if (loc.getWorld() == null)
            return;

        boolean placedLight = false;

        if (loc.getBlock().getType().getHardness() >= 0) {
            placedLight = true;
            loc.getBlock().setType(Material.LIGHT);
        }

        boolean finalPlacedLight = placedLight;
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;

                if (counter <= 17) {
                    loc.getWorld().spawnParticle(Particle.FLASH, loc, 0, 0, 0, 0, 0);
                }

                if (counter >= 20 * 8) {
                    if (finalPlacedLight && loc.getBlock().getType() == Material.LIGHT)
                        loc.getBlock().setType(Material.AIR);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.TORCH, "Flash", "25", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
