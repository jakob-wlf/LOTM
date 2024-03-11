package dev.ua.ikeepcalm.pathways.impl.tyrant;

import dev.ua.ikeepcalm.pathways.Ability;
import dev.ua.ikeepcalm.pathways.Items;
import dev.ua.ikeepcalm.pathways.Pathway;
import dev.ua.ikeepcalm.pathways.impl.tyrant.abilities.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TyrantItems extends Items {

    public TyrantItems(Pathway pathway) {
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
                "§9Physical Enhancements: §7Enhanced Strength and Resistance",
                "§9Night vision"
        );
        abilityInfo.put(9, s9);

        String[] s8 = formatAbilityInfo(pathway.getStringColor(), "8: " + names.get(8),
                "§9Use: §7/items §9to get the abilities for your Sequence",
                "§9Raging Blows: §7Unleash a barrage of blows"
        );
        abilityInfo.put(8, s8);

        String[] s7 = formatAbilityInfo(pathway.getStringColor(), "7: " + names.get(7),
                "§9Water Spells: §7Gain a multitude of small water related spells",
                "§9Switch through spells using §7Left-Click§9."
        );
        abilityInfo.put(7, s7);

        String[] s6 = formatAbilityInfo(pathway.getStringColor(), "6: " + names.get(6),
                "§9Wind Manipulation: §7The ability to use wind in combat by creating wind blades, binding the opponent or flying",
                "§9Switch through spells using §7Left-Click§9."
        );
        abilityInfo.put(6, s6);

        String[] s5 = formatAbilityInfo(pathway.getStringColor(), "5: " + names.get(5),
                "§9Lightning: §7Strike your enemies with powerful lightning bolts",
                "§9Disable the destruction of the Lightning with §7Left-Click§9",
                "§9Siren Song: §7Create various effects by singing",
                "§9Switch through the different songs using §7Left-Click§9.",
                "§9You gain a few new water spells"
        );
        abilityInfo.put(5, s5);

        String[] s4 = formatAbilityInfo(pathway.getStringColor(), "4: " + names.get(4),
                "§9Roar: §7Unleash a roar that has enough power to cut through mountains",
                "§9Tornado: §7Summon a Tornado",
                "§9Tsunami: §7Summon a huge Tsunami"
        );
        abilityInfo.put(4, s4);

        String[] s3 = formatAbilityInfo(pathway.getStringColor(), "3: " + names.get(3),
                "§9Lightning Storm: §7Create a giant lightning storm that can span several hundreds of meters",
                "§9Enable/Disable Destruction of the Lightning Bolts using §7Left-Click§9.",
                "§9When in water, aquatic life will now try to help you fight your enemies"
        );
        abilityInfo.put(3, s3);

        String[] s2 = formatAbilityInfo(pathway.getStringColor(), "2: " + names.get(2),
                "§9Extreme Coldness: §7Create an area of extreme coldness that can freeze everything within seconds"
        );
        abilityInfo.put(2, s2);

        String[] s1 = formatAbilityInfo(pathway.getStringColor(), "1: " + names.get(1),
                "§9Lightning Tornado: §7Create a huge tornado made out of lightning",
                "§9Lightning ball: §7Condense Lightning into a sphere and unleash it upon your enemy"
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
        addAbility(new RagingBlows(1, pathway, 8, this, false));
        addAbility(new WaterSpells(2, pathway, 7, this, false));
        addAbility(new WindManipulation(3, pathway, 6, this, false));
        addAbility(new Lightning(4, pathway, 5, this, false));
        addAbility(new SirenSong(5, pathway, 5, this, false));
        addAbility(new Roar(6, pathway, 4, this, false));
        addAbility(new Tornado(7, pathway, 4, this, false));
        addAbility(new Tsunami(8, pathway, 4, this, false));
        addAbility(new LightningStorm(9, pathway, 3, this, false));
        addAbility(new ExtremeColdness(10, pathway, 2, this, false));
        addAbility(new LightningTornado(11, pathway, 1, this, false));
        addAbility(new LightningBall(12, pathway, 1, this, false));
    }

    public void addAbility(Ability ability) {
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());
    }

    public static ItemStack createItem(Material item, String name, String spirituality, int id, int sequence, String player) {
        ItemStack currentItem = new ItemStack(item);
        ItemMeta itemMeta = currentItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§9" + name);
        itemMeta.addEnchant(Enchantment.CHANNELING, id, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7" + spirituality);
        lore.add("§8§l-----------------");
        lore.add("§9Tyrant - Pathway (" + sequence + ")");
        lore.add("§8" + player);
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}
