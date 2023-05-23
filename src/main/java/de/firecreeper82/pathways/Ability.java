package de.firecreeper82.pathways;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Ability {
    protected int identifier;
    protected Pathway pathway;
    protected Player p;
    protected int sequence;
    protected Items items;

    public Ability(int identifier, Pathway pathway, int sequence, Items items) {
        this.identifier = identifier;
        this.pathway = pathway;
        this.sequence = sequence;
        this.items = items;
    }


    public abstract void useAbility();

    public abstract ItemStack getItem();

    public void onHold() {
    }

    public void leftClick() {
    }

    public Pathway getPathway() {
        return pathway;
    }

    public void setPathway(Pathway pathway) {
        this.pathway = pathway;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public void removeAbility() {
    }

    public int getSequence() {
        return sequence;
    }

    public double getMultiplier() {
        double multiplier = 1;
        if (pathway.getSequence().getSequenceMultiplier().containsKey(pathway.getSequence().getCurrentSequence())) {
            multiplier = pathway.getSequence().getSequenceMultiplier().get(pathway.getSequence().getCurrentSequence());
        } else {
            for (int i = pathway.getSequence().currentSequence; i < 9; i++) {
                if (pathway.getSequence().getSequenceMultiplier().containsKey(i)) {
                    multiplier = pathway.getSequence().getSequenceMultiplier().get(i);
                }
            }
        }
        return multiplier;
    }

    public static double getMultiplier(Pathway pathway) {
        double multiplier = 1;
        if (pathway.getSequence().getSequenceMultiplier().containsKey(pathway.getSequence().getCurrentSequence())) {
            multiplier = pathway.getSequence().getSequenceMultiplier().get(pathway.getSequence().getCurrentSequence());
        } else {
            for (int i = pathway.getSequence().currentSequence; i < 9; i++) {
                if (pathway.getSequence().getSequenceMultiplier().containsKey(i)) {
                    multiplier = pathway.getSequence().getSequenceMultiplier().get(i);
                }
            }
        }
        return multiplier;
    }
}
