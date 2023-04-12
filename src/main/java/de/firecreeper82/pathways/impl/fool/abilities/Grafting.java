package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import de.firecreeper82.pathways.impl.fool.abilities.grafting.BlockToEntity;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Grafting extends Ability implements Listener {

    private final HashMap<Location[], Integer> graftedLocations;
    private final ArrayList<Entity> teleportedPlayers;

    private int radius = 1;

    public Grafting(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        graftedLocations = new HashMap<>();
        teleportedPlayers = new ArrayList<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<Location[], Integer> entry : graftedLocations.entrySet()) {
                    if(entry.getKey()[0].getWorld() == null || entry.getKey()[1].getWorld() == null)
                        return;

                    for(Entity entity : entry.getKey()[0].getWorld().getNearbyEntities(entry.getKey()[0], entry.getValue(), entry.getValue(), entry.getValue())) {
                        if(teleportedPlayers.contains(entity))
                            continue;

                        entity.teleport(entry.getKey()[1]);
                        teleportedPlayers.add(entity);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                teleportedPlayers.remove(entity);
                            }
                        }.runTaskLater(Plugin.instance, 30);
                    }

                    for(Entity entity : entry.getKey()[1].getWorld().getNearbyEntities(entry.getKey()[1], radius, 1, radius)) {
                        if(teleportedPlayers.contains(entity))
                            continue;

                        entity.teleport(entry.getKey()[0]);
                        teleportedPlayers.add(entity);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                teleportedPlayers.remove(entity);
                            }
                        }.runTaskLater(Plugin.instance, 50);
                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    enum Category {
        Location("Location - Location"),
        Block("Entity - Block"),
        Entity("Entity - Entity"),
        STUCK("Entity - Location");

        private final String name;

        Category(String name) {
            this.name = name;
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if(!e.isSneaking() || e.getPlayer() != p || !p.getInventory().getItemInMainHand().isSimilar(getItem()))
            return;

        radius++;
        if(radius > 6)
            radius = 1;

        p.sendMessage("§5Set the radius to " + radius);
    }

    private Category selectedCategory = Category.Location;
    private final Category[] categories = Category.values();
    private boolean grafting = false;
    private int selected = 0;

    private Location loc1;
    private Location loc2;

    private Material graftMaterial;
    private Entity blockToEntity;

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();



        switch (selectedCategory) {
            case Location -> {
                //Get block player is looking at
                Location playerLook = p.getEyeLocation();
                Vector vector = playerLook.getDirection().normalize().multiply(.5);
                for(int i = 0; i < 500; i++) {
                    if(playerLook.getBlock().getType().isSolid())
                        break;
                    playerLook.add(vector);
                }

                playerLook.add(0, .5, 0);

                if(!grafting) {
                    loc1 = playerLook;
                }
                else {
                    loc2 = playerLook;

                    graftedLocations.put(new Location[]{loc1, loc2}, radius);
                }

                p.spawnParticle(Particle.SPELL_WITCH, playerLook, 400, radius / 2f, .1525,radius / 2f, 0);

                grafting = !grafting;
            }
            case Block -> {
                Location playerLook = p.getEyeLocation();
                Vector vector = playerLook.getDirection().normalize().multiply(.5);
                if(!grafting) {
                    //Get entity player is looking at
                    Entity entity = null;

                    for(int i = 0; i < 500; i++) {
                        playerLook.add(vector);

                        if(p.getWorld().getNearbyEntities(playerLook, 1, 1, 1).isEmpty())
                            continue;

                        Entity e = p.getWorld().getNearbyEntities(playerLook, 1, 1, 1).iterator().next();

                        if(e == p)
                            continue;

                        entity = e;
                    }

                    if(entity == null) {
                        p.sendMessage("§cCouldn't find the entity");
                        return;
                    }

                    p.spawnParticle(Particle.SPELL_WITCH, entity.getLocation(), 50, .5, .5, .5, 0);
                    blockToEntity = entity;
                }
                else {
                    //Get block player is looking at
                    for(int i = 0; i < 500; i++) {
                        if(playerLook.getBlock().getType().isSolid())
                            break;
                        playerLook.add(vector);
                    }

                    graftMaterial = playerLook.getBlock().getType();
                    p.spawnParticle(Particle.SPELL_WITCH, playerLook, 80, .25, .25, .25, 0);

                    new BlockToEntity(blockToEntity, graftMaterial, p);
                    reset();
                }
                grafting = !grafting;
            }
        }
    }

    @Override
    //Display selected category
    public void onHold() {
        if(p == null)
            p = pathway.getBeyonder().getPlayer();
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Use-case: §f" + selectedCategory.name));

        for(Map.Entry<Location[], Integer> entry : graftedLocations.entrySet()) {
            if (entry.getKey()[0].getWorld() == null || entry.getKey()[1].getWorld() == null)
                return;

            p.spawnParticle(Particle.SPELL_WITCH, entry.getKey()[0], 75, entry.getValue() / 2f, .15,entry.getValue() / 2f, 0);
            p.spawnParticle(Particle.SPELL_WITCH, entry.getKey()[1], 75, entry.getValue() / 2f, .15,entry.getValue() / 2f, 0);
        }
    }

    @Override
    //Cycle through categories on left click
    public void leftClick() {
        grafting = false;
        reset();
        selected++;
        if(selected >= categories.length)
            selected = 0;
        selectedCategory = categories[selected];
    }

    private void reset() {
        loc1 = null;
        loc2 = null;

        graftMaterial = null;
        blockToEntity = null;

        radius = 1;
    }


    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.ECHO_SHARD, "Grafting", "None", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
