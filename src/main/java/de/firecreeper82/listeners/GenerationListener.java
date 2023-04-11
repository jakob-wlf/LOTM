package de.firecreeper82.listeners;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Potion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
}
