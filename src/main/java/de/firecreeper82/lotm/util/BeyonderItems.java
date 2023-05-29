package de.firecreeper82.lotm.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class BeyonderItems {

    private static final List<ItemStack> list = new ArrayList<>();
    private static final UUID characteristicUUID = UUID.fromString("4fba5f2f-cc36-4dc2-9b77-6064bb10788d");

    public static List<ItemStack> returnAllItems() {
        return list;
    }

    public static ItemStack getLavosSquidBlood() {
        final ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta magentaPaneMeta = item.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName("§4Lavos Squid Blood");
        item.setItemMeta(magentaPaneMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getMarlinBlood() {
        final ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta magentaPaneMeta = item.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName("§4Deep Sea Marlin Blood");
        item.setItemMeta(magentaPaneMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getSpiritRemains() {
        final ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Spirit Remains");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getMutatedDoor() {
        final ItemStack item = new ItemStack(Material.IRON_DOOR);
        ItemMeta magentaPaneMeta = item.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName("§bMutated Door");
        item.setItemMeta(magentaPaneMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getSpiritPouch() {
        final ItemStack item = new ItemStack(Material.GHAST_TEAR);
        ItemMeta magentaPaneMeta = item.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName("§bStomach Pouch of Spirit Eater");
        item.setItemMeta(magentaPaneMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getStellarAquaCrystal() {
        final ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§bStellar Aqua Crystal");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getMeteoriteCrystal() {
        final ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§4Meteorite Crystal");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getGoatHorn() {
        final ItemStack item = new ItemStack(Material.GOAT_HORN);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§7Hornacis Gray Mountain Goat Horn");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getRose() {
        final ItemStack item = new ItemStack(Material.ROSE_BUSH);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§4Human Faced Rose");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getSunflower() {
        final ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Crystal Sunflower");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getSirenRock() {
        final ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§9Piece of Siren Rock");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getMagmaHeart() {
        final ItemStack item = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§4Heart of a Magma Titan");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getRoosterComb() {
        final ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Comb of a Dawn Rooster");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getSpiritTreeFruit() {
        final ItemStack item = new ItemStack(Material.GLOW_BERRIES);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Fruit of a Radiance Spirit Pact Tree");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getBirdFeather() {
        final ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Feather of Spirit Pact Bird");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getCrystallizedRoot() {
        final ItemStack item = new ItemStack(Material.HANGING_ROOTS);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Crystallized Root of Tree of Elders");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getWhiteBrillianceRock() {
        final ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6White Brilliance Rock");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getHolyBrillianceRock() {
        final ItemStack item = new ItemStack(Material.QUARTZ);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Holy Brilliance Rock");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getTailFeather() {
        final ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Divine Birds Tail Feather");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }


    public static ItemStack getRedRoosterComb() {
        final ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Red Comb of a Dawn Rooster");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getRoot() {
        final ItemStack item = new ItemStack(Material.HANGING_ROOTS);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Root of a Mist Treant");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getPanther() {
        final ItemStack item = new ItemStack(Material.BLACK_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§8Spinal Fluid of a Black Patterned Panther");
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getPituitaryGland() {
        final ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Mutated Pituitary Gland of a Thousand-Faced Hunter");

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getShadowCharacteristic() {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§8Characteristic of a Human-Skinned Shadow");

        PlayerProfile profile = Bukkit.createPlayerProfile(characteristicUUID);
        PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(new URL("http://textures.minecraft.net/texture/3ccc8a690c89ebf01adf0440c0a3d540e2db89cfc97ad3b8e01810bf3289f67a"));
        } catch (MalformedURLException ignored) {
        }

        itemMeta.setOwnerProfile(profile);

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getWraithDust() {
        final ItemStack item = new ItemStack(Material.GUNPOWDER);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§7Dust of Ancient Wraiths");

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getGargoyleCrystal() {
        final ItemStack item = new ItemStack(Material.CONDUIT);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Core Crystal of Six Winged Gargoyle");

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getBizarroEye() {
        final ItemStack item = new ItemStack(Material.FERMENTED_SPIDER_EYE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Eye of a Bizarro Bane");
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getPlundererBody() {
        final ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Soul Body of a Spirit World Plunderer");
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getWolfEye() {
        final ItemStack item = new ItemStack(Material.SPIDER_EYE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Eye of a Hound of Fulgrim");
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getWolfHeart() {
        final ItemStack item = new ItemStack(Material.COAL);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Transfromed Heart of a Demonic Wolf of Fog");
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }
}
