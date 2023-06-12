package de.firecreeper82.pathways.impl.demoness;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.abilities.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DemonessItems extends Items {

    public DemonessItems(Pathway pathway) {
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
                "§dPhysical Enhancements: §7Enhanced agility and speed",
                "§dNight vision",
                "§dYou will no longer take fall damage"
        );
        abilityInfo.put(9, s9);

        String[] s8 = formatAbilityInfo(pathway.getStringColor(), "8: " + names.get(8),
                "§cNo new Abilities yet!"
        );
        abilityInfo.put(8, s8);

        String[] s7 = formatAbilityInfo(pathway.getStringColor(), "7: " + names.get(7),
                "§dInvisibility: §7Hide yourself from entities and players",
                "§dBlack Flames: §7Create black flames",
                "§dFrost Magic: §7Freeze entities or blocks",
                "§dCold Wind: §7Create a cold wind"
        );
        abilityInfo.put(7, s7);

        String[] s6 = formatAbilityInfo(pathway.getStringColor(), "6: " + names.get(6),
                "§dThread Manipulation: §7Create Threads and cobwebs to trap your opponents",
                "§dFrost Spear: §7Throw a spear made of ice to attack your enemies"
        );
        abilityInfo.put(6, s6);

        String[] s5 = formatAbilityInfo(pathway.getStringColor(), "5: " + names.get(5),
                "§dEpidemic: §7Spread a disease to kill all entities in your vicinity",
                "§Mirror World Traversal: §7You can travel through the mirror world"
        );
        abilityInfo.put(5, s5);

        String[] s4 = formatAbilityInfo(pathway.getStringColor(), "4: " + names.get(4),
                "§dPestilence: §7Spread the Epidemic over a far greater area and make it more deadly"
        );
        abilityInfo.put(4, s4);

        String[] s3 = formatAbilityInfo(pathway.getStringColor(), "3: " + names.get(3),
                "§dPetrification: §7Petrify entities or blocks"
        );
        abilityInfo.put(3, s3);

        String[] s2 = formatAbilityInfo(pathway.getStringColor(), "2: " + names.get(2),
                "§dCalamity Manipulation: §7Create several disasters like blizzards or tornados",
                "§dSwitch through the calamities using §7Left Click"
        );
        abilityInfo.put(2, s2);

        String[] s1 = formatAbilityInfo(pathway.getStringColor(), "1: " + names.get(1),
                "§dMeteor Shower: §7Summon a meteor shower",
                "§dIce Age: §7Freeze a large area"
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
        addAbility(new Instigate(1, pathway, 8, this));
        addAbility(new Invisibility(2, pathway, 7, this));
        addAbility(new DarkFlames(3, pathway, 7, this, false));
        addAbility(new FrostMagic(4, pathway, 7, this, false));
        addAbility(new ColdWind(5, pathway, 7, this, false));
        addAbility(new FrostSpear(6, pathway, 6, this, false));
        addAbility(new ThreadManipulation(7, pathway, 6, this, false));
        addAbility(new Epidemic(8, pathway, 5, this, false));
        addAbility(new MirrorWorldTraversal(9, pathway, 5, this));
        addAbility(new Pestilence(10, pathway, 4, this, false));
        addAbility(new Petrification(11, pathway, 3, this, false));
        addAbility(new CalamityManipulation(12, pathway, 2, this));
        addAbility(new MeteorShower(13, pathway, 1, this));
        addAbility(new IceAge(14, pathway, 1, this));
    }

    public void addAbility(Ability ability) {
        pathway.getSequence().getAbilities().add(ability);
        items.add(ability.getItem());
    }

    public static ItemStack createItem(Material item, String name, String spirituality, int id, int sequence, String player) {
        ItemStack currentItem = new ItemStack(item);
        ItemMeta itemMeta = currentItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§d" + name);
        itemMeta.addEnchant(Enchantment.CHANNELING, id, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7" + spirituality);
        lore.add("§8§l-----------------");
        lore.add("§dDemoness - Pathway (" + sequence + ")");
        lore.add("§8" + player);
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}
