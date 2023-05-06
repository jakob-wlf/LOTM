package de.firecreeper82.pathways.impl.emperor;

import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import de.firecreeper82.pathways.impl.demoness.DemonessSequence;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class EmperorPathway extends Pathway {

    public EmperorPathway(UUID uuid, int optionalSequence) {
        super(uuid, optionalSequence);
    }

    @Override
    public void init() {
        sequence = new EmperorSequence(this, optionalSequence);
        name = "§0Black Emperor";
        nameNormalized = "emperor";
        stringColor = "§0";
    }

    @Override
    public void initItems() {
        items = new EmperorItems(getPathway());
    }

    public static HashMap<Integer, String> getNames() {
        HashMap<Integer, String> names;
        names = new HashMap<>();
        names.put(9, "Lawyer");
        names.put(8, "Barbarian");
        names.put(7, "Briber");
        names.put(6, "Baron of Corruption");
        names.put(5, "Mentor of Disorder");
        names.put(4, "Earl of the Fallen");
        names.put(3, "Frenzied Mage");
        names.put(2, "Duke of Entropy");
        names.put(1, "Prince of Abolition");
        return names;
    }

}
