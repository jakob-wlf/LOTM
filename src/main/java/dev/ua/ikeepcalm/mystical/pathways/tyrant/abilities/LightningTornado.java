package dev.ua.ikeepcalm.mystical.pathways.tyrant.abilities;

import dev.ua.ikeepcalm.mystical.parents.Items;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.NpcAbility;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.tyrant.TyrantItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class LightningTornado extends NpcAbility {

    private final boolean npc;

    public LightningTornado(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if (!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        if (!npc)
            p = pathway.getBeyonder().getPlayer();

        this.npc = npc;
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
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        if (!(caster instanceof LivingEntity livingEntity))
            return;
        new dev.ua.ikeepcalm.mystical.pathways.disasters.LightningTornado(livingEntity, npc).spawnDisaster(livingEntity, loc);
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.BLUE_CANDLE, "Lightning Tornado", "5000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
