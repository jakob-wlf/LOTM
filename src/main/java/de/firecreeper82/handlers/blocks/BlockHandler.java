package de.firecreeper82.handlers.blocks;

import de.firecreeper82.lotm.util.BeyonderItems;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class BlockHandler implements Listener {

    private final ArrayList<CustomBlock> customBlocks;

    public BlockHandler() {
        customBlocks = new ArrayList<>();

        //40
        initBlock(Material.PRISMARINE, BeyonderItems.getStellarAquaCrystal(), 2, "prismarine");
        //80
        initBlock(Material.ROSE_BUSH, BeyonderItems.getRose(), 2, "rose");
    }

    private void initBlock(Material material, ItemStack drop, int rarity, String id) {
        customBlocks.add(new CustomBlock(material, drop, rarity, id));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        for(CustomBlock customBlock : customBlocks) {
            if(e.getBlock().getType() != customBlock.type())
                continue;

            Random random = new Random();
            if(random.nextInt(customBlock.rarity()) == 0) {
                e.setDropItems(false);
                e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), customBlock.drop());
            }
            break;
        }
    }
}
