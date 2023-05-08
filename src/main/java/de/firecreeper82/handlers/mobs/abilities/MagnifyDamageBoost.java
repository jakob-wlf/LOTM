package de.firecreeper82.handlers.mobs.abilities;

import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.lotm.Beyonder;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import de.firecreeper82.lotm.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MagnifyDamageBoost implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        if (attacker instanceof Player playerDamager && Plugin.beyonders.containsKey(attacker.getUniqueId()) && Plugin.beyonders.get(attacker.getUniqueId()).getPathway().toString().equals("emperor")) {
            if (Plugin.emperorMagnifyDamage.containsKey(playerDamager.getUniqueId())) {
                double originalDamage = event.getDamage();
                double extraDamage = originalDamage * Plugin.emperorMagnifyDamage.get(playerDamager) / 2;
                event.setDamage(extraDamage);
            }
        }
        if (event instanceof Player && Plugin.beyonders.containsKey(((Player) event).getUniqueId()) && Plugin.beyonders.get(((Player) event).getUniqueId()).getPathway().toString().equals("emperor")) {
            if (Plugin.emperorMagnifyDamageDown.containsKey(((Player) event).getUniqueId())) {
                double originalDamage = event.getDamage();
                double reducedDamage = originalDamage * 1 / Plugin.emperorMagnifyDamageDown.get(((Player) event).getUniqueId()) / 2;
            }
        }
    }

}