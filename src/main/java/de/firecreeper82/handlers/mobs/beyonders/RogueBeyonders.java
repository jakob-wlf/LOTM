package de.firecreeper82.handlers.mobs.beyonders;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.impl.demoness.abilities.*;
import de.firecreeper82.pathways.impl.door.abilities.*;
import de.firecreeper82.pathways.impl.fool.abilities.AirBullet;
import de.firecreeper82.pathways.impl.fool.abilities.FlameControlling;
import de.firecreeper82.pathways.impl.fool.abilities.Grafting;
import de.firecreeper82.pathways.impl.sun.abilities.*;
import de.firecreeper82.pathways.impl.tyrant.abilities.*;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class RogueBeyonders implements Listener {

    private final HashMap<EntityType, Integer> spawnProbabilityTable;

    private static final double[] PROBABILITY_DISTRIBUTION = {0.05, 0.06, 0.07, 0.08, 0.09, 0.10, 0.11, 0.12, 0.28};
    private static final int MIN_VALUE = 1;

    public RogueBeyonders() {
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        spawnProbabilityTable = new HashMap<>();

        spawnProbabilityTable.put(EntityType.COW, 10);
        spawnProbabilityTable.put(EntityType.SHEEP, 10);
        spawnProbabilityTable.put(EntityType.SKELETON, 15);
        spawnProbabilityTable.put(EntityType.ZOMBIE_VILLAGER, 5);
        spawnProbabilityTable.put(EntityType.SPIDER, 10);
        spawnProbabilityTable.put(EntityType.HUSK, 35);
        spawnProbabilityTable.put(EntityType.CHICKEN, 10);
        spawnProbabilityTable.put(EntityType.WOLF, 5);
        spawnProbabilityTable.put(EntityType.ZOMBIE, 15);
        spawnProbabilityTable.put(EntityType.CREEPER, 5);
        spawnProbabilityTable.put(EntityType.MAGMA_CUBE, 5);
        spawnProbabilityTable.put(EntityType.PIGLIN, 5);
        spawnProbabilityTable.put(EntityType.ZOMBIFIED_PIGLIN, 5);
        spawnProbabilityTable.put(EntityType.ENDERMAN, 5);
        spawnProbabilityTable.put(EntityType.HORSE, 5);
        spawnProbabilityTable.put(EntityType.FOX, 5);

    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        Random random = new Random();

//        if (!spawnProbabilityTable.containsKey(e.getEntity().getType()))
//            return;
//
//        if (random.nextInt(100) > spawnProbabilityTable.get(e.getEntity().getType()))
//            return;
//
//        boolean aggressive = (random.nextInt(4) == 0);
//        int sequence = Util.biasedRandomNumber(PROBABILITY_DISTRIBUTION, MIN_VALUE);
//        int pathway = random.nextInt(5);
//
//        if(Plugin.instance.getCurrentRogueBeyonders().size() > 50) {
//            Plugin.instance.removeRogueBeyonder(Plugin.instance.getCurrentRogueBeyonders().get((new Random()).nextInt(Plugin.instance.getCurrentRogueBeyonders().size())));
//        }
//
//        spawnNPC(aggressive, sequence, pathway, e.getLocation());
    }

    public void spawnNPC(boolean aggressive, int sequence, int pathway, Location location) {
        RogueBeyonder rogueBeyonder = new RogueBeyonder(aggressive, sequence, pathway, this);
        Plugin.instance.getServer().getPluginManager().registerEvents(rogueBeyonder, Plugin.instance);
        rogueBeyonder.spawn(location);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(CitizensAPI.getNPCRegistry().getByUniqueId(e.getEntity().getUniqueId()) != null)
            e.setDeathMessage("");
    }
}
