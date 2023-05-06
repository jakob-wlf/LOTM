package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.lotm.util.UtilItems;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import de.firecreeper82.pathways.impl.disasters.Disaster;
import de.firecreeper82.pathways.impl.disasters.Lightning;
import de.firecreeper82.pathways.impl.disasters.Meteor;
import de.firecreeper82.pathways.impl.disasters.Tornado;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Miracles extends Ability implements Listener {

    private int selected;
    private Category selectedCategory;
    private final Category[] categories;

    private Chat chat;

    private final Inventory[] inventories;

    private final ArrayList<Disaster> disasters;

    private final ArrayList<Entity> summonedMobs;

    enum Chat {
        MOB,
        BIOME,
        TELEPORT,
        NOTHING,
    }

    public Miracles(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);

        items.addToSequenceItems(identifier - 1, sequence);
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        p = pathway.getBeyonder().getPlayer();

        selected = 0;
        categories = Category.values();
        selectedCategory = categories[selected];

        chat = Chat.NOTHING;

        summonedMobs = new ArrayList<>();

        inventories = new Inventory[categories.length];

        initializeInvs();

        disasters = new ArrayList<>();
        initializeDisasters();
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if(!Arrays.asList(inventories).contains(e.getInventory()))
            return;
        e.setCancelled(true);

        //Check if clicked on disaster and spawn corresponding disaster where player is looking at
        for(Disaster disaster : disasters) {
            if(!disaster.getItem().isSimilar(e.getCurrentItem()))
                continue;

            //Get block player is looking at
            BlockIterator iter = new BlockIterator(p, 100);
            Block lastBlock = iter.next();
            while (iter.hasNext()) {
                lastBlock = iter.next();
                if (lastBlock.getType() == Material.AIR) {
                    continue;
                }
                break;
            }
            Location loc = lastBlock.getLocation();
            disaster.spawnDisaster(p, loc);
            p.closeInventory();
        }

        World world = p.getWorld();
        if(UtilItems.getSunnyWeather().isSimilar(e.getCurrentItem())) {
            p.sendMessage("§6The weather clears up!");
            world.setClearWeatherDuration(120 * 60 * 20);
            p.closeInventory();
        }
        else if(UtilItems.getRainyWeather().isSimilar(e.getCurrentItem())) {
            p.sendMessage("§3It begins to rain!");
            world.setClearWeatherDuration(0);
            world.setStorm(true);
            world.setThunderDuration(120 * 60 * 20);
            p.closeInventory();
        }
        else if(UtilItems.getStormyWeather().isSimilar(e.getCurrentItem())) {
            p.sendMessage("§9A storm is approaching");
            world.setClearWeatherDuration(0);
            world.setStorm(true);
            world.setThundering(true);
            world.setThunderDuration(120 * 60 * 20);
            p.closeInventory();
        }

    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        if(!summonedMobs.contains(e.getEntity()))
            return;

        if(e.getTarget() == pathway.getBeyonder().getPlayer())
            e.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer() != pathway.getBeyonder().getPlayer())
            return;

        if(chat == Chat.NOTHING)
            return;

        e.setCancelled(true);

        //Summoning Mob
        if(chat == Chat.MOB) {
            chat = Chat.NOTHING;
            String chatMsg = e.getMessage();
            EntityType entityType = null;

            for(EntityType type : EntityType.values()) {
                if(type.name().replace("_", " ").equalsIgnoreCase(chatMsg)) {
                    entityType = type;
                    break;
                }
            }

            final EntityType type = entityType;

            if(entityType == null) {
                p.sendMessage("§c" + chatMsg + " is not a valid entity!");
                return;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    //Get block player is looking at
                    BlockIterator iter = new BlockIterator(p, 100);
                    Block lastBlock = iter.next();
                    while (iter.hasNext()) {
                        Block prevBlock = lastBlock;
                        lastBlock = iter.next();
                        if (!lastBlock.getType().isSolid()) {
                            continue;
                        }
                        lastBlock = prevBlock;
                        break;
                    }

                    Location loc = lastBlock.getLocation();
                    World world = loc.getWorld();
                    if(world == null)
                        return;
                    Entity entity = world.spawnEntity(loc, type);
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 2000, 1, 2, 1, 2);

                    Team team = pathway.getBeyonder().getTeam();
                    team.addEntry(entity.getUniqueId().toString());

                    summonedMobs.add(entity);
                }
            }.runTaskLater(Plugin.instance, 0);
        }
        //Teleporting
        else if(chat == Chat.TELEPORT) {
            chat = Chat.NOTHING;
            if(e.getMessage().split(" ").length != 1 && e.getMessage().split(" ").length != 3) {
                p.sendMessage("§cYou need to type in coordinates or a player name");
                chat = Chat.NOTHING;
                return;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(e.getMessage().split(" ").length == 1) {
                        if(Bukkit.getPlayer(e.getMessage()) == null) {
                            p.sendMessage("§c" + e.getMessage() + " is not a valid player");
                            chat = Chat.NOTHING;
                            return;
                        }

                        p.teleport(Objects.requireNonNull(Bukkit.getPlayer(e.getMessage())));
                    }
                    else {
                        for(String msg : e.getMessage().split(" ")) {
                            if(!Util.isInteger(msg)) {
                                p.sendMessage("§cYou need to type in coordinates or a player name");
                                chat = Chat.NOTHING;
                                return;
                            }

                            Location loc = new Location(p.getWorld(), Util.parseInt(e.getMessage().split(" ")[0]), Util.parseInt(e.getMessage().split(" ")[1]), Util.parseInt(e.getMessage().split(" ")[2]));
                            p.teleport(loc);
                        }
                    }
                    chat = Chat.NOTHING;
                }
            }.runTaskLater(Plugin.instance, 0);
        }
        //Change the Biome
        else if(chat == Chat.BIOME) {
            chat = Chat.NOTHING;
            if(e.getMessage().split(" ").length != 1) {
                p.sendMessage("§cYou need to type in the biome");
                chat = Chat.NOTHING;
                return;
            }

            String chatMsg = e.getMessage();
            Biome biome = null;

            for(Biome b : Biome.values()) {
                if(b.name().replace("_", " ").equalsIgnoreCase(chatMsg)) {
                    biome = b;
                    break;
                }
            }

            if(biome == null) {
                p.sendMessage("§c" + chatMsg + " is not a valid biome!");
                return;
            }

            final Biome biomeChange = biome;
            final Location loc = p.getLocation();
            final World world = p.getWorld();

            new BukkitRunnable() {
                @Override
                public void run() {
                    final int radius = 64;
                    for(int i = radius / 2; i > -(radius / 2); i--) {
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                if( (x*x) + (z*z) <= Math.pow(radius, 2)) {
                                    Location tempLoc = new Location(world, (int) loc.getX() + x, (int) loc.getY() + i, (int) loc.getZ() + z);
                                    world.setBiome(tempLoc, biomeChange);
                                    if(!tempLoc.getBlock().getType().isSolid()) {
                                        tempLoc.subtract(0, 1, 0);
                                        if(tempLoc.getBlock().getType().isSolid()) {
                                            tempLoc.add(0, 1, 0);
                                            world.spawnParticle(Particle.SPELL_WITCH, tempLoc, 5, .075, .075, .075, 2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    String biomeName = String.join(" ", (biomeChange.name().substring(0, 1).toUpperCase() + biomeChange.name().substring(1).toLowerCase()).split("_"));
                    p.sendMessage("§5Changed the Biome to " + biomeName);
                }
            }.runTaskLater(Plugin.instance, 0);
        }
    }

    private void initializeDisasters() {
        Meteor meteor = new Meteor(p);
        Tornado tornado = new Tornado(p);
        Lightning lightning = new Lightning(p);

        disasters.add(meteor);
        disasters.add(tornado);
        disasters.add(lightning);
    }

    //Create all the Inventories and put them into the array
    private void initializeInvs() {
        final ItemStack pane = UtilItems.getMagentaPane();
        final ItemStack meteor = UtilItems.getMeteor();
        final ItemStack tornado = UtilItems.getTornado();
        final ItemStack lightning = UtilItems.getLightning();

        final ItemStack sun = UtilItems.getSunnyWeather();
        final ItemStack rain = UtilItems.getRainyWeather();
        final ItemStack storm = UtilItems.getStormyWeather();

        //Natural Disasters Inventory
        Inventory inventoryDisaster = Bukkit.createInventory(p, 27, "§5§lNatural Disasters");
        for (int i = 0; i < inventoryDisaster.getSize(); i++) {
            inventoryDisaster.setItem(i, pane);
        }
        inventoryDisaster.setItem(10, meteor);
        inventoryDisaster.setItem(13, tornado);
        inventoryDisaster.setItem(17, lightning);

        inventories[0] = inventoryDisaster;

        //Weather Inventory
        Inventory inventoryWeather = Bukkit.createInventory(p, 27, "§5§lChange the Weather");
        for (int i = 0; i < inventoryWeather.getSize(); i++) {
            inventoryWeather.setItem(i, pane);
        }
        inventoryWeather.setItem(10, sun);
        inventoryWeather.setItem(13, rain);
        inventoryWeather.setItem(17, storm);

        inventories[4] = inventoryWeather;
    }

    enum Category {
        Natural_Disaster("Natural Disasters", 950),
        Summoning("Summon Mob", 250),
        Teleportation("Teleportation", 500),
        Change_Biome("Change the Biome", 400),
        Change_Weather("Change the Weather", 400);
        //Structure("Spawn Structure", 600);

        private final String name;
        private final int spirituality;

        Category(String name, int spirituality) {
            this.spirituality = spirituality;
            this.name = name;
        }
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().removeSpirituality(selectedCategory.spirituality);

        chat = Chat.NOTHING;

        initializeInvs();

        //open corresponding Inventory
        if(inventories[selected] != null)
            p.openInventory(inventories[selected]);

        switch (selected) {
            case 1 -> {
                chat = Chat.MOB;
                p.sendMessage("§5Which Mob do you want to summon?");
            }
            case 2 -> {
                chat = Chat.TELEPORT;
                p.sendMessage("§5Type the coordinates or the player you want to teleport to!");
            }
            case 3 -> {
                chat = Chat.BIOME;
                p.sendMessage("§5To which biome would you like to switch it to");
            }
            default -> chat = Chat.NOTHING;
        }
    }

    @Override
    //Display selected category
    public void onHold() {
        if(p == null)
            p = pathway.getBeyonder().getPlayer();
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Miracle: §f" + selectedCategory.name));
    }

    @Override
    //Cycle through categories on left click
    public void leftClick() {
        selected++;
        if(selected >= categories.length)
            selected = 0;
        selectedCategory = categories[selected];
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.NETHER_STAR, "Miracles", "varying", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
