package de.firecreeper82.listeners;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class PotionHandler implements Listener {

    public HashMap<UUID, ItemStack[]> currentRecipe;

    public PotionHandler() {
        currentRecipe = new HashMap<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null || e.getItem() == null || e.getClickedBlock().getLocation().getWorld() == null)
            return;
        if(e.getClickedBlock().getType() != Material.WATER_CAULDRON || e.getClickedBlock().getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.SOUL_FIRE || e.getMaterial().isAir()) {
            return;
        }
        Player p = e.getPlayer();
        e.setCancelled(true);
        if(!currentRecipe.containsKey(p.getUniqueId())) {
            ItemStack[] item = {e.getItem()};
            currentRecipe.put(p.getUniqueId(), item);
        }
        else {
            ItemStack[] items = new ItemStack[currentRecipe.get(p.getUniqueId()).length + 1];
            for(int i = 0; i < currentRecipe.get(p.getUniqueId()).length; i++) {
                items[i] = currentRecipe.get(p.getUniqueId())[i];
            }
            items[currentRecipe.get(p.getUniqueId()).length] = e.getItem();
            currentRecipe.replace(p.getUniqueId(), items);
            if(items.length >= 2) {
                for(Potion potion : Plugin.instance.getPotions()) {
                    for(int i = 1; i < 10; i++) {
                        if(potion.getSequencePotion(i) == null)
                            continue;

                        if(Arrays.equals(currentRecipe.get(p.getUniqueId()), potion.getSequencePotion(i))) {
                            e.getClickedBlock().getLocation().getWorld().dropItem(e.getClickedBlock().getLocation().clone().add(0, 1, 0), potion.returnPotionForSequence(i));
                            e.getPlayer().getInventory().remove(e.getItem());
                            currentRecipe.remove(p.getUniqueId());
                            return;
                        }
                    }
                    for(ItemStack item : currentRecipe.get(p.getUniqueId())) {
                        e.getClickedBlock().getWorld().dropItem(e.getClickedBlock().getLocation().clone().add(0, 1, 0), item);
                    }
                    currentRecipe.remove(p.getUniqueId());
                }
            }
        }

        if(e.getItem() != null)
            e.getPlayer().getInventory().remove(e.getItem());
    }
}
