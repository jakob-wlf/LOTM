package de.firecreeper82.lotm.util.mobs;

import de.firecreeper82.lotm.Plugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class BeyonderMobs implements Listener {

    final static ArrayList<CustomEntity> customEntities = new ArrayList<>();

    public static void spawnEntity(String name, String id, int rarity, ItemStack drop, EntityType entityType, Integer health) {
        customEntities.add(new CustomEntity(name, id, rarity, drop, entityType, health));
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        for(CustomEntity customEntity : customEntities) {
            if(e.getEntity().getType() != customEntity.getEntityType())
                return;

            Random random = new Random();
            if(random.nextInt(customEntity.getRarity()) != 0)
                return;

            Entity entity = e.getEntity();
            entity.setCustomName(customEntity.getName());

            if(entity instanceof LivingEntity livingEntity && customEntity.getMaxHealth() != null) {
                Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(customEntity.getMaxHealth());
                livingEntity.setHealth(customEntity.getMaxHealth());
            }

            entity.setMetadata("customEntityId", new FixedMetadataValue(Plugin.instance, customEntity.getId()));
        }
    }

    @EventHandler
    public void onEntityDie(EntityDamageEvent e) {
        if(e.getEntity().getMetadata("customEntityId").isEmpty())
            return;

        if(!(e.getEntity() instanceof LivingEntity livingEntity))
            return;

        if(livingEntity.getHealth() > e.getDamage())
            return;

        for(CustomEntity customEntity : customEntities) {
            if(Objects.equals(e.getEntity().getMetadata("customEntityId").get(0).value(), customEntity.getId())) {
                livingEntity.getWorld().dropItem(livingEntity.getLocation(), customEntity.getDrop());
                break;
            }
        }
    }
}
