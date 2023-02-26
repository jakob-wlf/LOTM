package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlameJump extends Ability {

    public FlameJump(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    Block teleportBlock;

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        p.teleport(teleportBlock.getLocation().clone().add(0, 0.5, 0));
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.BLAZE_POWDER, "Flaming Jump", "25", identifier, 7, pathway.getBeyonder().getPlayer().getName());
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }


    @Override
    public void onHold() {
        p = pathway.getBeyonder().getPlayer();

        Vector direction = p.getLocation().getDirection().normalize();
        Location loc = p.getEyeLocation().clone();

        while(!loc.getBlock().getType().isSolid()) {
            loc.add(direction);
        }

        double nearestBlockDistance = -1;
        Block nearestBlock = null;

        List<Block> blocks = getNearbyBlocks(p.getLocation(), 30);

        for(Block b : blocks) {
            if(b.getType() != Material.FIRE)
                continue;
            if(nearestBlockDistance == -1) {
                nearestBlock = b;
                nearestBlockDistance = b.getLocation().distance(loc);
                continue;
            }
            if(nearestBlockDistance > b.getLocation().distance(loc)) {
                nearestBlock = b;
                nearestBlockDistance = b.getLocation().distance(loc);
            }
        }

        if(nearestBlock == null)
            return;

        loc = nearestBlock.getLocation().clone();

        p.spawnParticle(Particle.FLAME, loc.clone().add(0, 0.75, 0), 20, 0.01, 0.01, 0.01, 0.1);
        teleportBlock = nearestBlock;
    }
}
