package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FindPlayer extends Ability implements Listener {

    private static boolean finding;

    public FindPlayer(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);

        items.addToSequenceItems(identifier - 1, sequence);
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        finding = false;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if (finding) {
            finding = false;
            p.sendMessage("§cCancelling");
            return;
        }

        p.sendMessage("§bType in the player you want to teleport to.");
        p.sendMessage("§bClick again to cancel");

        finding = true;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        p = pathway.getBeyonder().getPlayer();

        if (!finding || e.getPlayer() != p)
            return;

        e.setCancelled(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                Player tp = null;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!e.getMessage().equalsIgnoreCase(player.getName()))
                        continue;
                    tp = player;
                    break;
                }

                finding = false;


                if (tp == null) {
                    p.sendMessage("§cCouldn't find the player " + e.getMessage());
                    p.sendMessage("§cCancelling");
                    return;
                }

                p.teleport(tp);
            }
        }.runTaskLater(Plugin.instance, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.RECOVERY_COMPASS, "Find Player", "15000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
