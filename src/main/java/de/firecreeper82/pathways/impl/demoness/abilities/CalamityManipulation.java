package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import de.firecreeper82.pathways.impl.disasters.Blizzard;
import de.firecreeper82.pathways.impl.disasters.Earthquake;
import de.firecreeper82.pathways.impl.disasters.Tornado;
import de.firecreeper82.pathways.impl.disasters.Tsunami;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CalamityManipulation extends Ability {

    private Category selectedCategory = Category.BLIZZARD;
    private final Category[] categories = Category.values();
    private int selected = 0;

    public CalamityManipulation(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    enum Category {
        TORNADO("§fTornado"),
        BLIZZARD("§bBlizzard"),
        TSUNAMI("§9Tsunami"),
        EARTHQUAKE("§2Earthquake");

        private final String name;

        Category(String name) {
            this.name = name;
        }
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();

        for (int i = 0; i < 200; i++) {
            if (loc.getBlock().getType().isSolid())
                break;
            loc.add(dir);
        }

        loc.subtract(dir);

        switch (selectedCategory) {
            case TORNADO -> new Tornado(p).spawnDisaster(p, loc);
            case BLIZZARD -> new Blizzard(p).spawnDisaster(p, loc);
            case TSUNAMI -> new Tsunami(p).spawnDisaster(p, loc);
            case EARTHQUAKE -> new Earthquake(p).spawnDisaster(p, loc);
        }
    }

    @Override
    //Cycle through categories on left click
    public void leftClick() {
        selected++;
        if (selected >= categories.length)
            selected = 0;
        selectedCategory = categories[selected];
    }

    @Override
    //Display selected category
    public void onHold() {
        if (p == null)
            p = pathway.getBeyonder().getPlayer();
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Calamity: §f" + selectedCategory.name));
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.WITHER_SKELETON_SKULL, "Calamity Manipulation", "4000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
