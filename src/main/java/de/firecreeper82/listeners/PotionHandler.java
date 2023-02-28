package de.firecreeper82.listeners;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Bukkit;
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

        //Checking if the clicked block is a filled cauldron with soul fire underneath
        if(e.getClickedBlock() == null || e.getItem() == null || e.getClickedBlock().getLocation().getWorld() == null)
            return;
        if(e.getClickedBlock().getType() != Material.WATER_CAULDRON || e.getClickedBlock().getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.SOUL_FIRE || e.getMaterial().isAir()) {
            return;
        }
        Player p = e.getPlayer();
        e.setCancelled(true);

        //If Player is not creating a recipe already, add him to the HashMap along with an ItemStack Array
        if(!currentRecipe.containsKey(p.getUniqueId())) {
            ItemStack[] item = {e.getItem()};
            currentRecipe.put(p.getUniqueId(), item);
        }
        //Player is already creating a recipe
        else {
            //Creating an ItemStack[] that is by one larger than the ItemStack[] in the HashMap and adding the ItemStacks
            ItemStack[] items = new ItemStack[currentRecipe.get(p.getUniqueId()).length + 1];
            for(int i = 0; i < currentRecipe.get(p.getUniqueId()).length; i++) {
                items[i] = currentRecipe.get(p.getUniqueId())[i];
            }

            //Adding the clicked Item to the items Array and replacing the player in the HashMap
            items[currentRecipe.get(p.getUniqueId()).length] = e.getItem();
            currentRecipe.replace(p.getUniqueId(), items);

            //Checking if the recipe is a complete recipe for a potion if the Array has 2 ItemStacks
            if(items.length >= 2) {
                for(Potion potion : Plugin.instance.getPotions()) {
                    for(int i = 1; i < 10; i++) {
                        if(potion.getSequencePotion(i) == null) {
                            Bukkit.broadcastMessage("Gegs");
                            continue;
                        }

                        //If Potion recipe exists, drop the potion and remove the Player from the HashMap
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
                }
                currentRecipe.remove(p.getUniqueId());
            }
        }

        if(e.getItem() != null)
            e.getPlayer().getInventory().remove(e.getItem());
    }
}
