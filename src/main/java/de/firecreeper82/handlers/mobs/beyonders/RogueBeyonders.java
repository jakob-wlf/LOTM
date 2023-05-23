package de.firecreeper82.handlers.mobs.beyonders;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.HashMap;
import java.util.Random;

public class RogueBeyonders implements Listener {

    private final HashMap<EntityType, Integer> spawnProbabilityTable;

    private static final double[] PROBABILITY_DISTRIBUTION = {0.05, 0.06, 0.07, 0.08, 0.09, 0.10, 0.11, 0.12, 0.28};
    private static final int MIN_VALUE = 1;

    public RogueBeyonders() {
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        spawnProbabilityTable = new HashMap<>();

        spawnProbabilityTable.put(EntityType.VILLAGER, 100);
        spawnProbabilityTable.put(EntityType.COW, 1);
        spawnProbabilityTable.put(EntityType.SHEEP, 1);
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        Random random = new Random();

        if (!spawnProbabilityTable.containsKey(e.getEntity().getType()))
            return;

        if (random.nextInt(Math.max(1, 101 - spawnProbabilityTable.get(e.getEntity().getType()))) != 0)
            return;

        boolean aggressive = (random.nextInt(4) == 0);
        int sequence = Util.biasedRandomNumber(PROBABILITY_DISTRIBUTION, MIN_VALUE);
        int pathway = random.nextInt(Plugin.temp.size());

        RogueBeyonder rogueBeyonder = new RogueBeyonder(aggressive, sequence, pathway);
        Plugin.instance.getServer().getPluginManager().registerEvents(rogueBeyonder, Plugin.instance);
        rogueBeyonder.spawn(e.getLocation());
    }
}
