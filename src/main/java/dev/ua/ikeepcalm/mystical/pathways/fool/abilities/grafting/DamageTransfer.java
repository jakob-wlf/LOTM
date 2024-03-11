package dev.ua.ikeepcalm.mystical.pathways.fool.abilities.grafting;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
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
    private final int maxTimesReceived;

    public DamageTransfer(LivingEntity receive, LivingEntity target, ArrayList<DamageTransfer> damageTransfers, boolean npc) {
        this.receive = receive;
        this.target = target;

        timesReceived = 0;
        maxTimesReceived = npc ? 3 : 10;

        LordOfTheMinecraft.instance.getServer().getPluginManager().registerEvents(this, LordOfTheMinecraft.instance);

        DamageTransfer instance = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (timesReceived >= maxTimesReceived || !target.isValid() || !receive.isValid()) {
                    damageTransfers.remove(instance);
                    cancel();
                }
            }
        }.runTaskTimer(LordOfTheMinecraft.instance, 0, 0);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageEvent e) {
        if (timesReceived >= maxTimesReceived || e.getEntity() != receive || !target.isValid() || !receive.isValid())
            return;

        timesReceived++;
        if (e instanceof EntityDamageByEntityEvent event)
            target.damage(e.getDamage(), event.getDamager());
        else
            target.damage(e.getDamage());
        e.setCancelled(true);
    }

    public LivingEntity getReceive() {
        return receive;
    }
}
