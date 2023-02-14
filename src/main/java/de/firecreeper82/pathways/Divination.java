package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Divination implements Listener {


    public static void divine(Beyonder beyonder) {
        Inventory inv = createInv(beyonder);
        beyonder.getPlayer().openInventory(inv);
    }

    private static Inventory createInv(Beyonder beyonder) {
        Inventory inv = Bukkit.createInventory(beyonder.getPlayer(), 27);

        ItemStack magentaPane = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta magentaPaneMeta = magentaPane.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName(" ");
        magentaPaneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        magentaPaneMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        magentaPane.setItemMeta(magentaPaneMeta);


        for(int i = 0; i < 27; i++) {
            if(!(i > 9 && i < 17)) {
                inv.setItem(i, magentaPane);
            }
        }
        return inv;
    }
}
