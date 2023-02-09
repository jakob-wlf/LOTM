package de.firecreeper82.pathways.impl.sun;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.abilities.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SunItems extends Items {

    public SunItems(Pathway pathway) {
        super(pathway);
        items = new ArrayList<>();
        sequenceItems = new HashMap<>();
        sequenceItems.put(0, 9);
        sequenceItems.put(1, 8);
        sequenceItems.put(2, 8);
        sequenceItems.put(3, 7);
        sequenceItems.put(4, 7);
        sequenceItems.put(5, 7);
        sequenceItems.put(6, 7);
        sequenceItems.put(7, 5);
        sequenceItems.put(8, 5);
        sequenceItems.put(9, 4);
        sequenceItems.put(10, 4);
        sequenceItems.put(11, 4);
        sequenceItems.put(12, 4);
        sequenceItems.put(13, 2);
        sequenceItems.put(14, 2);
        sequenceItems.put(15, 2);
        sequenceItems.put(16, 1);
        createItems();
    }

    @Override
    public ArrayList<ItemStack> returnItemsFromSequence(int sequence) {
        ArrayList<ItemStack> itemsForSequence = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : sequenceItems.entrySet()) {
            if(entry.getValue() >= sequence) {
                itemsForSequence.add(items.get(entry.getKey()));
            }
        }
        return itemsForSequence;
    }

    @Override
    public void createItems() {
        //Bard - Holy song
        Ability ability = new HolySong(1, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Light Supplicant - Holy Light
        ability = new HolyLight(2, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Light Supplicant - Illuminate
        ability = new Illuminate(3, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Solar High Priest - Fire of Light
        ability = new FireOfLight(4, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Solar High Priest - Holy Light Summoning
        ability = new HolyLightSummoning(5, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Solar High Priest - Holy Oath
        ability = new HolyOath(6, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Solar High Priest - Cleave of Purification
        ability = new CleaveOfPurification(7, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Priest of Light - Light of Holiness
        ability = new LightOfHoliness(10, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Priest of Light - Light of Purification
        ability = new LightOfPurification(11, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Unshadowed - Unshadowed Spear
        ability = new UnshadowedSpear(12, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Unshadowed - Flaring Sun
        ability = new FlaringSun(13, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Unshadowed - Unshadowed Domain
        ability = new UnshadowedDomain(14, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Unshadowed - Armor of Light
        ability = new ArmorOfLight(15, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Justice Mentor - Wings of Light
        ability = new WingsOfLight(16, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Light Seeker - Spear of Light
        ability = new SpearOfLight(17, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Light Seeker - Ocean of Light
        ability = new OceanOfLight(18, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //White Angel - Day and Night
        ability = new DayAndNight(19, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());
    }

    public static ItemStack createItem(Material item, String name, String spirituality, int id, int sequence, String player) {
        ItemStack currentItem = new ItemStack(item);
        ItemMeta itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6" + name);
        itemMeta.addEnchant(Enchantment.CHANNELING, id, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7" + spirituality);
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (" + sequence + ")");
        lore.add("§8" + player);
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}
