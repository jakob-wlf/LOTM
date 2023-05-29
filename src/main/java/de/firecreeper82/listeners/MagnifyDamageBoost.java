/*package de.firecreeper82.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import de.firecreeper82.lotm.Plugin;

import static de.firecreeper82.lotm.Plugin.beyonders;

public class MagnifyDamageBoost implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        if (attacker instanceof Player && beyonders.containsKey(attacker.getUniqueId()) && beyonders.get(attacker.getUniqueId()).getPathway().toString().equals("emperor") && beyonders.get(((Player) event).getUniqueId()).damageMagnified) {
            double originalDamage = event.getDamage();
            double extraDamage = originalDamage + originalDamage * Plugin.emperorMagnifyDamage.get(attacker.getUniqueId()) / 3;
            event.setDamage(extraDamage);

        }
        if (event instanceof Player && beyonders.containsKey(((Player) event).getUniqueId()) && beyonders.get(((Player) event).getUniqueId()).getPathway().toString().equals("emperor")) {
            if (Plugin.emperorMagnifyDamageDown.containsKey(((Player) event).getUniqueId()) && beyonders.get(((Player) event).getUniqueId()).damageMagnifiedDown) {
                double originalDamage = event.getDamage();
                double reducedDamage = originalDamage - originalDamage * Plugin.emperorMagnifyDamageDown.get(((Player) event).getUniqueId()) / 3;
                event.setDamage(reducedDamage);
            }
        }
    }
}*/