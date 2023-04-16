package de.firecreeper82.pathways.impl.door;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.abilities.*;
import de.firecreeper82.pathways.impl.door.abilities.Record;
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
                "§6Use: §7/items §6to get the abilities for your Sequence",
                    "§6Holy Song: §7Imbue strength and agility through singing a song"
        );
        abilityInfo.put(9, s9);

        String[] s8 = formatAbilityInfo(pathway.getStringColor(), "8: " + names.get(8),
                "§6Holy Light: §7Summon a fiery beam of light from the sky. Especially effective against the undead",
                    "§6Illuminate: §7Summon light to illuminate the darkness"
        );
        abilityInfo.put(8, s8);

        String[] s7 = formatAbilityInfo(pathway.getStringColor(), "7: " + names.get(7),
                "§6Fire of Light: §7Summon a holy fire that burns and damages mobs",
                    "§6Holy Light Summoning: §7An upgraded version of Holy Light",
                    "§6Holy Oath: §7Temporarily strengthen one’s strength and agility",
                    "§6Cleave of Purification: §7 A purifying blow particularly effective against the undead",
                    "§6Immunity against horror and poison effects"
        );
        abilityInfo.put(7, s7);

        String[] s6 = formatAbilityInfo(pathway.getStringColor(), "6: " + names.get(6),
                "§cNo new Abilities yet!"
        );
        abilityInfo.put(6, s6);

        String[] s5 = formatAbilityInfo(pathway.getStringColor(), "5: " + names.get(5),
                "§6Light of Holiness: §7Upgraded version of Holy Light Summoning",
                    "§6Light of Purification: §7Produces a halo-like effect, purifying all undead and foul creatures within a certain range"
        );
        abilityInfo.put(5, s5);

        String[] s4 = formatAbilityInfo(pathway.getStringColor(), "4: " + names.get(4),
                "§6Unshadowed Spear: §7Throw a spear made of pure condensed light that explodes on impact into brilliant light",
                    "§6Flaring Sun: §7Summon a miniature sun to burn all enemies that come near it",
                    "§6Unshadowed Domain: §7Lighten up the entire area and reveal any entity in the area",
                    "§6Armor of Light: §7Summon a Holy-Armour to grant you extra protection"
        );
        abilityInfo.put(4, s4);

        String[] s3 = formatAbilityInfo(pathway.getStringColor(), "3: " + names.get(3),
                "§6Wings of Light: §7Summon Wings made out of Light that allow you to fly"
        );
        abilityInfo.put(3, s3);

        String[] s2 = formatAbilityInfo(pathway.getStringColor(), "2: " + names.get(2),
                "§6Spear of Light: §7An upgraded version of the Unshadowed Spear",
                    "§6Ocean of Light: §7An upgraded version of the Unshadowed Domain. Purify all undead creatures in the area"
        );
        abilityInfo.put(2, s2);

        String[] s1 = formatAbilityInfo(pathway.getStringColor(), "1: " + names.get(1),
                "§6Day and Night: §7Freely fast forward the time to make it night or day"
        );
        abilityInfo.put(1, s1);
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
        addAbility(new DoorOpening(1, pathway, 9, this));
        addAbility(new Fog(2, pathway, 8, this));
        addAbility(new Wind(3, pathway, 8, this));
        addAbility(new Flash(4, pathway, 8, this));
        addAbility(new Freezing(5, pathway, 8, this));
        addAbility(new ElectricShock(6, pathway, 8, this));
        addAbility(new Divine(7, pathway, 7, this));
        addAbility(new Record(8, pathway, 6, this));
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
