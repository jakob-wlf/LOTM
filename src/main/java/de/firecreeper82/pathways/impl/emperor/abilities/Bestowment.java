package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Bestowment extends Ability {
    private boolean activated;

    public Bestowment(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null) return;

        LivingEntity target = null;

        outerloop:
        for (int i = 0; i < 10; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (!(entity instanceof LivingEntity e) || entity == p) continue;
                target = e;
                break outerloop;
            }
            loc.add(dir);
        }

        if (target == null) {
            p.sendMessage("§cCouldn't find the target!");
            return;
        }

        LivingEntity finalTarget = target;

        if (!finalTarget.isValid() || !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
            pathway.getSequence().getUsesAbilities()[identifier - 1] = false;

            return;
        }

        new BukkitRunnable() {
            int timer = 13;

            @Override
            public void run() {
                if (pathway.getBeyonder().getSpirituality() - 20000 <= 0){
                    cancel();
                }
                pathway.getPathway().getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 20000);
                if (finalTarget instanceof Player && Plugin.beyonders.containsKey(finalTarget)) {
                    Plugin.beyonders.get(finalTarget).looseControl(95, 2);
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