package dev.ua.ikeepcalm.mystical.pathways.sun;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.utils.BeyonderItemsUtil;
import dev.ua.ikeepcalm.mystical.Pathway;
import dev.ua.ikeepcalm.mystical.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class SunPotions extends Potion {

    public SunPotions() {
        name = "sun";
        stringColor = "§6";
        mainIngredients = new HashMap<>();
        supplementaryIngredients = new HashMap<>();

        putMainIntoHashMap(9, BeyonderItemsUtil.getSunflower(), BeyonderItemsUtil.getSirenRock());
        putMainIntoHashMap(8, BeyonderItemsUtil.getMagmaHeart());
        putMainIntoHashMap(7, BeyonderItemsUtil.getRoosterComb(), BeyonderItemsUtil.getSpiritTreeFruit());
        putMainIntoHashMap(6, BeyonderItemsUtil.getCrystallizedRoot(), BeyonderItemsUtil.getBirdFeather());
        putMainIntoHashMap(5, BeyonderItemsUtil.getRedRoosterComb(), BeyonderItemsUtil.getWhiteBrillianceRock());
        putMainIntoHashMap(4, BeyonderItemsUtil.getTailFeather(), BeyonderItemsUtil.getHolyBrillianceRock());
        putMainIntoHashMap(3, LordOfTheMinecraft.instance.getCharacteristic().getCharacteristic(3, "sun", stringColor));
        putMainIntoHashMap(2, LordOfTheMinecraft.instance.getCharacteristic().getCharacteristic(2, "sun", stringColor));
        putMainIntoHashMap(1, LordOfTheMinecraft.instance.getCharacteristic().getCharacteristic(1, "sun", stringColor));

        putSupplIntoHashMap(9, new ItemStack(Material.SHORT_GRASS), new ItemStack(Material.SUNFLOWER));
        putSupplIntoHashMap(8, new ItemStack(Material.SUNFLOWER), new ItemStack(Material.SWEET_BERRIES));
        putSupplIntoHashMap(7, new ItemStack(Material.OBSIDIAN), new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.FEATHER));
        putSupplIntoHashMap(6, BeyonderItemsUtil.getSunflower(), new ItemStack(Material.FERN), new ItemStack(Material.GLOW_BERRIES));
        putSupplIntoHashMap(5, new ItemStack(Material.MAGMA_CREAM), new ItemStack(Material.SUNFLOWER));
        putSupplIntoHashMap(4, BeyonderItemsUtil.getRedRoosterComb(), BeyonderItemsUtil.getWhiteBrillianceRock());
        putSupplIntoHashMap(3);
        putSupplIntoHashMap(2);
        putSupplIntoHashMap(1);
    }

    @Override
    public ItemStack returnPotionForSequence(int sequence) {
        return Potion.createPotion(
                "§6",
                sequence,
                Objects.requireNonNull(Pathway.getNamesForPathway(name)).get(sequence),
                Color.fromBGR(32, 165, 218),
                ""
        );
    }
}
