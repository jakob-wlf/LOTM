package dev.ua.ikeepcalm.mystical.pathways.fool;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.utils.BeyonderItemsUtil;
import dev.ua.ikeepcalm.utils.GeneralItemsUtil;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.mystical.parents.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class FoolPotions extends Potion {

    public FoolPotions() {
        name = "fool";
        stringColor = "§5";
        mainIngredients = new HashMap<>();
        supplementaryIngredients = new HashMap<>();

        putMainIntoHashMap(9, BeyonderItemsUtil.getLavosSquidBlood(), BeyonderItemsUtil.getStellarAquaCrystal());
        putMainIntoHashMap(8, BeyonderItemsUtil.getGoatHorn(), BeyonderItemsUtil.getRose());
        putMainIntoHashMap(7, BeyonderItemsUtil.getRoot(), BeyonderItemsUtil.getPanther());
        putMainIntoHashMap(6, BeyonderItemsUtil.getPituitaryGland(), BeyonderItemsUtil.getShadowCharacteristic());
        putMainIntoHashMap(5, BeyonderItemsUtil.getWraithDust(), BeyonderItemsUtil.getGargoyleCrystal());
        putMainIntoHashMap(4, BeyonderItemsUtil.getBizarroEye(), BeyonderItemsUtil.getPlundererBody());
        putMainIntoHashMap(3, BeyonderItemsUtil.getWolfEye(), BeyonderItemsUtil.getWolfHeart());
        putMainIntoHashMap(2, LordOfTheMinecraft.instance.getCharacteristic().getCharacteristic(2, "fool", stringColor));
        putMainIntoHashMap(1, LordOfTheMinecraft.instance.getCharacteristic().getCharacteristic(1, "fool", stringColor));

        putSupplIntoHashMap(9, new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.MUTTON));
        putSupplIntoHashMap(8, GeneralItemsUtil.getWaterPotion(), new ItemStack(Material.SUNFLOWER), new ItemStack(Material.SHORT_GRASS));
        putSupplIntoHashMap(7, GeneralItemsUtil.getMundanePotion(), new ItemStack(Material.AMETHYST_SHARD), new ItemStack(Material.GLOW_INK_SAC));
        putSupplIntoHashMap(6, new ItemStack(Material.SKELETON_SKULL), new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GOLDEN_CARROT));
        putSupplIntoHashMap(5, GeneralItemsUtil.getRegenPotion(), new ItemStack(Material.DARK_OAK_LOG), new ItemStack(Material.FERMENTED_SPIDER_EYE));
        putSupplIntoHashMap(4, new ItemStack(Material.ENDER_EYE), new ItemStack(Material.BIRCH_LOG), new ItemStack(Material.TURTLE_HELMET));
        putSupplIntoHashMap(3, new ItemStack(Material.NETHER_STAR), new ItemStack(Material.ANCIENT_DEBRIS), new ItemStack(Material.BOOKSHELF));
        putSupplIntoHashMap(2, new ItemStack(Material.NETHER_STAR), new ItemStack(Material.DRAGON_BREATH), new ItemStack(Material.WITHER_ROSE));
        putSupplIntoHashMap(1);
    }

    @Override
    public ItemStack returnPotionForSequence(int sequence) {
        return Potion.createPotion(
                "§5",
                sequence,
                Objects.requireNonNull(Pathway.getNamesForPathway(name)).get(sequence),
                Color.fromBGR(128, 0, 128),
                ""
        );
    }
}
