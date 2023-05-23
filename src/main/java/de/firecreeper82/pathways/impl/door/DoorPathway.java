package de.firecreeper82.pathways.impl.door;

import de.firecreeper82.pathways.Pathway;

import java.util.HashMap;
import java.util.UUID;

public class DoorPathway extends Pathway {

    public DoorPathway(UUID uuid, int optionalSequence) {
        super(uuid, optionalSequence);
    }

    @Override
    public void init() {
        sequence = new DoorSequence(this, optionalSequence);
        name = "§bDoor";
        nameNormalized = "door";
        stringColor = "§b";
    }

    @Override
    public void initItems() {
        items = new DoorItems(getPathway());
    }

    public static HashMap<Integer, String> getNames() {
        HashMap<Integer, String> names;
        names = new HashMap<>();
        names.put(9, "Apprentice");
        names.put(8, "Trickmaster");
        names.put(7, "Astrologer");
        names.put(6, "Scribe");
        names.put(5, "Traveler");
        names.put(4, "Secrets Sorcerer");
        names.put(3, "Wanderer");
        names.put(2, "Planeswalker");
        names.put(1, "Key of Stars");
        return names;
    }

}
