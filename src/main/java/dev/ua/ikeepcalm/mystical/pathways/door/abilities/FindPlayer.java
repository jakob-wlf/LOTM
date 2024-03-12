package dev.ua.ikeepcalm.mystical.pathways.door.abilities;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.Ability;
import dev.ua.ikeepcalm.mystical.parents.Items;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.door.DoorItems;
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
        LordOfTheMinecraft.instance.getServer().getPluginManager().registerEvents(this, LordOfTheMinecraft.instance);
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
        }.runTaskLater(LordOfTheMinecraft.instance, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.RECOVERY_COMPASS, "Find Player", "15000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
