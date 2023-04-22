package de.firecreeper82.lotm.util;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilItems {

    private static final List<ItemStack> list = new ArrayList<>();

    public static List<ItemStack> returnAllItems() {
        return list;
    }

    public static ItemStack getMagentaPane() {
        final ItemStack magentaPane = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta magentaPaneMeta = magentaPane.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName(" ");
        magentaPaneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        magentaPaneMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        magentaPane.setItemMeta(magentaPaneMeta);

        list.add(magentaPane);

        return magentaPane;
    }

    public static ItemStack getCauldron() {
        final ItemStack item = new ItemStack(Material.CAULDRON );
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Brewing Cauldron");
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getPurplePane() {
        final ItemStack magentaPane = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta magentaPaneMeta = magentaPane.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName(" ");
        magentaPaneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        magentaPaneMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        magentaPane.setItemMeta(magentaPaneMeta);

        list.add(magentaPane);

        return magentaPane;
    }

    public static ItemStack getWhitePane() {
        final ItemStack magentaPane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta magentaPaneMeta = magentaPane.getItemMeta();
        assert magentaPaneMeta != null;
        magentaPaneMeta.setDisplayName(" ");
        magentaPaneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        magentaPaneMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        magentaPane.setItemMeta(magentaPaneMeta);

        list.add(magentaPane);

        return magentaPane;
    }

    public static ItemStack getDowsingRod() {
        final ItemStack dowsingStick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = dowsingStick.getItemMeta();
        assert stickMeta != null;
        stickMeta.setDisplayName("§5Dowsing Rod Seeking");
        stickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stickMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        dowsingStick.setItemMeta(stickMeta);

        list.add(dowsingStick);

        return dowsingStick;
    }

    public static ItemStack getDangerPremonition() {
        final ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemStack = item.getItemMeta();
        assert itemStack != null;
        itemStack.setDisplayName("§5Danger Premonition");
        itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.addEnchant(Enchantment.CHANNELING, 1, true);
        item.setItemMeta(itemStack);

        list.add(item);

        return item;
    }

    public static ItemStack getDreamDivination() {
        final ItemStack item = new ItemStack(Material.RED_BED);
        ItemMeta itemStack = item.getItemMeta();
        assert itemStack != null;
        itemStack.setDisplayName("§5Dream Divination");
        itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.addEnchant(Enchantment.CHANNELING, 1, true);
        item.setItemMeta(itemStack);

        list.add(item);

        return item;
    }

    public static ItemStack getCowHead() {
        final ItemStack cowHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta cowMeta = (SkullMeta) cowHead.getItemMeta();
        assert cowMeta != null;
        cowMeta.setDisplayName("§6Entities");
        String[] cowLore = {"§5Divine the location of entities"};
        cowMeta.setLore(Arrays.asList(cowLore));
        PlayerProfile cowProfile = Bukkit.createPlayerProfile(Plugin.randomUUID);
        PlayerTextures cowTextures = cowProfile.getTextures();
        try {
            cowTextures.setSkin(new URL("https://textures.minecraft.net/texture/c5a9cd58d4c67bccc8fb1f5f756a2d381c9ffac2924b7f4cb71aa9fa13fb5c"));
        }
        catch (MalformedURLException ignored) {}
        cowMeta.setOwnerProfile(cowProfile);
        cowHead.setItemMeta(cowMeta);

        list.add(cowHead);

        return cowHead;
    }

    public static ItemStack getGrassHead() {
        final ItemStack grassHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta grassMeta = (SkullMeta) grassHead.getItemMeta();
        assert grassMeta != null;
        grassMeta.setDisplayName("§6Biomes");
        String[] grassLore = {"§5Divine the location of biomes"};
        grassMeta.setLore(Arrays.asList(grassLore));
        PlayerProfile grassProfile = Bukkit.createPlayerProfile(Plugin.randomUUID);
        PlayerTextures grassTextures = grassProfile.getTextures();
        try {
            grassTextures.setSkin(new URL("http://textures.minecraft.net/texture/16bb9fb97ba87cb727cd0ff477f769370bea19ccbfafb581629cd5639f2fec2b"));
        }
        catch (MalformedURLException ignored) {}
        grassMeta.setOwnerProfile(grassProfile);
        grassHead.setItemMeta(grassMeta);

        list.add(grassHead);

        return grassHead;
    }

    public static ItemStack getDivinationHead() {
        final ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        assert playerMeta != null;
        playerMeta.setDisplayName("§6Biomes");
        String[] playerLore = {"§5Divine the location of players"};
        playerMeta.setLore(Arrays.asList(playerLore));
        PlayerProfile playerProfile = Bukkit.createPlayerProfile(Plugin.randomUUID);
        PlayerTextures playerTextures = playerProfile.getTextures();
        try {
            playerTextures.setSkin(new URL("http://textures.minecraft.net/texture/4d9d043adc884b979b4f42bdb350f2a301327cab49c4ce2de42a8f4601fe9dbf"));
        }
        catch (MalformedURLException ignored) {}
        playerMeta.setOwnerProfile(playerProfile);
        playerHead.setItemMeta(playerMeta);

        list.add(playerHead);

        return playerHead;
    }

    public static ItemStack getMeteor() {
        final ItemStack meteor = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meteorMeta = meteor.getItemMeta();
        assert meteorMeta != null;
        meteorMeta.setDisplayName("§4Summon Meteor");
        meteorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meteorMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        meteor.setItemMeta(meteorMeta);

        list.add(meteor);

        return meteor;
    }

    public static ItemStack getTornado() {
        final ItemStack tornado = new ItemStack(Material.FEATHER);
        ItemMeta tornadoMeta = tornado.getItemMeta();
        assert tornadoMeta != null;
        tornadoMeta.setDisplayName("§fSummon Tornado");
        tornadoMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        tornadoMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        tornado.setItemMeta(tornadoMeta);

        list.add(tornado);

        return tornado;
    }

    public static ItemStack getLightning() {
        final ItemStack lightning = new ItemStack(Material.LIGHTNING_ROD);
        ItemMeta lightningMeta = lightning.getItemMeta();
        assert lightningMeta != null;
        lightningMeta.setDisplayName("§bSummon Lightning");
        lightningMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lightningMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        lightning.setItemMeta(lightningMeta);

        list.add(lightning);

        return lightning;
    }

    public static ItemStack getSunnyWeather() {
        final ItemStack sun = new ItemStack(Material.SUNFLOWER);
        ItemMeta sunMeta = sun.getItemMeta();
        assert sunMeta != null;
        sunMeta.setDisplayName("§6Clear Weather");
        sunMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        sunMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        sun.setItemMeta(sunMeta);

        list.add(sun);

        return sun;
    }

    public static ItemStack getRainyWeather() {
        final ItemStack rain = new ItemStack(Material.WATER_BUCKET);
        ItemMeta rainMeta = rain.getItemMeta();
        assert rainMeta != null;
        rainMeta.setDisplayName("§3Rainy Weather");
        rainMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        rainMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        rain.setItemMeta(rainMeta);

        list.add(rain);

        return rain;
    }

    public static ItemStack getStormyWeather() {
        final ItemStack storm = new ItemStack(Material.BLAZE_ROD);
        ItemMeta stormMeta = storm.getItemMeta();
        assert stormMeta != null;
        stormMeta.setDisplayName("§9Stormy Weather");
        stormMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stormMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        storm.setItemMeta(stormMeta);

        list.add(storm);

        return storm;
    }

    public static ItemStack getThreadLength() {
        final ItemStack lengthString = new ItemStack(Material.STRING);
        ItemMeta stringMeta = lengthString.getItemMeta();
        assert stringMeta != null;
        stringMeta.setDisplayName("§6Change Thread Length");
        stringMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stringMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        lengthString.setItemMeta(stringMeta);

        list.add(lengthString);

        return lengthString;
    }

    public static ItemStack getExcludeEntities() {
        final ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§cExclude Entities");
        String[] lore = {
                "§cType in an Entity of which you want to", " §4exclude §cthe §4Spirit Body Threads",
                "§cToggle §4exclude §centities on", " §cand include entities off",
                "§cType cancel to §4cancel §cand reset to §4reset"

        };
        itemMeta.setLore(Arrays.asList(lore));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getIncludeEntities() {
        final ItemStack item = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§aInclude Entities");
        String[] lore = {
                "§aType in an Entity of which you want to", " §2include §athe §2Spirit Body Threads",
                "§aToggle §2include §aentities on", " §aand exclude entities off",
                "§aType cancel to §2cancel §aand reset to §2reset"

        };
        itemMeta.setLore(Arrays.asList(lore));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getAttack() {
        final ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§5Attack");
        String[] lore = {
                "§aRight click to attack!"
        };
        itemMeta.setLore(Arrays.asList(lore));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getConfirmPotion() {
        final ItemStack item = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§aBrew Potion");
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }

    public static ItemStack getMundanePotion() {
        final ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(new PotionData(PotionType.MUNDANE));
        potion.setItemMeta(potionMeta);
        return potion;
    }

    public static ItemStack getWaterPotion() {
        final ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(new PotionData(PotionType.WATER));
        potion.setItemMeta(potionMeta);
        return potion;
    }

    public static ItemStack getRegenPotion() {
        final ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(new PotionData(PotionType.REGEN));
        potion.setItemMeta(potionMeta);
        return potion;
    }
}
