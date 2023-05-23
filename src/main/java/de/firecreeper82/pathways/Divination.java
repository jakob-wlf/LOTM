package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class Divination implements Listener {

    private final HashMap<Beyonder, Inventory> openInv;
    private final HashMap<Beyonder, Collection<Entity>> animalDowsing;
    private final ArrayList<Beyonder> dreamDivination;

    private final ItemStack magentaPane;
    private final ItemStack stick;
    private final ItemStack danger;
    private final ItemStack dream;

    private final ItemStack cowHead;
    private final ItemStack grassHead;
    private final ItemStack playerHead;

    public Divination() {
        openInv = new HashMap<>();
        animalDowsing = new HashMap<>();
        dreamDivination = new ArrayList<>();

        magentaPane = UtilItems.getMagentaPane();
        stick = UtilItems.getDowsingRod();
        danger = UtilItems.getDangerPremonition();
        dream = UtilItems.getDreamDivination();

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
        for (int i = 0; i < 27; i++) {
            if (!(i > 9 && i < 17)) {
                inv.setItem(i, magentaPane);
            }
        }
        return inv;
    }

    private Inventory startInv(Inventory inv) {
        inv.setItem(10, magentaPane);
        inv.setItem(11, stick);
        inv.setItem(12, magentaPane);
        inv.setItem(13, danger);
        inv.setItem(14, magentaPane);
        inv.setItem(15, dream);
        inv.setItem(16, magentaPane);
        return inv;
    }

    private Inventory dowsingRodInv(Inventory inv) {
        inv.setItem(10, magentaPane);
        inv.setItem(11, cowHead);
        inv.setItem(12, magentaPane);
        inv.setItem(13, grassHead);
        inv.setItem(14, magentaPane);
        inv.setItem(15, playerHead);
        inv.setItem(16, magentaPane);
        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!Plugin.beyonders.containsKey(e.getWhoClicked().getUniqueId()) || !openInv.containsKey(Plugin.beyonders.get(e.getWhoClicked().getUniqueId())) || e.getClickedInventory() != openInv.get(Plugin.beyonders.get(e.getWhoClicked().getUniqueId())))
            return;

        e.setCancelled(true);

        Beyonder beyonder = Plugin.beyonders.get(e.getWhoClicked().getUniqueId());

        if (Objects.equals(e.getCurrentItem(), stick)) {
            Inventory inv = dowsingRodInv(createRawInv(beyonder));
            beyonder.getPlayer().openInventory(inv);
            openInv.remove(beyonder);
            openInv.put(beyonder, inv);
        }

        if (danger.isSimilar(e.getCurrentItem())) {
            beyonder.getPlayer().closeInventory();

            for (Entity entity : beyonder.getPlayer().getNearbyEntities(50, 50, 50)) {
                if (!(entity instanceof Player) && !(entity instanceof Monster))
                    continue;

                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 1, false, false, false));
            }

            openInv.remove(beyonder);
        }

        if (dream.isSimilar(e.getCurrentItem())) {
            Player p = beyonder.getPlayer();
            p.sendMessage("§5Which Player do you want to divine?");
            p.closeInventory();
            openInv.remove(beyonder);
            remove(beyonder);
            dreamDivination.add(beyonder);
        }

        if (Objects.equals(e.getCurrentItem(), cowHead)) {
            openInv.remove(beyonder);
            Player p = (Player) e.getWhoClicked();
            p.closeInventory();
            p.sendMessage("§5Write the entity you want to locate");
            remove(beyonder);
            animalDowsing.put(beyonder, p.getNearbyEntities(1500, 500, 1500));
        }
    }

    private void remove(Beyonder beyonder) {
        animalDowsing.remove(beyonder);
        dreamDivination.remove(beyonder);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!Plugin.beyonders.containsKey(e.getPlayer().getUniqueId()) || !openInv.containsKey(Plugin.beyonders.get(e.getPlayer().getUniqueId())) || e.getInventory() != openInv.get(Plugin.beyonders.get(e.getPlayer().getUniqueId())))
            return;
        openInv.remove(Plugin.beyonders.get(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onDreamChat(AsyncPlayerChatEvent e) {
        if (!Plugin.beyonders.containsKey(e.getPlayer().getUniqueId()) || !dreamDivination.contains(Plugin.beyonders.get(e.getPlayer().getUniqueId())))
            return;

        e.setCancelled(true);

        e.setCancelled(true);
        Player p = e.getPlayer();

        String chatMsg = e.getMessage();

        Player target = null;

        Beyonder beyonder = null;

        for (Beyonder b : dreamDivination) {
            if (b.getPlayer() == p) {
                beyonder = b;
                break;
            }
        }

        if (beyonder == null) {
            p.sendMessage("§cSomething went wrong");
            return;
        }

        remove(beyonder);

        if (chatMsg.equalsIgnoreCase("cancel")) {
            p.sendMessage("§cStopping Dowsing Rod Divination");
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == p)
                continue;

            if (player.getName().equals(chatMsg)) {
                target = player;
                break;
            }
        }

        if (target == null) {
            p.sendMessage("§cCouldn't find the player " + chatMsg);
            return;
        }

        GameMode prevGameMode = p.getGameMode();
        Location prevLoc = p.getLocation().clone();

        Player finalTarget = target;
        new BukkitRunnable() {
            @Override
            public void run() {
                p.setGameMode(GameMode.SPECTATOR);
                p.setSpectatorTarget(finalTarget);
            }
        }.runTaskLater(Plugin.instance, 0);


        new BukkitRunnable() {
            @Override
            public void run() {
                p.setSpectatorTarget(null);
                p.setGameMode(prevGameMode);
                p.teleport(prevLoc);
            }
        }.runTaskLater(Plugin.instance, 20 * 5);
    }

    @EventHandler
    public void onDowsingChat(AsyncPlayerChatEvent e) {
        if (!Plugin.beyonders.containsKey(e.getPlayer().getUniqueId()) || !animalDowsing.containsKey(Plugin.beyonders.get(e.getPlayer().getUniqueId())))
            return;

        e.setCancelled(true);
        Player p = e.getPlayer();
        Collection<Entity> nearbyEntities = animalDowsing.get(Plugin.beyonders.get(p.getUniqueId()));

        String chatMsg = e.getMessage();
        EntityType entityType = null;

        if (chatMsg.equalsIgnoreCase("cancel")) {
            p.sendMessage("§cStopping Dowsing Rod Divination");
            animalDowsing.remove(Plugin.beyonders.get(p.getUniqueId()));
            return;
        }

        for (EntityType type : EntityType.values()) {
            if (type.name().equalsIgnoreCase(chatMsg)) {
                entityType = type;
                break;
            }
        }

        if (entityType == null) {
            p.sendMessage("§c" + Util.capitalize(chatMsg) + " is not a valid entity! If you want to cancel the divination, type \"cancel\"");
            return;
        }

        Entity entity = null;
        double distance = -1;
        for (Entity ent : nearbyEntities) {
            if (ent.getType() == entityType) {
                if (ent.getLocation().distance(p.getEyeLocation()) < distance || distance == -1) {
                    distance = ent.getLocation().distance(p.getEyeLocation());
                    entity = ent;
                }
            }
        }

        if (entity == null) {
            p.sendMessage("§cThere is no " + Util.capitalize(entityType.name()) + " nearby!");
            animalDowsing.remove(Plugin.beyonders.get(p.getUniqueId()));
            return;
        }

        animalDowsing.remove(Plugin.beyonders.get(p.getUniqueId()));
        Vector v = entity.getLocation().toVector().subtract(p.getEyeLocation().toVector());

        Location particleLoc = p.getEyeLocation().clone().add(v.normalize().multiply(0.5));

        for (int i = 0; i < 50; i++) {
            p.spawnParticle(Particle.PORTAL, particleLoc, 50, 0.01, 0.0, 0.01, 0);
            particleLoc.add(v.normalize().multiply(0.5));
        }
    }
}