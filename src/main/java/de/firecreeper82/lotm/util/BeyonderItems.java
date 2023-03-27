package de.firecreeper82.lotm.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BeyonderItems {

    private static final List<ItemStack> list = new ArrayList<>();

    public static List<ItemStack> returnAllItems() {
        return list;
    }

    public static ItemStack getLavosSquidBlood() {
        final ItemStack magentaPane = new ItemStack(Material.REDSTONE);
        ItemMeta magentaPaneMeta = magentaPane.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName("§4Lavos Squid Blood");
        magentaPane.setItemMeta(magentaPaneMeta);

        list.add(magentaPane);

        return magentaPane;
    }

    public static ItemStack getStellarAquaCrystal() {
        final ItemStack magentaPane = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta magentaPaneMeta = magentaPane.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName("§bStellar Aqua Crystal");
        magentaPane.setItemMeta(magentaPaneMeta);

        list.add(magentaPane);

        return magentaPane;
    }
}
