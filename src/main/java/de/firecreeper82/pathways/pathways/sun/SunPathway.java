package de.firecreeper82.pathways.pathways.sun;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.boss.BarColor;

import java.awt.*;
import java.util.UUID;

public class SunPathway extends Pathway {

    public SunPathway(UUID uuid) {
        super(uuid);
        beyonder = Plugin.beyonders.get(uuid);
        name = "§6Sun";
        pathwayColor = Color.orange;
        stringColor = "§6";
        sequence = new SunSequence(this);
        items = new SunItems(this);
    }

    public SunPathway(UUID uuid, int optionalSequence) {
        super(uuid, optionalSequence);
        beyonder = Plugin.beyonders.get(uuid);
        name = "§6Sun";
        pathwayColor = Color.orange;
        stringColor = "§6";
        sequence = new SunSequence(this, optionalSequence);
        items = new SunItems(this);
    }
}
