package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.impl.fool.FoolPathway;
import de.firecreeper82.pathways.impl.sun.SunPathway;
import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public abstract class Pathway {

    public UUID uuid;
    public Sequence sequence;
    public String name;
    public Beyonder beyonder;
    public int optionalSequence;
    public Color pathwayColor;
    public String stringColor;
    public String nameNormalized;

    public Items items;

    public Pathway(UUID uuid) {
        this.uuid = uuid;
    }

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


    public static Pathway initializeNew(String pathway, UUID uuid, int sequence) {
        Pathway pathwayObject;
        if(Plugin.beyonders.containsKey(uuid))
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
            default -> {return null;}
        }
    }

}

