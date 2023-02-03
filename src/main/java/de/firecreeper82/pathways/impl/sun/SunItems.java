package de.firecreeper82.pathways.impl.sun;

import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.abilities.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SunItems extends Items {

    public SunItems(Pathway pathway) {
        super(pathway);
        items = new ArrayList<>();
        sequenceItems = new HashMap<>();
        sequenceItems.put(0, 9);
        sequenceItems.put(1, 8);
        sequenceItems.put(2, 8);
        sequenceItems.put(3, 7);
        sequenceItems.put(4, 7);
        sequenceItems.put(5, 7);
        sequenceItems.put(6, 7);
        sequenceItems.put(7, 5);
        sequenceItems.put(8, 5);
        sequenceItems.put(9, 4);
        createItems();
    }

    @Override
    public ArrayList<ItemStack> returnItemsFromSequence(int sequence) {
        ArrayList<ItemStack> itemsForSequence = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : sequenceItems.entrySet()) {
            if(entry.getValue() >= sequence) {
                itemsForSequence.add(items.get(entry.getKey()));
            }
        }
        return itemsForSequence;
    }

    @Override
    public void createItems() {
        //Bard - Holy song
        ItemStack currentItem = new ItemStack(Material.MUSIC_DISC_MELLOHI);
        ItemMeta itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Holy Song");
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §75");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (9)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new HolySong(1, pathway));

        //Light Supplicant - Holy Light
        currentItem = new ItemStack(Material.GLOWSTONE_DUST);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Holy Light");
        itemMeta.addEnchant(Enchantment.CHANNELING, 2, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §715");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (8)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new HolyLight(2, pathway));

        //Light Supplicant - Illuminate
        currentItem = new ItemStack(Material.GOLD_NUGGET);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Illuminate");
        itemMeta.addEnchant(Enchantment.CHANNELING, 3, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §710");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (8)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new Illuminate(3, pathway));

        //Solar High Priest - Fire of Light
        currentItem = new ItemStack(Material.BLAZE_POWDER);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Fire of Light");
        itemMeta.addEnchant(Enchantment.CHANNELING, 4, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §720");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (7)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new FireOfLight(4, pathway));

        //Solar High Priest - Holy Light Summoning
        currentItem = new ItemStack(Material.BLAZE_ROD);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Holy Light Summoning");
        itemMeta.addEnchant(Enchantment.CHANNELING, 5, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §730");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (7)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new HolyLightSummoning(5, pathway));

        //Solar High Priest - Holy Oath
        currentItem = new ItemStack(Material.PAPER);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Holy Oath");
        itemMeta.addEnchant(Enchantment.CHANNELING, 6, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §75/s");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (7)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new HolyOath(6, pathway));

        //Solar High Priest - Cleave of Purification
        currentItem = new ItemStack(Material.HONEYCOMB);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Cleave of Purification");
        itemMeta.addEnchant(Enchantment.CHANNELING, 7, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §720");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (7)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new CleaveOfPurification(7, pathway));

        //Priest of Light - Light of Holiness
        currentItem = new ItemStack(Material.RAW_GOLD);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Light of Holiness");
        itemMeta.addEnchant(Enchantment.CHANNELING, 10, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7100");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (5)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new LightOfHoliness(10, pathway));

        //Priest of Light - Light of Purification
        currentItem = new ItemStack(Material.GLOWSTONE);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Light of Purification");
        itemMeta.addEnchant(Enchantment.CHANNELING, 11, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §750");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (5)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new LightOfPurification(11, pathway));

        //Unshadowed - Spear of Light
        currentItem = new ItemStack(Material.SPECTRAL_ARROW);
        itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Spear of Light");
        itemMeta.addEnchant(Enchantment.CHANNELING, 12, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7200");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (5)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        items.add(currentItem);
        pathway.getSequence().getAbilities().add(new SpearOfLight(12, pathway));
    }
}
