package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import de.firecreeper82.pathways.impl.fool.marionettes.BeyonderMarionette;
import de.firecreeper82.pathways.impl.fool.marionettes.Marionette;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MarionetteManagement extends Ability implements Listener {

    private int currentIndex;
    private static boolean teleportCooldown;

    private boolean beyonderMarionette;

    public MarionetteManagement(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        currentIndex = 0;
        teleportCooldown = false;

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if (!beyonderMarionette && !pathway.getBeyonder().getMarionettes().isEmpty()) {
            Marionette marionette = pathway.getBeyonder().getMarionettes().get(currentIndex);
            if (marionette.isBeingControlled()) {
                p.sendTitle("", "§cYou are currently controlling this marionette", 10, 70, 10);
                return;
            }
            if (marionette.isActive())
                marionette.removeEntity();
            else
                marionette.respawnEntity();
        } else if (beyonderMarionette && !pathway.getBeyonder().getBeyonderMarionettes().isEmpty()) {
            BeyonderMarionette marionette = pathway.getBeyonder().getBeyonderMarionettes().get(currentIndex);

            if (marionette.isActive())
                marionette.removeEntity();
            else
                marionette.respawnEntity();
        }
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.TRIPWIRE_HOOK, "Marionette Management", "None", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void leftClick() {
        if (currentIndex == pathway.getBeyonder().getMarionettes().size() - 1 && !beyonderMarionette) {
            if (!pathway.getBeyonder().getBeyonderMarionettes().isEmpty())
                beyonderMarionette = true;
            currentIndex = 0;
        } else if (beyonderMarionette && currentIndex == pathway.getBeyonder().getBeyonderMarionettes().size() - 1) {
            if (!pathway.getBeyonder().getMarionettes().isEmpty())
                beyonderMarionette = false;
            currentIndex = 0;
        } else
            currentIndex++;
    }

    @Override
    public void onHold() {
        p = pathway.getBeyonder().getPlayer();

        if (pathway.getBeyonder().getMarionettes().isEmpty()) {
            beyonderMarionette = true;
        }

        if (pathway.getBeyonder().getBeyonderMarionettes().isEmpty()) {
            beyonderMarionette = false;
        }

        if (pathway.getBeyonder().getMarionettes().isEmpty() && pathway.getBeyonder().getBeyonderMarionettes().isEmpty()) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cYou don't have any Marionettes!"));
            return;
        }

        if (!beyonderMarionette) {
            while (currentIndex >= pathway.getBeyonder().getMarionettes().size()) {
                currentIndex--;
            }
        } else {
            while (currentIndex >= pathway.getBeyonder().getBeyonderMarionettes().size()) {
                currentIndex--;
            }
        }

        if (!beyonderMarionette) {
            Marionette marionette = pathway.getBeyonder().getMarionettes().get(currentIndex);
            String entityName = Util.capitalize(pathway.getBeyonder().getMarionettes().get(currentIndex).getType().name());
            String status = marionette.isActive() ? "despawn" : "respawn";
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected: §7" + entityName + " §5-- §7Right-Click §5to " + status));
        } else {
            BeyonderMarionette marionette = pathway.getBeyonder().getBeyonderMarionettes().get(currentIndex);
            String entityName = Util.capitalize(pathway.getBeyonder().getBeyonderMarionettes().get(currentIndex).getName());
            String status = marionette.isActive() ? "despawn" : "respawn";
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected: §7" + entityName + " §5-- §7Right-Click §5to " + status));
        }

        if (!pathway.getBeyonder().getMarionettes().isEmpty()) {
            for (Marionette m : pathway.getBeyonder().getMarionettes()) {
                if (!m.isActive())
                    continue;

                Location playerLoc = p.getEyeLocation().clone().subtract(0, .4, 0);
                Location mobLoc = m.getEntity().getLocation().clone().add(0, .5, 0);
                Vector vector = mobLoc.toVector().subtract(playerLoc.toVector()).normalize().multiply(.75);
                World world = p.getWorld();

                int[] colors = new int[]{255, 255, 255};
                if (!beyonderMarionette) {
                    Marionette marionette = pathway.getBeyonder().getMarionettes().get(currentIndex);
                    colors = m == marionette ? new int[]{145, 0, 194} : new int[]{255, 255, 255};
                }

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(colors[0], colors[1], colors[2]), .75f);

                while (playerLoc.distance(mobLoc) > 1.5) {
                    playerLoc.add(vector);
                    world.spawnParticle(Particle.REDSTONE, playerLoc, 2, .025, .025, .025, dust);
                }
            }
        }

        if (pathway.getBeyonder().getBeyonderMarionettes().isEmpty())
            return;
        for (BeyonderMarionette m : pathway.getBeyonder().getBeyonderMarionettes()) {
            if (!m.isActive())
                continue;

            Location playerLoc = p.getEyeLocation().clone().subtract(0, .4, 0);
            Location mobLoc = m.getEntity().getLocation().clone().add(0, .5, 0);
            Vector vector = mobLoc.toVector().subtract(playerLoc.toVector()).normalize().multiply(.75);
            World world = p.getWorld();

            int[] colors = new int[]{255, 255, 255};
            if (beyonderMarionette) {
                BeyonderMarionette marionette1 = pathway.getBeyonder().getBeyonderMarionettes().get(currentIndex);
                colors = m == marionette1 ? new int[]{145, 0, 194} : new int[]{255, 255, 255};
            }

            Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(colors[0], colors[1], colors[2]), .75f);

            while (playerLoc.distance(mobLoc) > 1.5) {
                playerLoc.add(vector);
                world.spawnParticle(Particle.REDSTONE, playerLoc, 2, .025, .025, .025, dust);
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        p = pathway.getBeyonder().getPlayer();

        if (!e.isSneaking() || e.getPlayer() != p || teleportCooldown || !p.getInventory().getItemInMainHand().isSimilar(getItem()) || pathway.getSequence().getCurrentSequence() > 4)
            return;

        if (pathway.getBeyonder().getMarionettes().isEmpty()) {
            p.sendTitle("", "§cYou don't have any Marionettes active", 10, 70, 10);
            return;
        }


        boolean active;
        Mob mob;

        if (!beyonderMarionette) {
            Marionette m = pathway.getBeyonder().getMarionettes().get(currentIndex);
            active = m.isActive();
            mob = m.getEntity();
        } else {
            BeyonderMarionette m = pathway.getBeyonder().getBeyonderMarionettes().get(currentIndex);
            active = m.isActive();
            mob = m.getEntity();
        }

        if (!active) {
            p.sendTitle("", "§cThat Marionette is not active", 10, 70, 10);
            return;
        }

        Location playerLoc = p.getLocation();
        Location mobLoc = mob.getLocation();

        World world = p.getWorld();

        mob.teleport(playerLoc);
        p.teleport(mobLoc);

        world.spawnParticle(Particle.SPELL_WITCH, playerLoc, 250, 1, 2, 1, 2);
        world.spawnParticle(Particle.SPELL_WITCH, mobLoc, 250, 1, 2, 1, 2);

        teleportCooldown = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                teleportCooldown = false;
            }
        }.runTaskLater(Plugin.instance, 30);
    }
}
