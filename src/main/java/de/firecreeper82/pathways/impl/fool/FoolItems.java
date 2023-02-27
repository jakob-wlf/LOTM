package de.firecreeper82.pathways.impl.fool;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.abilities.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoolItems extends Items {

    public FoolItems(Pathway pathway) {
        super(pathway);
        items = new ArrayList<>();
        sequenceItems = new HashMap<>();
        sequenceItems.put(0, 9);
        sequenceItems.put(1, 7);
        sequenceItems.put(2, 7);
        sequenceItems.put(3, 7);
        sequenceItems.put(4, 7);
        sequenceItems.put(5, 7);
        sequenceItems.put(6, 3);
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
        //Seer - Divination
        Ability ability = new Divine(1, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Magician - Flame Controlling
        ability = new FlameControlling(2, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Magician - Air Bullet
        ability = new AirBullet(3, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Magician - Air Pipe
        ability = new AirPipe(4, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Magician - Flaming Jump
        ability = new FlameJump(5, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Magician - Paper Figurine Substitute
        ability = new PaperSubstitute(6, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());

        //Scholar of Yore - Fog of History
        ability = new FogOfHistory(9, pathway);
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());


    }

    public static ItemStack createItem(Material item, String name, String spirituality, int id, int sequence, String player) {
        ItemStack currentItem = new ItemStack(item);
        ItemMeta itemMeta = currentItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5" + name);
        itemMeta.addEnchant(Enchantment.CHANNELING, id, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7" + spirituality);
        lore.add("§8§l-----------------");
        lore.add("§5Fool - Pathway (" + sequence + ")");
        lore.add("§8" + player);
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}
