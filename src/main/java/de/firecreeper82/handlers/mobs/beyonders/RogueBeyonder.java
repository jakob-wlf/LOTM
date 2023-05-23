package de.firecreeper82.handlers.mobs.beyonders;


import de.firecreeper82.lotm.Plugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;


public class RogueBeyonder implements Listener {

    private final boolean aggressive;
    private final int sequence;
    private final int pathway;

    private boolean isWandering;

    private final NPC beyonder;

    private enum STATE {
        WANDER,
    }

    private STATE state;

    public RogueBeyonder(boolean aggressive, int sequence, int pathway) {
        this.aggressive = aggressive;
        this.sequence = sequence;
        this.pathway = pathway;

        Random random = new Random();

        String name = Plugin.instance.getNames().get(random.nextInt(Plugin.instance.getNames().size()));

        beyonder = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        beyonder.setProtected(false);

        SkinTrait skinTrait = new SkinTrait();
        skinTrait.setTexture("ewogICJ0aW1lc3RhbXAiIDogMTY4NDI1ODY0NTIyMywKICAicHJvZmlsZUlkIiA6ICJkOGNkMTNjZGRmNGU0Y2IzODJmYWZiYWIwOGIyNzQ4OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJaYWNoeVphY2giLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBiMTIxNzE1MjIxMjhmZGQ0YTZjYzFlMDRlMDZmNzk0ZmMxZTc5N2I2YmM2MGQ0N2E2NGMzYmZiMGEyM2VjMSIKICAgIH0KICB9Cn0=", "AoRkj9+lOTkFAFLOC9mY3zvLIooEINec6S2DKrO6om5Pq5CxJ289znUeZkKuxa5XbvlPr2FpwY2siaNMeQcJQwrBnv+8fDua4BR2dBEKge7w/4E3WHttn+hJsr6CGQ2j2FYlNMcnpJ6n/HNws58APBW2BhRwqIU6upH6p8XTmedIlCQHO1hv6pi9D82pF7eAn6dLuyKCYeDUZ9uPrOTZ9PUviQwqMFJn6Z+gAtN1Sg+tTXkEitp+HAvfYd7E71NQNFC7A35Bh2f6dlsjHgpESV5rWBtv9DN0tYtTF68CaNZUzSl5Wt+4U5mcIQ6zfNnlBIWe1eXYN5zSXsEQMnYwDAmPhWPnjD6L7TkWBIvqdh9YfGxbUyqRbzMVL0VrS+P6ZmTPMbPxUblFJLb95swMgpUwOQG9vcJvL/9Pp9ZpVednDk3g27Cp25QWZf1HNiIDgfrBsLEpKn2dmCFKGzCV7nlcNHqpnXCThSJZB/ALN2vTxF1MOJ22AIOAffHUCKARUZhxAVISYx1ZDr+FABAi6wHtyF5fJamgSVfoNnmCRvNCc0pzl9WEU1I66ddfwlcS2bHWdAsjq+xayftTGCDHIOk1w8fSHhGwSUz0wMY8Wco6gYxoRJAIeZ5OfxJD5lvP99H1htPf4hioK4/b+NwAYvJoG9TjDheLjJT2o+uQbfI=");
        beyonder.addTrait(skinTrait);

        state = STATE.WANDER;

        isWandering = false;

        new BukkitRunnable() {
            @Override
            public void run() {
                RogueBeyonder.this.run();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void run() {
        if (beyonder == null || !beyonder.isSpawned())
            return;

        if (state == STATE.WANDER) {
            if (isWandering) {
                return;
            }
            isWandering = true;
            beyonder.getDefaultGoalController().clear();
            beyonder.getDefaultGoalController().addGoal(WanderGoal.builder(beyonder).build(), 1);
        }

    }

    public void spawn(Location location) {
        beyonder.spawn(location);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (!beyonder.isSpawned())
            return;

        if (e.getEntity() == beyonder.getEntity())
            beyonder.destroy();
    }

    private void getRandomName() {

    }
}
