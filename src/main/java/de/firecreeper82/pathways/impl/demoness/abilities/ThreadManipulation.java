package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ThreadManipulation extends NPCAbility {

    public ThreadManipulation(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        placeThreads(true, caster, loc);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        placeThreads(false, null, null);
    }

    private void placeThreads(boolean npc, Entity e, Location target) {
        Entity caster = npc ? e : p;

        Location loc = npc ? target : p.getEyeLocation();

        if(!npc) {
            Vector dir = p.getEyeLocation().getDirection().normalize();
            World world = loc.getWorld();

            if (world == null)
                return;

            for (int i = 0; i < 30; i++) {
                loc.add(dir);

                if (loc.getBlock().getType().isSolid())
                    break;

                if (!world.getNearbyEntities(loc, 1.2, 1.2, 1.2).isEmpty() && !world.getNearbyEntities(loc, 1.2, 1.2, 1.2).contains(p))
                    break;
            }
        }

        Random random = new Random();

        ArrayList<Block> blocks = Util.getNearbyBlocksInSphere(loc, 7, false, false, true);

        HashMap<Block, Material> materials = new HashMap<>();

        for (Block block : blocks) {
            if (block.getType().isSolid())
                continue;

            if (random.nextInt(3) != 0)
                continue;

            materials.put(block, block.getType());
            block.setType(Material.COBWEB);

            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(materials.get(block));
                }
            }.runTaskLater(Plugin.instance, 20 * 20);
        }
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.COBWEB, "Thread Manipulation", "40", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
