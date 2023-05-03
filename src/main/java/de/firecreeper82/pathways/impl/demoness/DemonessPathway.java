package de.firecreeper82.pathways.impl.demoness;

import de.firecreeper82.pathways.Pathway;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class DemonessPathway extends Pathway {

    public DemonessPathway(UUID uuid, int optionalSequence) {
        super(uuid, optionalSequence);
    }

    @Override
    public void init() {
        sequence = new DemonessSequence(this, optionalSequence);
        name = "§dDemoness";
        nameNormalized = "demoness";
        stringColor = "§d";
    }

    @Override
    public void initItems() {
        items = new DemonessItems(getPathway());
    }

    public static HashMap<Integer, String> getNames() {
        HashMap<Integer, String> names;
        names = new HashMap<>();
        names.put(9, "Assassin");
        names.put(8, "Instigator");
        names.put(7, "Witch");
        names.put(6, "Demoness of Pleasure");
        names.put(5, "Demoness of Affliction");
        names.put(4, "Demoness of Despair");
        names.put(3, "Demoness of Unaging");
        names.put(2, "Demoness of Catastrophe");
        names.put(1, "Demoness of Apocalypse");
        return names;
    }

}
