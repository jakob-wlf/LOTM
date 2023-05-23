package de.firecreeper82.lotm.util.npc;

import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@TraitName("removeOnDamage")
public class RemoveOnDamageTrait extends Trait {
    public RemoveOnDamageTrait() {
        super("removeOnDamage");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!npc.isSpawned())
            return;

        if (e.getEntity() == npc.getEntity())
            npc.destroy();
    }
}
