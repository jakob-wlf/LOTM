package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import de.firecreeper82.pathways.impl.fool.marionettes.Marionette;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static de.firecreeper82.lotm.Plugin.beyonders;
import static de.firecreeper82.lotm.util.Util.getEntitiesInRadius;

public class Disorder extends Recordable {
    public Disorder(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        if (!recorded) pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
        destroy(beyonder, recorded);


        List<Entity> affected = getEntitiesInRadius(p, 50);
        for (Entity e : affected) {
            //fetch all beyonders in reach.
            List<Beyonder> beyonderList = new ArrayList<>();
            assert beyonders.containsKey(e.getUniqueId());
            beyonderList.add(beyonders.get(e.getUniqueId()));

            for (Beyonder beyonderAffected : beyonderList)
                //check if the beyonder in list is part of the fool pathway. Then, check if the entity "e" is inside a player's marionettes.
                if (beyonderAffected.getPathway().getNameNormalized().equals("fool") && beyonderAffected.getMarionettes().contains((Marionette) e)) {
                    beyonderAffected.getPlayer().damage(1, null);

                }
        }
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.BARRIER, "Disorder", "800", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
