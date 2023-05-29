package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FlameJump extends Ability {

    public FlameJump(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    Block teleportBlock;
    boolean justTeleported = false;

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if (teleportBlock == null)
            return;

        Location loc = teleportBlock.getLocation().clone().add(0.5, 0.5, 0.5);
        loc.setDirection(p.getLocation().getDirection());

        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 1, false, false));
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc.clone().add(0, -0.25, 0), 120, 0.3, 1, 0.3, 0.01);
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(0, -0.25, 0), 85, 0.3, 1, 0.3, 0.015);
        p.teleport(loc);

        justTeleported = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                p.setFireTicks(0);
                justTeleported = false;
            }
        }.runTaskLater(Plugin.instance, 15);
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.BLAZE_POWDER, "Flaming Jump", "25", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
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

        for (int i = 0; i < 60; i++) {
            loc.add(direction);
            if (loc.getBlock().getType().isSolid())
                break;
        }
        double nearestBlockDistance = -1;
        Block nearestBlock = null;

        List<Block> blocks = getNearbyBlocks(p.getLocation(), 60);

        for (Block b : blocks) {
            Material[] validMaterials = {
                    Material.FIRE,
                    Material.SOUL_FIRE,
                    Material.SOUL_CAMPFIRE,
                    Material.CAMPFIRE
            };
            if (!Arrays.asList(validMaterials).contains(b.getType()))
                continue;
            if (nearestBlockDistance == -1) {
                nearestBlock = b;
                nearestBlockDistance = b.getLocation().distance(loc);
                continue;
            }
            if (nearestBlockDistance > b.getLocation().distance(loc)) {
                nearestBlock = b;
                nearestBlockDistance = b.getLocation().distance(loc);
            }
        }

        if (nearestBlock == null) {
            teleportBlock = null;
            return;
        }

        loc = nearestBlock.getLocation().clone();

        if (!justTeleported)
            p.spawnParticle(Particle.FLASH, loc.clone().add(0.5, 0.75, 0.5), 1, 0, 0, 0, 0);
        teleportBlock = nearestBlock;
    }
}
