package de.firecreeper82.lotm.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UtilItems {

    private static final List<ItemStack> list = new ArrayList<>();

    public static List<ItemStack> returnAllItems() {
        return list;
    }

    public static ItemStack getMagentaPane() {
        final ItemStack magentaPane = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta magentaPaneMeta = magentaPane.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName(" ");
        magentaPaneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        magentaPaneMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        magentaPane.setItemMeta(magentaPaneMeta);

        list.add(magentaPane);

        return magentaPane;
    }

    public static ItemStack getMeteor() {
        final ItemStack meteor = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meteorMeta = meteor.getItemMeta();
        assert meteorMeta != null;
        meteorMeta.setDisplayName("§4Summon Meteor");
        meteorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meteorMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        meteor.setItemMeta(meteorMeta);

        list.add(meteor);

        return meteor;
    }

    public static ItemStack getTornado() {
        final ItemStack tornado = new ItemStack(Material.FEATHER);
        ItemMeta tornadoMeta = tornado.getItemMeta();
        assert tornadoMeta != null;
        tornadoMeta.setDisplayName("§fSummon Tornado");
        tornadoMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        tornadoMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        tornado.setItemMeta(tornadoMeta);

        list.add(tornado);

        return tornado;
    }

    public static ItemStack getLightning() {
        final ItemStack lightning = new ItemStack(Material.LIGHTNING_ROD);
        ItemMeta lightningMeta = lightning.getItemMeta();
        assert lightningMeta != null;
        lightningMeta.setDisplayName("§bSummon Lightning");
        lightningMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lightningMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        lightning.setItemMeta(lightningMeta);

        list.add(lightning);

        return lightning;
    }
}
