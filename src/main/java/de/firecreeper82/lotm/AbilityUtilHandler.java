package de.firecreeper82.lotm;

import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.impl.demoness.abilities.*;
import de.firecreeper82.pathways.impl.door.abilities.*;
import de.firecreeper82.pathways.impl.fool.abilities.AirBullet;
import de.firecreeper82.pathways.impl.fool.abilities.FlameControlling;
import de.firecreeper82.pathways.impl.fool.abilities.Grafting;
import de.firecreeper82.pathways.impl.sun.abilities.*;
import de.firecreeper82.pathways.impl.tyrant.abilities.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AbilityUtilHandler {

    private static final HashMap<Integer, List<NPCAbility>> abilities = new HashMap<>();
    private static final HashMap<Integer, String> colorPrefix = new HashMap<>();
    private static final HashMap<Integer, String> pathwayNames = new HashMap<>();

    public AbilityUtilHandler() {
        initAbilities();
        initColorPrefix();
        initPathwayNames();
    }

    private void initPathwayNames() {
        pathwayNames.put(0, "sun");
        pathwayNames.put(1, "fool");
        pathwayNames.put(2, "door");
        pathwayNames.put(3, "demoness");
        pathwayNames.put(4, "tyrant");
    }

    private void initColorPrefix() {
        colorPrefix.put(0, "§6");
        colorPrefix.put(1, "§5");
        colorPrefix.put(2, "§b");
        colorPrefix.put(3, "§d");
        colorPrefix.put(4, "§9");
    }

    private void initAbilities() {
        abilities.put(0, Arrays.asList(
                new BeamOfLight(0, null, 3, null, true),
                new CleaveOfPurification(0, null, 7, null, true),
                new FireOfLight(0, null, 7, null, true),
                new FlaringSun(0, null, 4, null, true),
                new HolyLight(0, null, 8, null, true),
                new HolyLightSummoning(0, null, 7, null, true),
                new LightOfHoliness(0, null, 5, null, true),
                new LightOfPurification(0, null, 4, null, true),
                new SpearOfLight(0, null, 2, null, true)
        ));

        abilities.put(1, Arrays.asList(
                new AirBullet(0, null, 7, null, true),
                new FlameControlling(0, null, 7, null, true),
                new Grafting(0, null, 1, null, true)
        ));

        abilities.put(2, Arrays.asList(
                new BlackHole(0, null, 2, null, true),
                new Conceptualize(0, null, 3, null, true),
                new ElectricShock(0, null, 8, null, true),
                new Exile(0, null, 4, null, true),
                new Freezing(0, null, 8, null, true),
                new SpaceConcealment(0, null, 4, null, true),
                new Wind(0, null, 8, null, true)
        ));

        abilities.put(3, Arrays.asList(
                new CalamityManipulation(0, null, 2, null, true),
                new ColdWind(0, null, 7, null, true),
                new DarkFlames(0, null, 7, null, true),
                new Epidemic(0, null, 5, null, true),
                new FrostMagic(0, null, 7, null, true),
                new FrostSpear(0, null, 6, null, true),
                new Petrification(0, null, 3, null, true),
                new Pestilence(0, null, 4, null, true),
                new ThreadManipulation(0, null, 6, null, true)
        ));

        abilities.put(4, Arrays.asList(
                new Lightning(0, null, 5, null, true),
                new LightningStorm(0, null, 3, null, true),
                new RagingBlows(0, null, 8, null, true),
                new Roar(0, null, 4, null, true),
                new Tornado(0, null, 4, null, true),
                new WaterSpells(0, null, 7, null, true),
                new WindManipulation(0, null, 6, null, true)
        ));
    }

    public static List<NPCAbility> getSequenceAbilities(int pathway, int sequence) {
        List<NPCAbility> pathwayAbilities = abilities.get(pathway);
        return pathwayAbilities.stream().filter(npcAbility -> npcAbility.getSequence() == sequence).collect(Collectors.toList());
    }

    public static HashMap<Integer, String> getColorPrefix() {
        return colorPrefix;
    }
    public static HashMap<Integer, List<NPCAbility>> getAbilities() {
        return abilities;
    }
    public static HashMap<Integer, String> getPathwayNames() {
        return pathwayNames;
    }


}
