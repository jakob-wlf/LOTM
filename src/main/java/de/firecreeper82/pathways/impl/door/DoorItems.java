package de.firecreeper82.pathways.impl.door;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.abilities.Record;
import de.firecreeper82.pathways.impl.door.abilities.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DoorItems extends Items {

    public DoorItems(Pathway pathway) {
        super(pathway);
        items = new ArrayList<>();

        abilityInfo = new HashMap<>();
        sequenceItems = new HashMap<>();
        initializeAbilityInfos();
        createItems();
    }

    @Override
    public void initializeAbilityInfos() {
        HashMap<Integer, String> names = Objects.requireNonNull(Pathway.getNamesForPathway(pathway.getNameNormalized()));
        String[] s9 = formatAbilityInfo(pathway.getStringColor(), "9: " + names.get(9),
                "§bUse: §7/items §bto get the abilities for your Sequence",
                "§bDoor Opening: §7Lets you pass through solid blocks"
        );
        abilityInfo.put(9, s9);

        String[] s8 = formatAbilityInfo(pathway.getStringColor(), "8: " + names.get(8),
                "§bFog: §7Create fog to hide yourself",
                "§bWind: §7Create Wind to push away entities",
                "§bFlash: §7Create light",
                "§bFreeze: §7Freeze the target for a few seconds",
                "§bElectric Shock: §7Attack your target with a small electric shock"
        );
        abilityInfo.put(8, s8);

        String[] s7 = formatAbilityInfo(pathway.getStringColor(), "7: " + names.get(7),
                "§bDivination: §7Divine the location of entities, biomes or structures"
        );
        abilityInfo.put(7, s7);

        String[] s6 = formatAbilityInfo(pathway.getStringColor(), "6: " + names.get(6),
                "§bRecord: §7Record beyonder abilities from other players",
                "§bRecorded abilities can only be used once",
                "§bThe higher the abilities Sequence is to yours, the less likely it is for the Recording to succed"
        );
        abilityInfo.put(6, s6);

        String[] s5 = formatAbilityInfo(pathway.getStringColor(), "5: " + names.get(5),
                "§bBlink: §7Short distance teleportation",
                "§bTravelers Door: §7Teleport yourself and other entities"
        );
        abilityInfo.put(5, s5);

        String[] s4 = formatAbilityInfo(pathway.getStringColor(), "4: " + names.get(4),
                "§bExile: §7Create multiple overlapping doors to temporarily exile entities within range",
                "§bSpace Concealment: §7Conceal a space. To enter/exit you have to find the corresponding door.",
                "§bYou can see the door and the concealed space while holding the item",
                "§bChange the radius with §7Left-click§b!",
                "§bDimensional Pocket: §7Open a dimensional pocket to store items"
        );
        abilityInfo.put(4, s4);

        String[] s3 = formatAbilityInfo(pathway.getStringColor(), "3: " + names.get(3),
                "§bWandering: §7Wander through the different dimensions",
                "§bConceptualization: §7Turn into symbols to travel the different dimensions",
                "§bConceptualize: §7Attack the enemy using symbols"
        );
        abilityInfo.put(3, s3);

        String[] s2 = formatAbilityInfo(pathway.getStringColor(), "2: " + names.get(2),
                "§bBlack Hole: Create a miniature black hole to suck in blocks and entities",
                "§bSpace Swapping: §7Swap places/Copy a place somewhere else/Move a place somewhere else",
                "§bUse §7Left-click §bto change the selected use case and §7Shift §bto change the radius"
        );
        abilityInfo.put(2, s2);

        String[] s1 = formatAbilityInfo(pathway.getStringColor(), "1: " + names.get(1),
                "§bStarfall: §7Summon multiple star fragments to hit the earth",
                "§bFind Player: §7Teleport to a player"
        );
        abilityInfo.put(1, s1);
    }

    @Override
    public ArrayList<ItemStack> returnItemsFromSequence(int sequence) {
        ArrayList<ItemStack> itemsForSequence = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : sequenceItems.entrySet()) {
            if (entry.getValue() >= sequence) {
                itemsForSequence.add(items.get(entry.getKey()));
            }
        }
        return itemsForSequence;
    }

    @Override
    public void createItems() {
        addAbility(new DoorOpening(1, pathway, 9, this));
        addAbility(new Fog(2, pathway, 8, this));
        addAbility(new Wind(3, pathway, 8, this));
        addAbility(new Flash(4, pathway, 8, this));
        addAbility(new Freezing(5, pathway, 8, this));
        addAbility(new ElectricShock(6, pathway, 8, this));
        addAbility(new Divine(7, pathway, 7, this));
        addAbility(new Record(8, pathway, 6, this));
        addAbility(new Blink(9, pathway, 5, this));
        addAbility(new TravelersDoor(10, pathway, 5, this));
        addAbility(new Exile(11, pathway, 4, this));
        addAbility(new SpaceConcealment(12, pathway, 4, this));
        addAbility(new DimensionalPocket(13, pathway, 4, this));
        addAbility(new Wandering(14, pathway, 3, this));
        addAbility(new Conceptualization(15, pathway, 3, this));
        addAbility(new Conceptualize(16, pathway, 3, this));
        addAbility(new BlackHole(17, pathway, 2, this));
        addAbility(new SpaceSwapping(18, pathway, 2, this));
        addAbility(new StarFall(19, pathway, 1, this));
        addAbility(new FindPlayer(20, pathway, 1, this));
    }

    public void addAbility(Ability ability) {
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());
    }

    public static ItemStack createItem(Material item, String name, String spirituality, int id, int sequence, String player) {
        ItemStack currentItem = new ItemStack(item);
        ItemMeta itemMeta = currentItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§b" + name);
        itemMeta.addEnchant(Enchantment.CHANNELING, id, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7" + spirituality);
        lore.add("§8§l-----------------");
        lore.add("§bDoor - Pathway (" + sequence + ")");
        lore.add("§8" + player);
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}
