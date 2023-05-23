package de.firecreeper82.listeners;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PotionListener implements Listener {

    @EventHandler
    public void onPotionInteract(PlayerInteractEvent e) {
        if (e.getItem() == null)
            return;

        int sequence = 0;
        Potion potion = null;
        outerloop:
        for (Potion p : Plugin.instance.getPotions()) {
            for (int i = 1; i < 10; i++) {
                if (p.returnPotionForSequence(i).equals(e.getItem())) {
                    sequence = i;
                    potion = p;
                    break outerloop;
                }
            }
        }
        if (sequence == 0)
            return;

        e.getPlayer().getInventory().remove(e.getItem());

        //Not a beyonder already
        if (!Plugin.beyonders.containsKey(e.getPlayer().getUniqueId())) {
            //initializing new Pathway
            Pathway pathway = Pathway.initializeNew(potion.getName(), e.getPlayer().getUniqueId(), sequence);
            if (pathway == null) {
                e.getPlayer().sendMessage("§cYour advancement has failed! You can call yourself lucky to still be alive...");
                return;
            }
            //makes new Beyonder loose control accordingly
            switch (9 - sequence) {
                case 0 -> pathway.getBeyonder().looseControl(93, 20);
                case 1 -> pathway.getBeyonder().looseControl(50, 20);
                case 2 -> pathway.getBeyonder().looseControl(25, 16);
                case 3, 4 -> pathway.getBeyonder().looseControl(15, 16);
                case 5 -> pathway.getBeyonder().looseControl(1, 20);
                default -> pathway.getBeyonder().looseControl(0, 10);
            }

            e.getPlayer().sendMessage(pathway.getItems().getAbilityInfo().get(sequence));
        }
        //Is a beyonder
        else {
            Beyonder beyonder = Plugin.beyonders.get(e.getPlayer().getUniqueId());
            beyonder.consumePotion(sequence, potion);
        }
    }
}
