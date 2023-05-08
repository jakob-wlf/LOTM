package de.firecreeper82.pathways.impl.emperor;

import de.firecreeper82.pathways.impl.emperor.abilities.Disorder;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.abilities.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EmperorItems extends Items{
    public EmperorItems(Pathway pathway){
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
                "§0Use: §7/items§0 to get the abilities for your Sequence",
                "§0BrainWash: Make the target's strikes against you weaker !");
        abilityInfo.put(9,s9);
        String[] s8 = formatAbilityInfo(pathway.getStringColor(),"8",names.get(8),
                "§0Brute: Problems that cannot be solved by the law will be solved by force.",
                "§0Physical Enhancement: They will receive physical enhancements, strength, and a constitution that breaks the rules.",
                "§0Mental Resistance: The Beyonders at this sequence have a high resistance to psychological influences");
        abilityInfo.put(8,s8);
        String[] s7 = formatAbilityInfo(pathway.getStringColor(),"8",names.get(8),
                "§0Bribe: bribe your target, granting them an array of various effects");
        abilityInfo.put(7,s7);
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
    public void createItems(){
        addAbility(new Bestowment(11,pathway,4,this));
        addAbility(new BrainWash(1, pathway, 9, this));
        addAbility(new Bribe(2,pathway, 7, this));
        addAbility(new Distortion(3,pathway,6, this));
        addAbility(new Disorder(4,pathway,6,this));
        addAbility(new Domineer(6,pathway,5,this));
        addAbility(new EmperorFloat(5,pathway,5,this));
        addAbility(new ExploitFlight(10,pathway,4,this));
        addAbility(new MagnifyDamage(7,pathway,4,this));
        addAbility(new MagnifyDefense(12,pathway,4,this));
        addAbility(new MagnifyReach(8,pathway,4,this));
        addAbility(new MagnifySelf(9,pathway,4,this));
    }

    public void addAbility(Ability ability) {
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());
    }

    public static ItemStack createItem(Material item, String name, String spirituality, int id, int sequence, String player) {
        ItemStack currentItem = new ItemStack(item);
        ItemMeta itemMeta = currentItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§f" + name);
        itemMeta.addEnchant(Enchantment.CHANNELING, id, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7" + spirituality);
        lore.add("§8§l-----------------");
        lore.add("§fBlack Emperor - Pathway (" + sequence + ")");
        lore.add("§8" + player);
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}

