package dev.ua.ikeepcalm.mystical.pathways.tyrant.abilities;

import dev.ua.ikeepcalm.mystical.parents.Items;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.NpcAbility;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.tyrant.TyrantItems;
import dev.ua.ikeepcalm.mystical.pathways.tyrant.TyrantSequence;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Lightning extends NpcAbility {

    boolean destruction;
    private final boolean npc;

    public Lightning(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if (!npc)
            p = pathway.getBeyonder().getPlayer();

        if (!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        this.npc = npc;
        destruction = true;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        Vector dir = p.getLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null)
            return;

        outerloop:
        for (int i = 0; i < 80; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (entity.getType() == EntityType.ARMOR_STAND || entity == p)
                    continue;
                break outerloop;
            }

            loc.add(dir);

            if (loc.getBlock().getType().isSolid()) {
                break;
            }
        }

        useNPCAbility(loc, p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.LIGHT_BLUE_DYE, "Lightning", "200", identifier, sequence, p.getName());
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        if (loc.getWorld() == null)
            return;

        Integer sequence = npc ? null : pathway.getSequence().getCurrentSequence();
        TyrantSequence.spawnLighting(loc, caster, multiplier, npc, destruction, sequence);
    }

    @Override
    public void leftClick() {
        destruction = !destruction;
        p.sendMessage("§aSet destruction to: §7" + destruction);
    }
}
