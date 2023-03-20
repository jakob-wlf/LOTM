package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.UtilItems;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import de.firecreeper82.pathways.impl.fool.abilities.disasters.Disaster;
import de.firecreeper82.pathways.impl.fool.abilities.disasters.Lightning;
import de.firecreeper82.pathways.impl.fool.abilities.disasters.Meteor;
import de.firecreeper82.pathways.impl.fool.abilities.disasters.Tornado;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Arrays;

public class Miracles extends Ability implements Listener {

    private int selected;
    private Category selectedCategory;
    private final Category[] categories;

    private final Inventory[] inventories;

    private final ArrayList<Disaster> disasters;

    public Miracles(int identifier, Pathway pathway) {
        super(identifier, pathway);

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        p = pathway.getBeyonder().getPlayer();

        selected = 0;
        categories = Category.values();
        selectedCategory = categories[selected];

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

        //Check if clicked on disaster and spawn corresponding disaster where player is looking at;
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

        //Natural Disasters Inventory
        Inventory inventory = Bukkit.createInventory(p, 27, "§5§lNatural Disasters");
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, pane);
        }
        inventory.setItem(10, meteor);
        inventory.setItem(13, tornado);
        inventory.setItem(17, lightning);

        inventories[0] = inventory;
    }

    enum Category {
        //Structure("Spawn Structure", 600), <--- Maybe later
        Natural_Disaster("Natural Disasters", 950),
        Summoning("Summon Mob", 250),
        Teleportation("Teleportation", 500),
        Change_Biome("Change the Biome", 400),
        Change_Weather("Change the Weather", 400);

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

        initializeInvs();

        //open corresponding Inventory
        if(inventories[selected] != null)
            p.openInventory(inventories[selected]);
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
        return FoolItems.createItem(Material.NETHER_STAR, "Miracles", "varying", identifier, 2, pathway.getBeyonder().getPlayer().getName());
    }
}
