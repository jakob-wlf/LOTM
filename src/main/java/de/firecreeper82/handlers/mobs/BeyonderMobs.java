package de.firecreeper82.handlers.mobs;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
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

    private final ArrayList<CustomEntity> customEntities;

    public BeyonderMobs() {
        customEntities = new ArrayList<>();

        spawnEntity("§9Lavos Squid", "squid", 100, BeyonderItems.getLavosSquidBlood(), EntityType.SQUID, 20);
        spawnEntity("§7Hornacis Gray Mountain Goat", "goat", 35, BeyonderItems.getGoatHorn(), EntityType.GOAT, 30);
    }

    private void spawnEntity(String name, String id, int rarity, ItemStack drop, EntityType entityType, Integer health) {
        customEntities.add(new CustomEntity(name, id, rarity, drop, entityType, health));
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        for(CustomEntity customEntity : customEntities) {
            if(e.getEntity().getType() != customEntity.entityType())
                continue;

            Random random = new Random();
            if(random.nextInt(customEntity.rarity()) != 0)
                return;

            Entity entity = e.getEntity();
            entity.setCustomName(customEntity.name());

            if(entity instanceof LivingEntity livingEntity && customEntity.maxHealth() != null) {
                Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(customEntity.maxHealth());
                livingEntity.setHealth(customEntity.maxHealth());
            }

            entity.setMetadata("customEntityId", new FixedMetadataValue(Plugin.instance, customEntity.id()));
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
            if(Objects.equals(e.getEntity().getMetadata("customEntityId").get(0).value(), customEntity.id())) {
                livingEntity.getWorld().dropItem(livingEntity.getLocation(), customEntity.drop());
                break;
            }
        }
    }
}
