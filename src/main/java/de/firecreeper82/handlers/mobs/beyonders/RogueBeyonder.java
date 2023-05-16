package de.firecreeper82.handlers.mobs.beyonders;


import de.firecreeper82.lotm.Plugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;


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

        beyonder = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Klein");
        beyonder.setProtected(false);

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
        if(beyonder == null || !beyonder.isSpawned())
            return;

        if(state == STATE.WANDER) {
            if(isWandering) {
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
        if(!beyonder.isSpawned())
            return;

        if(e.getEntity() == beyonder.getEntity())
            beyonder.destroy();
    }
}
