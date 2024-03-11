package dev.ua.ikeepcalm.mystical.pathways.tyrant;

import dev.ua.ikeepcalm.mystical.Pathway;

import java.util.HashMap;
import java.util.UUID;

public class TyrantPathway extends Pathway {

    public TyrantPathway(UUID uuid, int optionalSequence, int pathwayInt) {
        super(uuid, optionalSequence, pathwayInt);
    }

    @Override
    public void init() {
        sequence = new TyrantSequence(this, optionalSequence);
        name = "§9Tyrant";
        nameNormalized = "tyrant";
        stringColor = "§9";
    }

    @Override
    public void initItems() {
        items = new TyrantItems(getPathway());
    }

    public static HashMap<Integer, String> getNames() {
        HashMap<Integer, String> names;
        names = new HashMap<>();
        names.put(9, "Sailor");
        names.put(8, "Folk of Rage");
        names.put(7, "Seafarer");
        names.put(6, "Wind Blessed");
        names.put(5, "Ocean Songster");
        names.put(4, "Cataclysmic Interrer");
        names.put(3, "Sea King");
        names.put(2, "Calamity");
        names.put(1, "Thunder God");
        return names;
    }

}
