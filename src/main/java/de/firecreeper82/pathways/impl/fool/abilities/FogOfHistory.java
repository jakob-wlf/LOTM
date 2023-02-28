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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FogOfHistory extends Ability implements Listener {

    ArrayList<ItemStack> items;

    public FogOfHistory(int identifier, Pathway pathway) {
        super(identifier, pathway);
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        items = new ArrayList<>();

        items.addAll(Arrays.asList(pathway.getBeyonder().getPlayer().getInventory().getContents()));

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
            items.add(e.getItem().getItemStack().clone());
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

        double pageCount = Math.ceil((float) items.size() / 54);

        if(pageCount == 0)
            return;

        ArrayList<Inventory> pages = new ArrayList<>();
        for(int i = 0; i < pageCount; i++) {
            pages.add(Bukkit.createInventory(p, 54, "§5Fog of History"));
        }

        int counter = 0;
        for(int i = 0; i < pageCount; i++) {
            for(int j = 0; j < 54; j++) {
                if(counter >= items.size())
                    break;
                pages.get(i).setItem(j, items.get(counter));
                counter++;
            }
        }

        p.openInventory(pages.get(0));

    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.QUARTZ, "Fog of History", "100", identifier, 3, pathway.getBeyonder().getPlayer().getName());
    }
}
