package de.firecreeper82.handlers.mobs;

import de.firecreeper82.handlers.mobs.abilities.*;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.pathways.MobUsableAbility;
import org.bukkit.Location;
import org.bukkit.World;
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

        spawnEntity("§9Lavos Squid", "squid", 50, BeyonderItems.getLavosSquidBlood(), EntityType.SQUID, 20, null, null, "none", true);
        spawnEntity("§7Hornacis Gray Mountain Goat", "goat", 18, BeyonderItems.getGoatHorn(), EntityType.GOAT, 30, null, null, "none", true);
        spawnEntity("§0Black Patterned Panther", "panther", 28, BeyonderItems.getPanther(), EntityType.OCELOT, 60, null, null, "none", true);
        spawnEntity("§5Thousand-Faced Hunter", "thousand-faced", 55, BeyonderItems.getPituitaryGland(), EntityType.PILLAGER, 70, beyonderMobs, EntityType.ILLUSIONER, "none", true);
        spawnEntity("§0Human-Skinned Shadow", "shadow", 35, BeyonderItems.getShadowCharacteristic(), EntityType.ENDERMAN, 70, beyonderMobs, null, "none", true);
        spawnEntity("§7Ancient Wraith", "wraith", 120, BeyonderItems.getWraithDust(), EntityType.SKELETON, 145, beyonderMobs, EntityType.VEX, "wraith", true);
        spawnEntity("§5Six Winged Gargoyle", "gargoyle", 180, BeyonderItems.getGargoyleCrystal(), EntityType.ZOMBIE, 250, beyonderMobs, EntityType.IRON_GOLEM, "gargoyle", true);
        spawnEntity("§5Bizarro Bane", "bane", 40, BeyonderItems.getBizarroEye(), EntityType.WITCH, 200, beyonderMobs, EntityType.ALLAY, "bane", true, new BaneAbility(20));
        spawnEntity("§5Spirit World Plunderer", "plunderer", 300, BeyonderItems.getPlundererBody(), EntityType.ZOMBIFIED_PIGLIN, 250, beyonderMobs, EntityType.VEX, "plunderer", false, new SpawnVex(70), new PlundererAbility(35));
        spawnEntity("§5Hound of Fulgrim", "wolf", 85, BeyonderItems.getWolfEye(), EntityType.WOLF, 750, beyonderMobs, null, "wolf", true);
        spawnEntity("§5Demonic Wolf of Fog", "fog-wolf", 75, BeyonderItems.getWolfHeart(), EntityType.FOX, 750, beyonderMobs, EntityType.WOLF, "fog-wolf", true);

        spawnEntity("§4Magma Titan", "magma-titan", 25, BeyonderItems.getMagmaHeart(), EntityType.MAGMA_CUBE, 32, null, null, "none", true);
        spawnEntity("§6Dawn Rooster", "rooster", 85, BeyonderItems.getRedRoosterComb(), EntityType.CHICKEN, 60, beyonderMobs, null, "rooster", true, new RoosterAbility(60));
        spawnEntity("§6Divine Bird", "divine-bird", 185, BeyonderItems.getTailFeather(), EntityType.COW, 85, beyonderMobs, EntityType.PARROT, "divine-bird", true, new HolyLightSummoning(90), new FlaringSun(350));

        spawnEntity("§bSpirit Eater", "eater", 120, BeyonderItems.getSpiritPouch(), EntityType.ZOMBIFIED_PIGLIN, 30, beyonderMobs, EntityType.ALLAY, "eater", true);

    }

    private void spawnEntity(String name, String id, int rarity, ItemStack drop, EntityType entityType, Integer health, BeyonderMobs beyonderMobs, EntityType spawnType, String particle, boolean repeatingParticles, MobUsableAbility... abilities) {
        customEntities.add(new CustomEntity(name, id, rarity, drop, entityType, health, beyonderMobs, spawnType, particle, repeatingParticles, abilities));
    }

    public boolean spawnEntity(String id, Location location, World world) {
        for(CustomEntity customEntity : customEntities) {
            if(!customEntity.id().equalsIgnoreCase(id))
                continue;

            Entity entity = customEntity.spawnType() == null ? world.spawnEntity(location, customEntity.entityType()) : world.spawnEntity(location, customEntity.spawnType());
            entity.setCustomName(customEntity.name());

            if(entity instanceof LivingEntity livingEntity && customEntity.maxHealth() != null) {
                Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(customEntity.maxHealth());
                livingEntity.setHealth(customEntity.maxHealth());
            }

            entity.setMetadata("customEntityId", new FixedMetadataValue(Plugin.instance, customEntity.id()));

            if(customEntity.beyonderMobs() != null) {
                customEntity.beyonderMobs().addMob(entity, customEntity);
            }
            return true;
        }
        return false;
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
                customEntity.beyonderMobs().addMob(entity, customEntity);
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

    public ArrayList<CustomEntity> getCustomEntities() {
        return customEntities;
    }
}
