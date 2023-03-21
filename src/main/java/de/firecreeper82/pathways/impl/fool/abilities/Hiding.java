package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Hiding extends Ability implements Listener {

    private boolean hiding;
    private GameMode prevGameMode;

    public Hiding(int identifier, Pathway pathway, int sequence) {
        super(identifier, pathway, sequence);

        pathway.getItems().addToSequenceItems(identifier, sequence);

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        p = pathway.getBeyonder().getPlayer();
    }

    @Override
    public void useAbility() {
        if(hiding)
            return;

        p = pathway.getBeyonder().getPlayer();

        prevGameMode = p.getGameMode();
        p.setGameMode(GameMode.SPECTATOR);

        hiding = true;
        p.teleport(p.getLocation().add(0, 1, 0));

        new BukkitRunnable() {
            @Override
            public void run() {

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(216, 216, 216), 50f);
                p.spawnParticle(Particle.REDSTONE, p.getLocation(), 5000, 10, 10, 10, dust);

                if(!hiding) {
                    cancel();
                    p.setGameMode(prevGameMode);
                    p.teleport(p.getLocation().subtract(0, 1, 0));
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 8);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getPlayer() != p || !hiding)
            return;

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
        return FoolItems.createItem(Material.LIGHT_GRAY_DYE, "Hiding in the Fog of History", "85", identifier, 3, pathway.getBeyonder().getPlayer().getName());
    }
}
