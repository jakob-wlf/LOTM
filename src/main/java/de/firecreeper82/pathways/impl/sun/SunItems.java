package de.firecreeper82.pathways.impl.sun;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.abilities.*;
import org.bukkit.inventory.ItemStack;

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

    }
}
