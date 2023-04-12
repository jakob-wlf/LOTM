package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Hiding extends Ability implements Listener {

    private boolean hiding;

    public Hiding(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);

        items.addToSequenceItems(identifier - 1, sequence);

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        p = pathway.getBeyonder().getPlayer();
        p.setInvisible(false);
        p.setInvulnerable(false);

        for(Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(Plugin.instance, pathway.getBeyonder().getPlayer());
        }
    }

    @Override
    public void useAbility() {
        if(hiding)
            return;

        p = pathway.getBeyonder().getPlayer();

        hiding = true;

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(Plugin.instance, p);
        }

        p.setInvulnerable(true);
        p.setInvisible(true);

        new BukkitRunnable() {
            final Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(216, 216, 216), 50f);
            @Override
            public void run() {

                p.spawnParticle(Particle.REDSTONE, p.getLocation(), 500, 6, 6, 6, dust);
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(Plugin.instance, p);
                }

                p.setInvulnerable(true);
                p.setInvisible(true);

                if(!hiding) {
                    cancel();
                    p.setInvisible(false);
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.showPlayer(Plugin.instance, pathway.getBeyonder().getPlayer());
                    }

                    p.setInvulnerable(false);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 8);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getPlayer() != p || !hiding)
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getPlayer() != p || !hiding)
            return;

        for(Ability ability : pathway.getSequence().getAbilities()) {
            if(!(ability instanceof MarionetteControlling controlling))
                continue;
            if(controlling.isControlling())
                return;
        }

        e.setCancelled(true);
    }

    @Override
    public void leftClick() {
        hiding = false;
    }

    @Override
    public void onHold() {
        if(hiding)
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Left-Click to stop hiding"));
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.LIGHT_GRAY_DYE, "Hiding in the Fog of History", "85", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }

    public boolean isHiding() {
        return hiding;
    }
}
