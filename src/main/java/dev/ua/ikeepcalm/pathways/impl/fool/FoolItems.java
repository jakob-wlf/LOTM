package dev.ua.ikeepcalm.pathways.impl.fool;

import dev.ua.ikeepcalm.pathways.Ability;
import dev.ua.ikeepcalm.pathways.Items;
import dev.ua.ikeepcalm.pathways.Pathway;
import dev.ua.ikeepcalm.pathways.impl.fool.abilities.*;
import dev.ua.ikeepcalm.pathways.impl.fool.abilities.marionetteAbilities.MarionetteControlling;
import dev.ua.ikeepcalm.pathways.impl.fool.abilities.marionetteAbilities.SpiritBodyThreads;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FoolItems extends Items {

    public FoolItems(Pathway pathway) {
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
                "§5Use: §7/items §5to get the abilities for your Sequence",
                "§5Divination: §7Divine the location of entities, biomes or structures"
        );
        abilityInfo.put(9, s9);

        String[] s8 = formatAbilityInfo(pathway.getStringColor(), "8: " + names.get(8),
                "§5Enhanced Attributes: §7Strength, Speed, Jump Height",
                "§5You will no longer take Fall-Damage",
                "§5Paper Throw: §7Right-Click with Paper to throw it"
        );
        abilityInfo.put(8, s8);

        String[] s7 = formatAbilityInfo(pathway.getStringColor(), "7: " + names.get(7),
                "§5Flame Controlling: §7Use up coal to produce flames",
                "§5Air Bullet: §7Shoot a projectile made out of air",
                "§5Air Pipe: §7Breathe underwater",
                "§5Flaming Jump: §7Teleport to nearby flames or fireplaces",
                "§5Paper Figurine Substitute: §7Substitute yourself with a Paper Figurine once you receive damage to avoid it. (Uses up paper)"
        );
        abilityInfo.put(7, s7);

        String[] s6 = formatAbilityInfo(pathway.getStringColor(), "6: " + names.get(6),
                "§5Flame Controlling doesn't consume coal anymore"
        );
        abilityInfo.put(6, s6);

        String[] s5 = formatAbilityInfo(pathway.getStringColor(), "5: " + names.get(5),
                "§5Spirit Body Threads: §7Convert Entities into marionettes",
                "§5Use §7left-click §5to change the target",
                "§5USe §7Shift + Left Click §5to only target players and player characters",
                "§5Marionette Controlling: §7Take full control over a marionette",
                "§5Use §7left-click §5to change selected marionette",
                "§5Use §7Shift + Left Click §5to stop your marionette from following you",
                "§5Marionettes will follow you around and fight for you"
        );
        abilityInfo.put(5, s5);

        String[] s4 = formatAbilityInfo(pathway.getStringColor(), "4: " + names.get(4),
                "§5All abilities enhanced massively",
                    "§5Marionette Controlling now allows you to switch place with the selected marionette by doing §7Shift + Right Click"
        );
        abilityInfo.put(4, s4);

        String[] s3 = formatAbilityInfo(pathway.getStringColor(), "3: " + names.get(3),
                "§5Fog of History: §7Get any item you have ever held out of the Fog of History",
                "§5Hiding in the Fog of History: §7Hide inside the Fog of History to escape pursuers",
                "§5Air Bullet is now adjustable by using §7Left-Click"
        );
        abilityInfo.put(3, s3);

        String[] s2 = formatAbilityInfo(pathway.getStringColor(), "2: " + names.get(2),
                "§5Miracles: §7Create various miracles including natural disaster, summoning mobs and changing the biome",
                "§5Change the selected Miracle using §7Left-Click"
        );
        abilityInfo.put(2, s2);

        String[] s1 = formatAbilityInfo(pathway.getStringColor(), "1: " + names.get(1),
                "§5Grafting: §7Graft together various physical objects and concepts to create various effects.",
                "§5Change the selected use-case using §7Left-Click",
                "§5Realm of Mysteries: §7Create a concealed environment where nothing fromn outside can enter or affect you",
                "§7and nothing inside can leak out (e.g. conversations)",
                "§5Adjust the radius using §7Left-Click"
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
        SpiritBodyThreads spiritBodyThreads = new SpiritBodyThreads(7, pathway, 5, this, false);

        addAbility(new Divine(1, pathway, 9, this));
        addAbility(new FlameControlling(2, pathway, 7, this, false));
        addAbility(new AirBullet(3, pathway, 7, this, false));
        addAbility(new AirPipe(4, pathway, 7, this));
        addAbility(new FlameJump(5, pathway, 7, this));
        addAbility(new PaperSubstitute(6, pathway, 7, this));
        addAbility(spiritBodyThreads);
        addAbility(new MarionetteControlling(8, pathway, 5, this, spiritBodyThreads));
        addAbility(new FogOfHistory(9, pathway, 3, this));
        addAbility(new Hiding(10, pathway, 3, this));
        addAbility(new Miracles(11, pathway, 2, this));
        addAbility(new Grafting(12, pathway, 1, this, false));
        addAbility(new RealmOfMysteries(13, pathway, 1, this));
    }

    public void addAbility(Ability ability) {
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
