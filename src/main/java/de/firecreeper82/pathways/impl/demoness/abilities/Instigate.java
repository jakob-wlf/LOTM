package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Instigate extends Ability {

    private boolean isInstigating;
    private Mob attacker;

    public Instigate(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        isInstigating = false;
        attacker = null;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null)
            return;

        LivingEntity target = null;

        outerloop:
        for (int i = 0; i < 25; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (!(entity instanceof LivingEntity e) || entity == p)
                    continue;
                target = e;
                break outerloop;
            }

            loc.add(dir);
        }

        if (target == null) {
            p.sendMessage("§cCouldn't find the target!");
            return;
        }

        if (!isInstigating) {
            if (!(target instanceof Mob mob)) {
                p.sendMessage("§cThat entity can't be instigated!");
                return;
            }
            isInstigating = true;
            p.sendMessage("§aInstigating " + mob.getName() + "!");
            p.sendMessage("§aChoose target");
            attacker = mob;
            return;
        }

        isInstigating = false;
        if (attacker == null) {
            p.sendMessage("§cSomething went wrong!");
            return;
        }

        attacker.setTarget(target);
        p.sendMessage("§a" + target.getName() + " is being targeted by " + attacker.getName() + "!");
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.STONE_SWORD, "Instigate", "65", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
