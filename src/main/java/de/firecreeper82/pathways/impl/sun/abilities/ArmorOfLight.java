package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ArmorOfLight extends Ability implements Listener {
    public ArmorOfLight(int identifier, Pathway pathway) {
        super(identifier, pathway);
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        ItemStack[] lastItems = new ItemStack[4];
        lastItems[0] = p.getInventory().getHelmet();
        lastItems[1] = p.getInventory().getChestplate();
        lastItems[2] = p.getInventory().getLeggings();
        lastItems[3] = p.getInventory().getBoots();

        p.getInventory().setHelmet(createHelmet());
        p.getInventory().setChestplate(createChestPlate());
        p.getInventory().setLeggings(createLeggings());
        p.getInventory().setBoots(createBoots());

        p.getInventory().addItem(createSword());

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                Location loc = p.getLocation().add(0, 0.5, 0);
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 2f);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 2, 0.3, 0.7, 0.3, 0, dust);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 1, 0.3, 0.7, 0.3, 0);

                if(counter >= 20) {
                    pathway.getSequence().removeSpirituality(100);
                    counter = 0;
                }

                if(pathway.getBeyonder().getSpirituality() <= 100) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }
                counter++;

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    p.getInventory().setHelmet(lastItems[0]);
                    p.getInventory().setChestplate(lastItems[1]);
                    p.getInventory().setLeggings(lastItems[2]);
                    p.getInventory().setBoots(lastItems[3]);

                    p.getInventory().remove(createHelmet());
                    p.getInventory().remove(createChestPlate());
                    p.getInventory().remove(createLeggings());
                    p.getInventory().remove(createBoots());
                    p.getInventory().remove(createSword());
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);

    }

    @Override
    public ItemStack getItem() {
        ItemStack currentItem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Armor of Light");
        itemMeta.addEnchant(Enchantment.CHANNELING, 15, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7100/s");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (4)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }

    public ItemStack createSword() {
        ItemStack item = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§6Sword of Light");
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 5, true);
        itemMeta.addEnchant(Enchantment.SWEEPING_EDGE, 5, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§5A flaming sword made out of");
        lore.add("§5blazing light");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createHelmet() {
        ItemStack item = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§6Helmet of Light");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§3A godly helmet made from shining light");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createChestPlate() {
        ItemStack item = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§6Chestplate of Light");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§3A godly chestplate made from shining light");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createLeggings() {
        ItemStack item = new ItemStack(Material.GOLDEN_LEGGINGS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§6Leggings of Light");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§3Godly leggings made from shining light");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createBoots() {
        ItemStack item = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§6Boots of Light");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§3A godly pair of boots made from shining light");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack() == createHelmet() || e.getItemDrop().getItemStack() == createChestPlate() || e.getItemDrop().getItemStack() == createLeggings() || e.getItemDrop().getItemStack() == createBoots() || e.getItemDrop().getItemStack() == createSword())
            e.setCancelled(true);
    }
}
