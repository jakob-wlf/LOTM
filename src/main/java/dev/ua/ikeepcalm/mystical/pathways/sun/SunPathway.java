package dev.ua.ikeepcalm.mystical.pathways.sun;

import dev.ua.ikeepcalm.mystical.parents.Pathway;

import java.util.HashMap;
import java.util.UUID;

public class SunPathway extends Pathway {

    public SunPathway(UUID uuid, int optionalSequence, int pathwayInt) {
        super(uuid, optionalSequence, pathwayInt);
    }

    @Override
    public void init() {
        sequence = new SunSequence(this, optionalSequence);
        name = "§6Sun";
        nameNormalized = "sun";
        stringColor = "§6";
    }

    @Override
    public void initItems() {
        items = new SunItems(getPathway());
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
