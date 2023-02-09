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
        if(e.getItem() == null)
            return;

        int sequence = 0;
        Potion potion = null;
        outerloop: for(Potion p : Plugin.instance.getPotions()) {
            for(int i = 1; i < 10; i++) {
                if(p.returnPotionForSequence(i).equals(e.getItem())) {
                    sequence = i;
                    potion = p;
                    break outerloop;
                }
            }
        }
        if(sequence == 0)
            return;

        e.getPlayer().getInventory().remove(e.getItem());

        //Not a beyonder already
        if(!Plugin.beyonders.containsKey(e.getPlayer().getUniqueId())) {
            Pathway pathway = Pathway.initializeNew(potion.name, e.getPlayer().getUniqueId(), sequence);
            if(sequence < 9) {
                if(pathway == null) {
                    e.getPlayer().sendMessage("§cYour advancement has failed! You can call yourself lucky to still be alive...");
                    return;
                }
                switch(9 - sequence) {
                    case 1 -> pathway.getBeyonder().looseControl(6);
                    case 2 -> pathway.getBeyonder().looseControl(7);
                    case 3, 4 -> pathway.getBeyonder().looseControl(8);
                    case 5 -> pathway.getBeyonder().looseControl(9);
                    default -> pathway.getBeyonder().looseControl(10);
                }
            }
        }
        //Is a beyonder
        else {
            Beyonder beyonder = Plugin.beyonders.get(e.getPlayer().getUniqueId());
            if(!beyonder.getPathway().getNameNormalized().equals(potion.name)) {
                beyonder.looseControl(10);
                return;
            }
            Pathway pathway = beyonder.getPathway();
            if(pathway == null) {
                e.getPlayer().sendMessage("§cYour advancement has failed! You can call yourself lucky to still be alive...");
                return;
            }
            if(sequence < beyonder.getPathway().getSequence().getCurrentSequence() - 1) {
                switch(beyonder.getPathway().getSequence().getCurrentSequence() - 1 - sequence) {
                    case 1 -> pathway.getBeyonder().looseControl(6);
                    case 2 -> pathway.getBeyonder().looseControl(7);
                    case 3, 4 -> pathway.getBeyonder().looseControl(8);
                    case 5 -> pathway.getBeyonder().looseControl(9);
                    default -> pathway.getBeyonder().looseControl(10);
                }
            }
            //Else is temporary until I implement the not 100% chance ofg dying when loosing control
            else {
                beyonder.consumePotion(sequence);
            }
        }
    }
}
