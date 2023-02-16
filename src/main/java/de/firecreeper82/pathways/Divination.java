package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Divination implements Listener {

    private final HashMap<Beyonder, Inventory> openInv;

    private final ItemStack magentaPane;
    private final ItemStack stick;

    private final ItemStack cowHead;

    public Divination() {
        openInv = new HashMap<>();

        magentaPane = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta magentaPaneMeta = magentaPane.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName(" ");
        magentaPaneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        magentaPaneMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        magentaPane.setItemMeta(magentaPaneMeta);


        stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();
        assert stickMeta != null;
        stickMeta.setDisplayName("§5Dowsing Rod Seeking");
        stickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stickMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        stick.setItemMeta(stickMeta);

        cowHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) cowHead.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§6Animals");
        String[] lore = {"§5Divine the location of animals"};
        meta.setLore(Arrays.asList(lore));
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL("https://textures.minecraft.net/texture/c5a9cd58d4c67bccc8fb1f5f756a2d381c9ffac2924b7f4cb71aa9fa13fb5c"));
        }
        catch (MalformedURLException ignored) {}
        meta.setOwnerProfile(profile);
        cowHead.setItemMeta(meta);
    }

    public void divine(Beyonder beyonder) {
        Inventory inv = startInv(createRawInv(beyonder));
        beyonder.getPlayer().openInventory(inv);
        openInv.put(beyonder, inv);
    }

    private Inventory createRawInv(Beyonder beyonder) {
        Inventory inv = Bukkit.createInventory(beyonder.getPlayer(), 27, "§5Divination");
        inv = createRawInv(inv);
        return inv;
    }

    public Inventory createRawInv(Inventory inv) {
        for(int i = 0; i < 27; i++) {
            if(!(i > 9 && i < 17)) {
                inv.setItem(i, magentaPane);
            }
        }
        return inv;
    }

    private Inventory startInv(Inventory inv) {
        inv.setItem(10, stick);
        return inv;
    }

    private Inventory dowsingRodInv(Inventory inv) {
        inv.setItem(10, cowHead);
        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!Plugin.beyonders.containsKey(e.getWhoClicked().getUniqueId()) || !openInv.containsKey(Plugin.beyonders.get(e.getWhoClicked().getUniqueId())) || e.getClickedInventory() != openInv.get(Plugin.beyonders.get(e.getWhoClicked().getUniqueId())))
            return;

        Beyonder beyonder = Plugin.beyonders.get(e.getWhoClicked().getUniqueId());

        if(Objects.equals(e.getCurrentItem(), stick)) {
            Inventory inv = openInv.get(beyonder);
            inv.clear();
            inv = dowsingRodInv(createRawInv(inv));
            beyonder.getPlayer().closeInventory();
            beyonder.getPlayer().openInventory(inv);
            openInv.replace(beyonder, inv);
        }

        if(Objects.equals(e.getCurrentItem(), cowHead)) {
            Inventory inv = Bukkit.createInventory(beyonder.getPlayer(), 54, "§5Divination");
            beyonder.getPlayer().closeInventory();
            beyonder.getPlayer().openInventory(inv);
            openInv.replace(beyonder, inv);
        }
        e.setCancelled(true);
    }
}