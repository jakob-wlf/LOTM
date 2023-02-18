package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        SkullMeta cowMeta = (SkullMeta) cowHead.getItemMeta();
        assert cowMeta != null;
        cowMeta.setDisplayName("§6Entities");
        String[] cowLore = {"§5Divine the location of entities"};
        cowMeta.setLore(Arrays.asList(cowLore));
        PlayerProfile cowProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures cowTextures = cowProfile.getTextures();
        try {
            cowTextures.setSkin(new URL("https://textures.minecraft.net/texture/c5a9cd58d4c67bccc8fb1f5f756a2d381c9ffac2924b7f4cb71aa9fa13fb5c"));
        }
        catch (MalformedURLException ignored) {}
        cowMeta.setOwnerProfile(cowProfile);
        cowHead.setItemMeta(cowMeta);

        grassHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta grassMeta = (SkullMeta) grassHead.getItemMeta();
        assert grassMeta != null;
        grassMeta.setDisplayName("§6Biomes");
        String[] grassLore = {"§5Divine the location of biomes"};
        grassMeta.setLore(Arrays.asList(grassLore));
        PlayerProfile grassProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures grassTextures = grassProfile.getTextures();
        try {
            grassTextures.setSkin(new URL("http://textures.minecraft.net/texture/16bb9fb97ba87cb727cd0ff477f769370bea19ccbfafb581629cd5639f2fec2b"));
        }
        catch (MalformedURLException ignored) {}
        grassMeta.setOwnerProfile(grassProfile);
        grassHead.setItemMeta(grassMeta);

        playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        assert playerMeta != null;
        playerMeta.setDisplayName("§6Biomes");
        String[] playerLore = {"§5Divine the location of biomes"};
        playerMeta.setLore(Arrays.asList(playerLore));
        PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures playerTextures = playerProfile.getTextures();
        try {
            playerTextures.setSkin(new URL("http://textures.minecraft.net/texture/4d9d043adc884b979b4f42bdb350f2a301327cab49c4ce2de42a8f4601fe9dbf"));
        }
        catch (MalformedURLException ignored) {}
        playerMeta.setOwnerProfile(playerProfile);
        playerHead.setItemMeta(playerMeta);
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
            p.sendMessage("§c" + chatMsg + " is not a valid entity! If you want to cancel the divination, type \"cancel\"");
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
            p.sendMessage("§cThere is no " + entityType.name() + " nearby!");
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