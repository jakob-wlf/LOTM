package de.firecreeper82.pathways;

import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.lotm.util.mobs.BeyonderMobs;
import org.bukkit.entity.EntityType;

public class EntitiesHandler {

    public EntitiesHandler() {
        BeyonderMobs.spawnEntity("§9Lavos Squid", "squid", 100, BeyonderItems.getLavosSquidBlood(), EntityType.SQUID, 20);
    }
}
