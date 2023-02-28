package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;

public class FogOfHistory extends Ability implements Listener {

    ArrayList<ItemStack> items;
    ArrayList<ItemStack> summonedItems;
    ArrayList<Inventory> pages;

    public FogOfHistory(int identifier, Pathway pathway) {
        super(identifier, pathway);
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        items = new ArrayList<>();

        for(ItemStack item : pathway.getBeyonder().getPlayer().getInventory().getContents()) {
            if(item == null)
                continue;
            ItemStack addItem = item.clone();
            addItem.setAmount(addItem.getType().getMaxStackSize());
            addItem = normalizeItem(addItem);
            items.add(addItem);
        }
        summonedItems = new ArrayList<>();
    }

    @EventHandler
    public void onPlayerPickUpItem(EntityPickupItemEvent e) {
        if(e.getEntity() != pathway.getBeyonder().getPlayer())
            return;
        ItemStack itemNormalized = normalizeItem(e.getItem().getItemStack().clone());


        boolean isContained = false;

        items.removeAll(Collections.singleton(null));

        items.removeIf(item -> pathway.getSequence().checkValid(item));


        for(ItemStack item : items) {
            if(item == null)
                continue;
            if(normalizeItem(item.clone()).isSimilar(itemNormalized))
                isContained = true;
        }

        if(!isContained) {
            ItemStack addItem = e.getItem().getItemStack().clone();
            addItem.setAmount(addItem.getType().getMaxStackSize());
            addItem = normalizeItem(addItem);
            items.add(addItem);
        }

    }

    private ItemStack normalizeItem(ItemStack itemNormalized) {
        if(itemNormalized instanceof Damageable) {
            ((Damageable) itemNormalized).setDamage(0);
        }
        return itemNormalized;
    }

    @Override
    public void useAbility() {
        Player p = pathway.getBeyonder().getPlayer();

        items.removeAll(Collections.singleton(null));

        items.removeIf(item -> pathway.getSequence().checkValid(item));

        double pageCount = Math.ceil((float) items.size() / 52);


        if(pageCount == 0)
            return;

        pages = new ArrayList<>();

        for(int i = 0; i < pageCount; i++) {
            pages.add(Bukkit.createInventory(p, 54, "§5Fog of History"));
        }

        int counter = 0;
        for(int i = 0; i < pageCount; i++) {
            for(int j = 0; j < 52; j++) {
                if(counter >= items.size())
                    break;
                pages.get(i).setItem(j, items.get(counter));
                counter++;
            }
        }

        p.openInventory(pages.get(0));

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack checkItem = e.getItemDrop().getItemStack().clone();
        checkItem.setAmount(checkItem.getMaxStackSize());
        if(!summonedItems.contains(checkItem))
            return;

        summonedItems.remove(checkItem);
        e.getItemDrop().getItemStack().setAmount(checkItem.getMaxStackSize() - e.getItemDrop().getItemStack().getMaxStackSize());
        e.getPlayer().getInventory().remove(checkItem);
        e.getItemDrop().remove();
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if(pages == null)
            return;
        if(!pages.contains(e.getInventory()))
            return;

        e.setCancelled(true);
        if(summonedItems.size() >= 3)
            return;
        if(e.getCurrentItem() == null)
            return;
        e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
        ItemStack summonedItem = e.getCurrentItem();
        summonedItems.add(summonedItem);
        new BukkitRunnable() {
            @Override
            public void run() {
                summonedItems.remove(summonedItem);
                if(e.getWhoClicked().getInventory().contains(summonedItem))
                    e.getWhoClicked().getInventory().remove(summonedItem);
            }
        }.runTaskLater(Plugin.instance, 20 * 60 * 3);
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.QUARTZ, "Fog of History", "100", identifier, 3, pathway.getBeyonder().getPlayer().getName());
    }
}
