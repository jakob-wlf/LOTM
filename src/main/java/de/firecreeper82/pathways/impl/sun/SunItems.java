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
        initializeAbilityInfos();
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
        addAbility(new HolySong(1, pathway, 9, this));
        addAbility(new HolyLight(2, pathway, 8, this));
        addAbility(new Illuminate(3, pathway, 8, this));
        addAbility(new FireOfLight(4, pathway, 7, this));
        addAbility(new HolyLightSummoning(5, pathway, 7, this));
        addAbility(new HolyOath(6, pathway, 7, this));
        addAbility(new CleaveOfPurification(7, pathway, 7, this));
        addAbility(new LightOfHoliness(10, pathway, 5, this));
        addAbility(new LightOfPurification(11, pathway, 5, this));
        addAbility(new UnshadowedSpear(12, pathway, 4, this));
        addAbility(new FlaringSun(13, pathway, 4, this));
        addAbility(new UnshadowedDomain(14, pathway, 4, this));
        addAbility(new ArmorOfLight(15, pathway, 4, this));
        addAbility(new WingsOfLight(16, pathway, 3, this));
        addAbility(new SpearOfLight(17, pathway, 2, this));
        addAbility(new OceanOfLight(18, pathway, 2, this));
        addAbility(new DayAndNight(19, pathway, 1, this));
    }

    @Override
    public void initializeAbilityInfos() {

    }

    public void addAbility(Ability ability) {
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());
    }

    public static ItemStack createItem(Material item, String name, String spirituality, int id, int sequence, String player) {
        ItemStack currentItem = new ItemStack(item);
        ItemMeta itemMeta = currentItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6" + name);
        itemMeta.addEnchant(Enchantment.CHANNELING, id, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
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
