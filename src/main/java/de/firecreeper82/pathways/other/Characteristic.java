package de.firecreeper82.pathways.other;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;


public class Characteristic {

    private final HashMap<String, URL> blocksForPathway;
    private final UUID uuid;

    private final HashMap<ItemStack, int[]> allCharacteristics;

    public Characteristic() throws MalformedURLException {
        blocksForPathway = new HashMap<>();
        uuid = Plugin.randomUUID;
        blocksForPathway.put("fool", new URL("http://textures.minecraft.net/texture/855af6c5ff21eb55631a25221d753cdc9a0f679d5cacf555b350ba0e3521e092"));
        blocksForPathway.put("sun", new URL("http://textures.minecraft.net/texture/8a03a8a877de7a4d6b167633a96ae3983998fd9d9a4c5e3fa817d138e81e4499"));
        blocksForPathway.put("door", new URL("http://textures.minecraft.net/texture/5f1e18cd9f9d3822196f0ccf1a8e071d87bb32ab50df4d6cfed93a1a948835ca"));
        blocksForPathway.put("demoness", new URL("http://textures.minecraft.net/texture/b21f8f3e52fa21b45ff56f3f73dd21661ff257d97bd52ed958f2d757be89a961"));
        blocksForPathway.put("tyrant", new URL("http://textures.minecraft.net/texture/de73a8675ec1be13b1932627533212b1ded2b1773e54b06ea489a35d9744d615"));

        allCharacteristics = new HashMap<>();
        for(int i = 9; i > 0; i--) {
            allCharacteristics.put(getCharacteristic(i, "sun", "§6"), new int[]{0, i});
            allCharacteristics.put(getCharacteristic(i, "fool", "§5"), new int[]{1, i});
            allCharacteristics.put(getCharacteristic(i, "door", "§b"), new int[]{2, i});
            allCharacteristics.put(getCharacteristic(i, "demoness", "§d"), new int[]{3, i});
            allCharacteristics.put(getCharacteristic(i, "tyrant", "§9"), new int[]{4, i});
        }

    }


    public ItemStack getCharacteristic(int sequence, String pathway, String pathwayColor) {
        final ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        assert playerMeta != null;
        playerMeta.setDisplayName(pathwayColor + Util.capitalize(pathway) + " Beyonder Characteristic");
        String[] playerLore = {
                pathwayColor + "A Characteristic corresponding to Sequence " + sequence,
                pathwayColor + "of the " + Util.capitalize(pathway) + " Pathway!"
        };
        playerMeta.setLore(Arrays.asList(playerLore));
        if (blocksForPathway.get(pathway) != null) {
            PlayerProfile playerProfile = Bukkit.createPlayerProfile(uuid);
            PlayerTextures playerTextures = playerProfile.getTextures();
            playerTextures.setSkin(blocksForPathway.get(pathway));
            playerMeta.setOwnerProfile(playerProfile);
        }
        playerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        playerMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        playerHead.setItemMeta(playerMeta);

        return playerHead;
    }

    public int[] getCharacteristicInfo(ItemStack characteristic) {
        if(allCharacteristics.containsKey(characteristic))
            return allCharacteristics.get(characteristic);
        else
            return new int[]{-1, -1};
    }
}
