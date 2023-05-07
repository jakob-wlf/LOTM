package de.firecreeper82.handlers.mobs.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import static de.firecreeper82.lotm.Plugin.beyonders;
import static de.firecreeper82.lotm.util.Util.getEntitiesInRadius;

public class Disorder extends Ability {
    public Disorder(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }
    @Override
    public void useAbility(){
        List<Entity> affected = getEntitiesInRadius(p, 50);
        for (Entity entity: affected){
            List<Beyonder> beyonderList = new ArrayList<>();
                    beyonderList.add(beyonders.get(entity.getUniqueId()));
            for (Beyonder beyonder :beyonderList)
                if (beyonder.getMarionettes().contains(entity.getUniqueId()) && beyonder.getPathway().getNameNormalized().equals("fool")){
                    beyonder.getPlayer().damage(1,null);
            }
        }
    }
    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.BARRIER,"","800",identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
