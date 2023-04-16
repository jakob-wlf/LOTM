package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.pathways.impl.door.abilities.Record;
import org.bukkit.entity.Player;

public abstract class Recordable extends Ability{
    public Recordable(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    public void record() {
        if(pathway.getBeyonder().getRecords().isEmpty())
            return;

        for(Record record : pathway.getBeyonder().getRecords()) {
            record.addAbility(this);
        }
    }

    public abstract void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded);

    public void destroy() {

    }
}
