package de.firecreeper82.listeners;

import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
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

        if((e.getClickedBlock().getMetadata("special").isEmpty()) || e.getClickedBlock().getMetadata("special").get(0).value() == null || !(e.getClickedBlock().getMetadata("special").get(0).value() instanceof Boolean metaDataTag))
            return;

        if(!metaDataTag)
            return;

        e.setCancelled(true);

        Player p = e.getPlayer();
        Inventory inv = createInventory(p);
        p.openInventory(inv);
    }

    private Inventory createInventory(Player p) {

        final int[] invConfig = {
                1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 0, 0, 2, 2, 2, 0, 0, 1,
                1, 0, 0, 2, 2, 2, 0, 0, 1,
                1, 3, 2, 2, 2, 2, 2, 3, 1,
                1, 3, 3, 3, 3, 3, 3, 3, 1,
                1, 1, 1, 1, 3, 1, 1, 1, 1
        };

        HashMap<Integer, ItemStack> itemMap = new HashMap<>();
        itemMap.put(0, new ItemStack(Material.AIR));
        itemMap.put(1, UtilItems.getPurplePane());
        itemMap.put(2, UtilItems.getMagentaPane());
        itemMap.put(3, UtilItems.getWhitePane());

        Inventory inv = Bukkit.createInventory(p, 54, "§5Brew a Potion");
        for(int i = 0; i < 54; i++) {
            inv.setItem(i, itemMap.get(invConfig[i]));
        }
        return inv;
    }
}
