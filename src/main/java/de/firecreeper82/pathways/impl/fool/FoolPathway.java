package de.firecreeper82.pathways.impl.fool;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class FoolPathway extends Pathway {

    public FoolPathway(UUID uuid) {
        super(uuid);
        sequence = new FoolSequence(this);
        init();
    }

    public FoolPathway(UUID uuid, int optionalSequence) {
        super(uuid, optionalSequence);
        sequence = new FoolSequence(this, optionalSequence);
        init();
    }

    @Override
    public void init() {
        beyonder = Plugin.beyonders.get(uuid);
        name = "§6Sun";
        nameNormalized = "sun";
        pathwayColor = Color.orange;
        stringColor = "§6";
    }

    @Override
    public void initItems() {
        items = new FoolItems(getPathway());
    }

    public static HashMap<Integer, String> getNames() {
        HashMap<Integer, String> names;
        names = new HashMap<>();
        names.put(9, "Bard");
        names.put(8, "Light Supplicant");
        names.put(7, "Solar High Priest");
        names.put(6, "Notary");
        names.put(5, "Priest of Light");
        names.put(4, "Unshadowed");
        names.put(3, "Justice Mentor");
        names.put(2, "Light Seeker");
        names.put(1, "White Angel");
        return names;
    }

}
