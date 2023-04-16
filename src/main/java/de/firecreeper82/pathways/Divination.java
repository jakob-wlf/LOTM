package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.Vector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Divination implements Listener {

    private final HashMap<Beyonder, Inventory> openInv;
    private final HashMap<Beyonder, Collection<Entity>> animalDowsing;

    private final ItemStack magentaPane;
    private final ItemStack stick;

    private final ItemStack cowHead;
    private final ItemStack grassHead;
    private final ItemStack playerHead;

    public Divination() {
        openInv = new HashMap<>();
        animalDowsing = new HashMap<>();

        magentaPane = UtilItems.getMagentaPane();
        stick = UtilItems.getDowsingRod();
        cowHead = UtilItems.getCowHead();
        grassHead = UtilItems.getGrassHead();
        playerHead = UtilItems.getDivinationHead();
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
        inv.setItem(11, grassHead);
        inv.setItem(12, playerHead);
        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!Plugin.beyonders.containsKey(e.getWhoClicked().getUniqueId()) || !openInv.containsKey(Plugin.beyonders.get(e.getWhoClicked().getUniqueId())) || e.getClickedInventory() != openInv.get(Plugin.beyonders.get(e.getWhoClicked().getUniqueId())))
            return;

        e.setCancelled(true);

        Beyonder beyonder = Plugin.beyonders.get(e.getWhoClicked().getUniqueId());

        if(Objects.equals(e.getCurrentItem(), stick)) {
            Inventory inv = dowsingRodInv(createRawInv(beyonder));
            beyonder.getPlayer().openInventory(inv);
            openInv.remove(beyonder);
            openInv.put(beyonder, inv);
        }

        if(Objects.equals(e.getCurrentItem(), cowHead)) {
            openInv.remove(beyonder);
            Player p = (Player) e.getWhoClicked();
            p.closeInventory();
            p.sendMessage("§5Write the entity you want to locate");
            animalDowsing.remove(beyonder);
            animalDowsing.put(beyonder, p.getNearbyEntities(1500, 500, 1500));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(!Plugin.beyonders.containsKey(e.getPlayer().getUniqueId()) || !openInv.containsKey(Plugin.beyonders.get(e.getPlayer().getUniqueId())) || e.getInventory() != openInv.get(Plugin.beyonders.get(e.getPlayer().getUniqueId())))
            return;
        openInv.remove(Plugin.beyonders.get(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(!Plugin.beyonders.containsKey(e.getPlayer().getUniqueId()) || !animalDowsing.containsKey(Plugin.beyonders.get(e.getPlayer().getUniqueId())))
            return;

        e.setCancelled(true);
        Player p = e.getPlayer();
        Collection<Entity> nearbyEntities = animalDowsing.get(Plugin.beyonders.get(p.getUniqueId()));

        String chatMsg = e.getMessage();
        EntityType entityType = null;

        if(chatMsg.equalsIgnoreCase("cancel")) {
            p.sendMessage("§cStopping Dowsing Rod Divination");
            animalDowsing.remove(Plugin.beyonders.get(p.getUniqueId()));
            return;
        }

        for(EntityType type : EntityType.values()) {
            if(type.name().equalsIgnoreCase(chatMsg)) {
                entityType = type;
                break;
            }
        }

        if(entityType == null) {
            p.sendMessage("§c" + Util.capitalize(chatMsg) + " is not a valid entity! If you want to cancel the divination, type \"cancel\"");
            return;
        }

        Entity entity = null;
        double distance = -1;
        for(Entity ent : nearbyEntities) {
            if(ent.getType() == entityType) {
                if(ent.getLocation().distance(p.getEyeLocation()) < distance || distance == -1) {
                    distance = ent.getLocation().distance(p.getEyeLocation());
                    entity = ent;
                }
            }
        }

        if(entity == null) {
            p.sendMessage("§cThere is no " + Util.capitalize(entityType.name()) + " nearby!");
            animalDowsing.remove(Plugin.beyonders.get(p.getUniqueId()));
            return;
        }

        animalDowsing.remove(Plugin.beyonders.get(p.getUniqueId()));
        Vector v = entity.getLocation().toVector().subtract(p.getEyeLocation().toVector());

        Location particleLoc = p.getEyeLocation().clone().add(v.normalize().multiply(0.5));

        for(int i = 0; i < 50; i++) {
            p.spawnParticle(Particle.PORTAL, particleLoc, 50, 0.01, 0.0, 0.01, 0);
            particleLoc.add(v.normalize().multiply(0.5));
        }
    }
}