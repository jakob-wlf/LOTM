package de.firecreeper82.pathways.impl.sun;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.UUID;

public class SunPathway extends Pathway {

    public SunPathway(UUID uuid) {
        super(uuid);
        sequence = new SunSequence(this);
        init();
    }

    public SunPathway(UUID uuid, int optionalSequence) {
        super(uuid, optionalSequence);
        sequence = new SunSequence(this, optionalSequence);
        init();
    }

    @Override
    public void init() {
        beyonder = Plugin.beyonders.get(uuid);
        name = "§6Sun";
        nameNormalized = "sun";
        pathwayColor = Color.orange;
        stringColor = "§6";
        new BukkitRunnable() {
            @Override
            public void run() {
                items = new SunItems(getPathway());
            }
        }.runTaskLater(Plugin.instance, 2);
    }
}
