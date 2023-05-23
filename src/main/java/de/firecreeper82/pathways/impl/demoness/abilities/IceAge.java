package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class IceAge extends Ability {

    public IceAge(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        ArrayList<Block> blocks = Util.getNearbyBlocksInSphere(p.getLocation(), 100, false, true, true);

        for (Block block : blocks) {
            block.setBiome(Biome.ICE_SPIKES);

            if (block.getType() == Material.WATER) {
                block.setType(Material.ICE);
                continue;
            }
            if (block.getType().getHardness() >= 5 || block.getType().getHardness() < 0)
                continue;

            if (block.getType().getHardness() < .2)
                block.setType(Material.AIR);
            if (block.getType().getHardness() < 1)
                block.setType(Material.ICE);
            else if (block.getType().getHardness() < 3)
                block.setType(Material.PACKED_ICE);
            else if (block.getType().getHardness() > 0)
                block.setType(Material.BLUE_ICE);

            Block b = block.getLocation().add(0, 1, 0).getBlock();

            if (!b.getType().isSolid())
                b.setType(Material.SNOW);
        }

        p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getEyeLocation(), 15000, 100, 100, 100, 0);

        for (Entity entity : p.getNearbyEntities(150, 150, 150)) {
            if (!(entity instanceof LivingEntity livingEntity))
                continue;

            livingEntity.damage(25, p);
            livingEntity.setFreezeTicks(20 * 60);
        }
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.BLUE_ICE, "Ice Age", "300000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
