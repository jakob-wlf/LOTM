package de.firecreeper82.pathways.impl.fool;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class FoolPathway extends Pathway {

    public FoolPathway(UUID uuid, int optionalSequence) {
        super(uuid, optionalSequence);
    }

    @Override
    public void init() {
        sequence = new FoolSequence(this, optionalSequence);
        name = "§5Fool";
        nameNormalized = "fool";
        stringColor = "§5";
    }

    @Override
    public void initItems() {
        items = new FoolItems(this);
    }

    public static HashMap<Integer, String> getNames() {
        HashMap<Integer, String> names;
        names = new HashMap<>();
        names.put(9, "Seer");
        names.put(8, "Clown");
        names.put(7, "Magician");
        names.put(6, "Faceless");
        names.put(5, "Marionettist");
        names.put(4, "Bizarro Sorcerer");
        names.put(3, "Scholar of Yore");
        names.put(2, "Miracle Invoker");
        names.put(1, "Attendant of Mysteries");
        return names;
    }
}
