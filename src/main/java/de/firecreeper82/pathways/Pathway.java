package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.impl.demoness.DemonessPathway;
import de.firecreeper82.pathways.impl.door.DoorPathway;
import de.firecreeper82.pathways.impl.fool.FoolPathway;
import de.firecreeper82.pathways.impl.sun.SunPathway;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public abstract class Pathway {

    protected UUID uuid;
    protected Sequence sequence;
    protected String name;
    protected Beyonder beyonder;
    protected int optionalSequence;
    protected String stringColor;
    protected String nameNormalized;

    public static final String[] validNames = new String[]{
            "sun",
            "fool"
    };

    public Items items;

    public Pathway(UUID uuid, int optionalSequence) {
        this.uuid = uuid;
        this.optionalSequence = optionalSequence;
    }


    public void init() {

    }

    public UUID getUuid() {
        return uuid;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Beyonder getBeyonder() {
        return beyonder;
    }

    public void setBeyonder(Beyonder beyonder) {
        this.beyonder = beyonder;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public String getStringColor() {
        return stringColor;
    }

    public Pathway getPathway() {
        return this;
    }

    public String getNameNormalized() {
        return nameNormalized;
    }


    //Initializes a new Pathway
    //Called from BeyonderCmd, Plugin and PotionListener
    public static Pathway initializeNew(String pathway, UUID uuid, int sequence) {
        Pathway pathwayObject;
        if (Plugin.beyonders.containsKey(uuid))
            return null;
        switch (pathway) {
            case "sun" -> {
                pathwayObject = new SunPathway(uuid, sequence);
                Beyonder beyonder = new Beyonder(uuid, pathwayObject);
                Plugin.beyonders.put(uuid, beyonder);
                Plugin.instance.getServer().getPluginManager().registerEvents(beyonder, Plugin.instance);
                return pathwayObject;
            }
            case "fool" -> {
                pathwayObject = new FoolPathway(uuid, sequence);
                Beyonder beyonder = new Beyonder(uuid, pathwayObject);
                Plugin.beyonders.put(uuid, beyonder);
                Plugin.instance.getServer().getPluginManager().registerEvents(beyonder, Plugin.instance);
                return pathwayObject;
            }
            case "door" -> {
                pathwayObject = new DoorPathway(uuid, sequence);
                Beyonder beyonder = new Beyonder(uuid, pathwayObject);
                Plugin.beyonders.put(uuid, beyonder);
                Plugin.instance.getServer().getPluginManager().registerEvents(beyonder, Plugin.instance);
                return pathwayObject;
            }
            case "demoness" -> {
                pathwayObject = new DemonessPathway(uuid, sequence);
                Beyonder beyonder = new Beyonder(uuid, pathwayObject);
                Plugin.beyonders.put(uuid, beyonder);
                Plugin.instance.getServer().getPluginManager().registerEvents(beyonder, Plugin.instance);
                return pathwayObject;
            }
            default -> {
                return null;
            }
        }
    }

    public abstract void initItems();


    public static HashMap<Integer, String> getNamesForPathway(String pathway) {
        switch (pathway.toLowerCase()) {
            case "sun" -> {
                return SunPathway.getNames();
            }
            case "fool" -> {
                return FoolPathway.getNames();
            }
            case "door" -> {
                return DoorPathway.getNames();
            }
            case "demoness" -> {
                return DemonessPathway.getNames();
            }
            default -> {
                return null;
            }
        }
    }

    @SuppressWarnings("unused")
    public static boolean isValidPathway(String pathway) {
        return Arrays.asList(validNames).contains(pathway);
    }
}

