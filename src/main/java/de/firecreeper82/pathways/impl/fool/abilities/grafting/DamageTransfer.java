package de.firecreeper82.pathways.impl.fool.abilities.grafting;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class DamageTransfer implements Listener {

    private final LivingEntity receive;
    private final LivingEntity target;

    private int timesReceived;

    public DamageTransfer(LivingEntity receive, LivingEntity target, ArrayList<DamageTransfer> damageTransfers) {
        this.receive = receive;
        this.target = target;

        timesReceived = 0;

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        DamageTransfer instance = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(timesReceived >= 10 || !target.isValid() || !receive.isValid()) {
                    damageTransfers.remove(instance);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageEvent e) {
        if(timesReceived >= 10 || e.getEntity() != receive || !target.isValid() || !receive.isValid())
            return;

        timesReceived++;
        if(e instanceof EntityDamageByEntityEvent event)
            target.damage(e.getDamage(), event.getDamager());
        else
            target.damage(e.getDamage());
        e.setCancelled(true);
    }

    public LivingEntity getReceive() {
        return receive;
    }
}
