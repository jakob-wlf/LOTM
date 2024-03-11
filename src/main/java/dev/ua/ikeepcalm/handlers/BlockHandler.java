package dev.ua.ikeepcalm.handlers;

import dev.ua.ikeepcalm.entities.custom.CustomBlock;
import dev.ua.ikeepcalm.utils.BeyonderItemsUtil;
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

        initBlock(Material.PRISMARINE, BeyonderItemsUtil.getStellarAquaCrystal(), 40, "prismarine");
        initBlock(Material.ROSE_BUSH, BeyonderItemsUtil.getRose(), 80, "rose");
        initBlock(Material.HANGING_ROOTS, BeyonderItemsUtil.getRoot(), 120, "root");
        initBlock(Material.SUNFLOWER, BeyonderItemsUtil.getSunflower(), 60, "sunflower");
        initBlock(Material.SEA_LANTERN, BeyonderItemsUtil.getSirenRock(), 25, "siren-rock");
        initBlock(Material.FLOWERING_AZALEA_LEAVES, BeyonderItemsUtil.getSpiritTreeFruit(), 40, "tree-fruit");
        initBlock(Material.MANGROVE_LOG, BeyonderItemsUtil.getCrystallizedRoot(), 70, "crystallized-root");
        initBlock(Material.AMETHYST_BLOCK, BeyonderItemsUtil.getWhiteBrillianceRock(), 120, "white-rock");
        initBlock(Material.NETHER_QUARTZ_ORE, BeyonderItemsUtil.getHolyBrillianceRock(), 150, "holy-rock");
        initBlock(Material.OBSIDIAN, BeyonderItemsUtil.getMeteoriteCrystal(), 30, "meteorite");
        initBlock(Material.OAK_DOOR, BeyonderItemsUtil.getMutatedDoor(), 40, "door");
    }

    private void initBlock(Material material, ItemStack drop, int rarity, String id) {
        customBlocks.add(new CustomBlock(material, drop, rarity, id));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        for (CustomBlock customBlock : customBlocks) {
            if (e.getBlock().getType() != customBlock.type())
                continue;

            Random random = new Random();
            if (random.nextInt(customBlock.rarity()) == 0) {
                e.setDropItems(false);
                e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), customBlock.drop());
            }
            break;
        }
    }
}
