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

public class BeyonderMobsHandler implements Listener {

    private final ArrayList<CustomEntity> customEntities;

    public BeyonderMobsHandler() {
        customEntities = new ArrayList<>();
        BeyonderMobs beyonderMobs = new BeyonderMobs();

        spawnEntity("§9Lavos Squid", "squid", 80, BeyonderItems.getLavosSquidBlood(), EntityType.SQUID, 20, null, null);
        spawnEntity("§7Hornacis Gray Mountain Goat", "goat", 35, BeyonderItems.getGoatHorn(), EntityType.GOAT, 30, null, null);
        spawnEntity("§0Black Patterned Panther", "panther", 45, BeyonderItems.getPanther(), EntityType.OCELOT, 60, null, null);
        spawnEntity("§5Thousand-Faced Hunter", "thousand-faced", 100, BeyonderItems.getPituitaryGland(), EntityType.PILLAGER, 70, beyonderMobs, EntityType.ILLUSIONER);
        spawnEntity("§0Human-Skinned Shadow", "shadow", 80, BeyonderItems.getShadowCharacteristic(), EntityType.ENDERMAN, 70, beyonderMobs, null);
        spawnEntity("§7Ancient Wraith", "wraith", 250, BeyonderItems.getWraithDust(), EntityType.SKELETON, 145, beyonderMobs, EntityType.VEX);
        spawnEntity("§5Six Winged Gargoyle", "gargoyle", 300, BeyonderItems.getGargoyleCrystal(), EntityType.ZOMBIE, 250, beyonderMobs, EntityType.IRON_GOLEM);
        spawnEntity("§5Bizarro Bane", "bane", 1, BeyonderItems.getBizarroEye(), EntityType.WITCH, 200, beyonderMobs, EntityType.ALLAY);
        spawnEntity("§5Spirit World Plunderer", "plunderer", 1, BeyonderItems.getPlundererBody(), EntityType.ZOMBIFIED_PIGLIN, 250, beyonderMobs, EntityType.VEX);

    }

    private void spawnEntity(String name, String id, int rarity, ItemStack drop, EntityType entityType, Integer health, BeyonderMobs beyonderMobs, EntityType spawnType) {
        customEntities.add(new CustomEntity(name, id, rarity, drop, entityType, health, beyonderMobs, spawnType));
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        for(CustomEntity customEntity : customEntities) {
            if(e.getEntity().getType() != customEntity.entityType())
                continue;

            Random random = new Random();
            if(random.nextInt(customEntity.rarity()) != 0)
                return;

            Entity entity;
            if(customEntity.spawnType() == null)
                entity = e.getEntity();
            else {
                entity = e.getEntity().getWorld().spawnEntity(e.getLocation(), customEntity.spawnType());
                e.getEntity().remove();
            }

            entity.setCustomName(customEntity.name());

            if(entity instanceof LivingEntity livingEntity && customEntity.maxHealth() != null) {
                Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(customEntity.maxHealth());
                livingEntity.setHealth(customEntity.maxHealth());
            }

            entity.setMetadata("customEntityId", new FixedMetadataValue(Plugin.instance, customEntity.id()));

            if(customEntity.beyonderMobs() != null) {
                customEntity.beyonderMobs().addMob(entity, customEntity.id());
            }
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
