package de.firecreeper82.lotm.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    public static ItemStack getStellarAquaCrystal() {
        final ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§bStellar Aqua Crystal");
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

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        try { textures.setSkin(new URL("http://textures.minecraft.net/texture/3ccc8a690c89ebf01adf0440c0a3d540e2db89cfc97ad3b8e01810bf3289f67a")); }
        catch (MalformedURLException ignored) {}

        itemMeta.setOwnerProfile(profile);

        item.setItemMeta(itemMeta);

        list.add(item);

        return item;
    }
}
