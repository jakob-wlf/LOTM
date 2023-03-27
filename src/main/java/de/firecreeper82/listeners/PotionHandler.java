package de.firecreeper82.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PotionHandler implements Listener {

    public HashMap<UUID, ItemStack[]> currentRecipe;

    public PotionHandler() {
        currentRecipe = new HashMap<>();
    }

    /**
    This is just temporary and will definitely be reworked
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null)
            return;
        if(e.getClickedBlock().getType() != Material.CAULDRON)
            return;

        if((e.getClickedBlock().getMetadata("special").isEmpty()) || !(e.getClickedBlock().getMetadata("special").get(0).value() instanceof Boolean))
            return;

        if((Boolean) e.getClickedBlock().getMetadata("special").get(0).value()) {
            e.getPlayer().sendMessage("Test");
        }
    }
}
