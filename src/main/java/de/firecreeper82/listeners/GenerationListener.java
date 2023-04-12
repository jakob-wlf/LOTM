package de.firecreeper82.listeners;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.lotm.util.UtilItems;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

public class GenerationListener implements Listener {

    private static final double[] PROBABILITY_DISTRIBUTION = { 0.05, 0.06, 0.07, 0.08, 0.09, 0.10, 0.11, 0.12, 0.28 };
    private static final int MIN_VALUE = 1;

    @EventHandler
    public void onLootGenerate(LootGenerateEvent e) {
        ItemStack item;
        Random random = new Random();

        if(random.nextBoolean())
            return;

        int sequence = Util.biasedRandomNumber(PROBABILITY_DISTRIBUTION, MIN_VALUE);
        Potion potion = Plugin.instance.getPotions().get(random.nextInt(Plugin.instance.getPotions().size()));
        switch (random.nextInt(3)) {
            case 1 -> item = Plugin.instance.getRecipe().getRecipeForSequence(potion, sequence);

            case 2 -> item = Plugin.instance.getCharacteristic().getCharacteristic(sequence, potion.getName(), potion.getStringColor());

            default -> item = potion.returnPotionForSequence(sequence);
        }

        if(e.getInventoryHolder() == null)
            return;

        Inventory inv = e.getInventoryHolder().getInventory();
        inv.setItem(random.nextInt(inv.getSize()), item);

    }

    private ItemStack[] getRandomTrade() {
        ItemStack out;
        Random random = new Random();

        int sequence = Util.biasedRandomNumber(PROBABILITY_DISTRIBUTION, MIN_VALUE);
        Potion potion = Plugin.instance.getPotions().get(random.nextInt(Plugin.instance.getPotions().size()));

        switch (random.nextInt(4)) {
            case 1 -> out = Plugin.instance.getRecipe().getRecipeForSequence(potion, sequence);

            case 2 -> out = Plugin.instance.getCharacteristic().getCharacteristic(sequence, potion.getName(), potion.getStringColor());

            case 3 -> {
                out = UtilItems.getCauldron();
                ItemStack in = new ItemStack(Material.GOLD_INGOT);
                in.setAmount(random.nextInt(20, 54));
                ItemStack in2 = new ItemStack(Material.DIAMOND);
                in.setAmount(random.nextInt(1, 4));
                return new ItemStack[] { in, in2, out };
            }

            default -> out = potion.returnPotionForSequence(sequence);
        }

        ItemStack in = new ItemStack(Material.EMERALD);
        in.setAmount(Math.round((random.nextInt(10, 63) / (float) sequence)));

        ItemStack in2 = new ItemStack(Material.DIAMOND);
        in.setAmount(Math.round((random.nextInt(10, 63) / (float) sequence)));

        return new ItemStack[]{in, in2, out};
    }

    private Map.Entry<ItemStack, ItemStack> getRandomIngredientTrade() {
        return null;
    }
}
