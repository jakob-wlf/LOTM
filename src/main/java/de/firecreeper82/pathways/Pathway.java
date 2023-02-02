package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import org.bukkit.boss.BarColor;

import java.awt.*;
import java.util.UUID;

public abstract class Pathway {

    public UUID uuid;
    public Sequence sequence;
    public String name;
    public Beyonder beyonder;
    public int optionalSequence;
    public Color pathwayColor;
    public String stringColor;

    public Items items;

    public Pathway(UUID uuid) {
        this.uuid = uuid;
    }

    public Pathway(UUID uuid, int optionalSequence) {
        this.uuid = uuid;
        this.optionalSequence = optionalSequence;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public Color getPathwayColor() {
        return pathwayColor;
    }

    public void setPathwayColor(Color pathwayColor) {
        this.pathwayColor = pathwayColor;
    }

    public String getStringColor() {
        return stringColor;
    }

    public void setStringColor(String stringColor) {
        this.stringColor = stringColor;
    }
}

