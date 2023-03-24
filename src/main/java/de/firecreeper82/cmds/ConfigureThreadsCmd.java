package de.firecreeper82.cmds;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.lotm.util.UtilItems;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.impl.fool.FoolPathway;
import de.firecreeper82.pathways.impl.fool.abilities.SpiritBodyThreads;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;

public class ConfigureThreadsCmd implements CommandExecutor, Listener {

    private final HashMap<Beyonder, Action> actions;

    private enum Action {
        NONE,
        LENGTH,
        EXCLUDE,
        INCLUDE
    }

    public ConfigureThreadsCmd() {
        actions = new HashMap<>();
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    @Override
    //Command for the Player to choose which Spirit Body Threads he can see
    //Only works for Beyonders of the Fool Pathway
    public boolean onCommand(@NonNull CommandSender s, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!(s instanceof Player p)) {
            s.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        if (args.length != 0) {
            s.sendMessage("§cWrong usage: Use /configure-threads");
            return true;
        }

        if (!Plugin.beyonders.containsKey(p.getUniqueId())) {
            s.sendMessage("§cYou have to be a Beyonder to use this command!");
            return true;
        }

        if (!(Plugin.beyonders.get(p.getUniqueId()).getPathway() instanceof FoolPathway)) {
            s.sendMessage("§cYou have to be a Beyonder of the Fool Pathway to use this command!");
            return true;
        }

        Beyonder beyonder = Plugin.beyonders.get(p.getUniqueId());
        Inventory inv = createInv(p);

        p.openInventory(inv);
        actions.put(beyonder, Action.NONE);

        return true;
    }

    private Inventory createInv(Player p) {
        Inventory inv = Bukkit.createInventory(p, 27, "§5Spirit Body Threads");
        for(int i = 0; i < 27; i++) {
            inv.setItem(i, UtilItems.getMagentaPane());
        }

        inv.setItem(10, UtilItems.getThreadLength());
        inv.setItem(13, UtilItems.getExcludeEntities());

        return inv;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(!Plugin.beyonders.containsKey(p.getUniqueId()) || !actions.containsKey(Plugin.beyonders.get(p.getUniqueId())) || actions.get(Plugin.beyonders.get(p.getUniqueId())) == Action.NONE)
            return;

        e.setCancelled(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                Beyonder beyonder = Plugin.beyonders.get(p.getUniqueId());

                if (!(beyonder.getPathway() instanceof FoolPathway) || beyonder.getPathway().getSequence().getCurrentSequence() > 5)
                    return;

                SpiritBodyThreads spiritBodyThreads = null;
                for(Ability a : beyonder.getPathway().getSequence().getAbilities()) {
                    if(a instanceof SpiritBodyThreads) {
                        spiritBodyThreads = (SpiritBodyThreads) a;
                        break;
                    }
                }

                if(spiritBodyThreads == null) {
                    p.sendMessage("§cSomething went wrong");
                    p.sendMessage("§cCancelling length-configuration!");
                    actions.replace(beyonder, Action.NONE);
                    p.openInventory(createInv(p));
                    return;
                }

                if(actions.get(beyonder) == Action.LENGTH) {
                    if(e.getMessage().equalsIgnoreCase("cancel")) {
                        p.sendMessage("§cCancelling length-configuration!");
                        actions.replace(beyonder, Action.NONE);
                        p.openInventory(createInv(p));
                        return;
                    }
                    if(!Util.isInteger(e.getMessage())) {
                        p.sendMessage("§cYou need to put in a number");
                        p.sendMessage("§cType §4cancel §cto cancel");
                        return;
                    }
                    int length = Util.parseInt(e.getMessage());

                    if(length < 0) {
                        p.sendMessage("§cYou need to put in a number greater than 0");
                        p.sendMessage("§cType §4cancel §cto cancel");
                        return;
                    }

                    if(spiritBodyThreads.getMaxDistance() < length) {
                        p.sendMessage("§cThe current maximum length for Spirit Body Threads is " + spiritBodyThreads.getMaxDistance() + " for you!");
                        p.sendMessage("§cType §4cancel §cto cancel");
                        return;
                    }

                    spiritBodyThreads.setPreferredDistance(length);
                    p.sendMessage("§aSet the maximum distance, you can see Spirit Body Threads from to " + length);
                    actions.replace(beyonder, Action.NONE);
                    p.openInventory(createInv(p));

                }
            }
        }.runTaskLater(Plugin.instance, 0);
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player p))
            return;

        if(!Plugin.beyonders.containsKey(p.getUniqueId()))
            return;

        if(UtilItems.getThreadLength().isSimilar(e.getCurrentItem())) {
            e.setCancelled(true);
            Beyonder beyonder = Plugin.beyonders.get(p.getUniqueId());
            if(actions.containsKey(beyonder))
                actions.replace(beyonder, Action.LENGTH);
            else
                actions.put(beyonder, Action.LENGTH);

            p.sendMessage("§5Type in the maximum length of the Spirit Body Threads you want to see", "§5Type §ccancel §5to cancel the process");
            p.closeInventory();
        }
    }
}
