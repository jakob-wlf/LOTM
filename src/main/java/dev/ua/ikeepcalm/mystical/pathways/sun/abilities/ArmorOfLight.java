package dev.ua.ikeepcalm.mystical.pathways.sun.abilities;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.Ability;
import dev.ua.ikeepcalm.mystical.parents.Items;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.sun.SunItems;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class ArmorOfLight extends Ability implements Listener {
    public final String playerName;
    public boolean removeOnRejoin;

    final ItemStack[] lastItems;

    final ItemStack helmet;
    final ItemStack chest;
    final ItemStack leggings;
    final ItemStack boots;
    final ItemStack sword;

    boolean dead;

    public ArmorOfLight(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        LordOfTheMinecraft.instance.getServer().getPluginManager().registerEvents(this, LordOfTheMinecraft.instance);

        playerName = pathway.getBeyonder().getPlayer().getName();
        removeOnRejoin = false;
        dead = false;
        lastItems = new ItemStack[4];

        helmet = createHelmet();
        chest = createChestPlate();
        leggings = createLeggings();
        boots = createBoots();
        sword = createSword();

        p = pathway.getBeyonder().getPlayer();
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

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
                Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc, 2, 0.3, 0.7, 0.3, 0, dust);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 1, 0.3, 0.7, 0.3, 0);

                if (counter >= 20) {
                    pathway.getSequence().removeSpirituality(100);
                    counter = 0;
                }

                if (pathway.getBeyonder().getSpirituality() <= 100) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }
                counter++;

                if (!pathway.getSequence().getUsesAbilities()[identifier - 1] || dead) {
                    p.getInventory().setHelmet(lastItems[0]);
                    p.getInventory().setChestplate(lastItems[1]);
                    p.getInventory().setLeggings(lastItems[2]);
                    p.getInventory().setBoots(lastItems[3]);

                    p.getInventory().remove(helmet);
                    p.getInventory().remove(chest);
                    p.getInventory().remove(leggings);
                    p.getInventory().remove(boots);
                    p.getInventory().remove(sword);
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    dead = false;
                    cancel();
                }

                if (!pathway.getBeyonder().online) {
                    removeOnRejoin = true;
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }
            }
        }.runTaskTimer(LordOfTheMinecraft.instance, 0, 1);

    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.TOTEM_OF_UNDYING, "Світлова Броня", "100/сек", identifier, 4, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }

    public ItemStack createSword() {
        ItemStack item = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Світловий меч");
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 5, true);
        itemMeta.addEnchant(Enchantment.SWEEPING_EDGE, 5, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Полум'яний меч, зроблений з");
        lore.add("§5сяючого світла");
        lore.add("§8" + playerName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createHelmet() {
        ItemStack item = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Світловий шолом");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§3божественний шолом, зроблений з сяючого світла");
        lore.add("§8" + playerName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createChestPlate() {
        ItemStack item = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Світловий нагрудник");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§3божественний нагрудник, зроблений з сяючого світла");
        lore.add("§8" + playerName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createLeggings() {
        ItemStack item = new ItemStack(Material.GOLDEN_LEGGINGS);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Світлові штани");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§3Божественні штани, зроблені з сяючого світла");
        lore.add("§8" + playerName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createBoots() {
        ItemStack item = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Світлові чоботи");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15, true);
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§3Божественна пара чобіт, зроблена з сяючого світла");
        lore.add("§8" + playerName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (Objects.requireNonNull(e.getItemDrop().getItemStack().getItemMeta()).getDisplayName().equals(Objects.requireNonNull(helmet.getItemMeta()).getDisplayName()) || e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(Objects.requireNonNull(helmet.getItemMeta()).getDisplayName()) || e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(Objects.requireNonNull(helmet.getItemMeta()).getDisplayName()) || e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(Objects.requireNonNull(helmet.getItemMeta()).getDisplayName()) || e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(Objects.requireNonNull(helmet.getItemMeta()).getDisplayName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        p = pathway.getBeyonder().getPlayer();
        if (!e.getPlayer().getUniqueId().equals(p.getUniqueId()))
            return;
        if (!removeOnRejoin)
            return;

        removeOnRejoin = false;
        e.getPlayer().getInventory().setHelmet(lastItems[0]);
        e.getPlayer().getInventory().setChestplate(lastItems[1]);
        e.getPlayer().getInventory().setLeggings(lastItems[2]);
        e.getPlayer().getInventory().setBoots(lastItems[3]);

        e.getPlayer().getInventory().remove(helmet);
        e.getPlayer().getInventory().remove(chest);
        e.getPlayer().getInventory().remove(leggings);
        e.getPlayer().getInventory().remove(boots);
        e.getPlayer().getInventory().remove(sword);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        p = pathway.getBeyonder().getPlayer();
        if (!e.getEntity().getUniqueId().equals(p.getUniqueId()))
            return;
        dead = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                dead = false;
            }
        }.runTaskLater(LordOfTheMinecraft.instance, 10);
    }
}
