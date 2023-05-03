package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class MirrorWorldTraversal extends Ability implements Listener {

    private boolean traversing;
    private GameMode prevGameMode;

    private boolean onCooldown;

    private final Material[] glassMaterials;

    public MirrorWorldTraversal(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        glassMaterials = new Material[] {
                Material.GLASS,
                Material.GLASS_PANE,
                Material.WHITE_STAINED_GLASS,
                Material.WHITE_STAINED_GLASS_PANE,
                Material.ORANGE_STAINED_GLASS,
                Material.ORANGE_STAINED_GLASS_PANE,
                Material.MAGENTA_STAINED_GLASS,
                Material.MAGENTA_STAINED_GLASS_PANE,
                Material.LIGHT_BLUE_STAINED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS,
                Material.YELLOW_STAINED_GLASS_PANE,
                Material.LIME_STAINED_GLASS,
                Material.LIME_STAINED_GLASS_PANE,
                Material.PINK_STAINED_GLASS,
                Material.PINK_STAINED_GLASS_PANE,
                Material.GRAY_STAINED_GLASS,
                Material.GRAY_STAINED_GLASS_PANE,
                Material.LIGHT_GRAY_STAINED_GLASS,
                Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                Material.CYAN_STAINED_GLASS,
                Material.CYAN_STAINED_GLASS_PANE,
                Material.PURPLE_STAINED_GLASS,
                Material.PURPLE_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS,
                Material.BLUE_STAINED_GLASS_PANE,
                Material.BROWN_STAINED_GLASS,
                Material.BROWN_STAINED_GLASS_PANE,
                Material.GREEN_STAINED_GLASS,
                Material.GREEN_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS,
                Material.RED_STAINED_GLASS_PANE,
                Material.BLACK_STAINED_GLASS,
                Material.BLACK_STAINED_GLASS_PANE
        };

        traversing = false;
        onCooldown = false;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if(traversing)
            return;

        if(onCooldown)
            return;

        ArrayList<Block> nearBlocks = Util.getBlocksInSquare(p.getEyeLocation().getBlock(), 4);

        boolean valid = false;
        for(Block block : nearBlocks) {
            if(!Arrays.asList(glassMaterials).contains(block.getType()))
                continue;

            valid = true;
            break;
        }


        if(!valid) {
            p.sendMessage("§cYou have to be near glass blocks");
            return;
        }

        traversing = true;
        prevGameMode = p.getGameMode();

        p.setGameMode(GameMode.SPECTATOR);

        onCooldown = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                onCooldown = false;
            }
        }.runTaskLater(Plugin.instance, 20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        p = pathway.getBeyonder().getPlayer();

        if(!traversing || e.getPlayer() != p)
            return;

        ArrayList<Block> nearBlocks = Util.getBlocksInSquare(p.getEyeLocation().getBlock(), 4);

        boolean valid = false;
        for(Block block : nearBlocks) {
            if(!Arrays.asList(glassMaterials).contains(block.getType()))
                continue;

            valid = true;
            break;
        }

        if(onCooldown)
            return;

        if(!valid) {
            p.sendMessage("§cYou have to be near glass blocks");
            return;
        }

        p.setGameMode(prevGameMode);
        onCooldown = false;
        traversing = false;
    }



    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.DIAMOND, "Mirror World Traversal", "500", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
