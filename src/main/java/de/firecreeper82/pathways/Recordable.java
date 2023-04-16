package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.pathways.impl.door.abilities.Record;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public abstract class Recordable extends Ability{
    public Recordable(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    public void record() {
        if(pathway.getBeyonder().getRecords().isEmpty())
            return;

        for(Record record : pathway.getBeyonder().getRecords()) {
            Random random = new Random();

            if(record.getPathway().getSequence().getCurrentSequence() - getSequence() < 0) {
                record.addAbility(this);
                return;
            }

            if(record.getPathway().getSequence().getCurrentSequence() - getSequence() == 0) {
                if(random.nextInt(3) == 0)
                    record.addAbility(this);
                return;
            }

            if(random.nextInt((record.getPathway().getSequence().getCurrentSequence() - getSequence()) * 3) == 0)
                record.addAbility(this);
        }
    }

    public abstract void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded);

    public void destroy(Beyonder beyonder, boolean recorded) {
        if(!recorded)
            return;

        for(ItemStack itemStack : beyonder.getPlayer().getInventory().getContents()) {
            if(itemStack == null)
                return;

            if(itemStack.isSimilar(getItem())) {
                beyonder.getPlayer().getInventory().remove(itemStack);
                return;
            }
        }

        pathway.getSequence().getRecordables().remove(this);
    }

    @Override
    public void useAbility() {
        double multiplier = getMultiplier();

        record();

        p = pathway.getBeyonder().getPlayer();
        useAbility(p, multiplier, pathway.getBeyonder(), false);
    }
}
