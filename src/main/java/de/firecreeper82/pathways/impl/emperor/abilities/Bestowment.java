package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Bestowment extends Recordable {

    public Bestowment(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        if (!recorded) pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
        destroy(beyonder, recorded);

        //loc vars
        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        //get the looked at entity
        if (loc.getWorld() == null) return;
        //entity target vars
        LivingEntity target = null;
        Player playerTarget = null;
        //look if the entity exists and is within reach
        outerloop:
        for (int i = 0; i < 10; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (!(entity instanceof LivingEntity e) || entity == p) continue;
                target = e;
                break outerloop;
            }
            loc.add(dir);
        }
        //returns error message to player
        if (target == null) {
            p.sendMessage("§cCouldn't find the target !");
            return;
        }

        //used var in code.
        LivingEntity finalTarget = target;
        if (finalTarget instanceof Player) {
            playerTarget = (Player) finalTarget;
        }
        //I don't know what this does. Let's treat it as legacy code...
        if (!finalTarget.isValid() || !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
            pathway.getSequence().getUsesAbilities()[identifier - 1] = false;

            return;
        }

        Player finalPlayerTarget = playerTarget ;
        assert finalPlayerTarget != null;
        new BukkitRunnable() {
            int timer = 13;

            @Override
            public void run() {
                if (pathway.getBeyonder().getSpirituality() - 20000 <= 0) {
                    cancel();
                }
                // find if finalTarget is beyonder.
                if (Plugin.beyonders.containsKey(finalPlayerTarget.getUniqueId())) {
                    return;
                }
                if (timer-- == 0) {
                    cancel();
                    pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 20000);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 20);

    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.COPPER_INGOT, "Bestowment", "20000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}