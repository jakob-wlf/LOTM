package de.firecreeper82.pathways;

import org.bukkit.entity.Player;

public abstract class Ability {
    public int identifier;
    public Pathway pathway;
    public Player p;

    public Ability(int identifier, Pathway pathway) {
        this.identifier = identifier;
        this.pathway = pathway;
    }


    public void useAbility() {

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
}
