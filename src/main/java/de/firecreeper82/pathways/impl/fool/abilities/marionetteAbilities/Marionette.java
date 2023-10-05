package de.firecreeper82.pathways.impl.fool.abilities.marionetteAbilities;

import de.firecreeper82.lotm.AbilityUtilHandler;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.NPCAbility;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.WitherTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Marionette implements Listener {

    private final boolean isBeyonder;
    private final int sequence;
    private final int pathway;
    private final EntityType entityType;
    private final UUID ownerId;
    private final String name;
    private Entity entity;
    private NPC npc;
    private final SpiritBodyThreads ability;
    private MarionetteControlling marionetteControllingAbility;

    private boolean isAngry;
    private boolean alive;
    private LivingEntity currentTarget;
    private int cooldown = 10;

    private boolean shouldFollow;
    private boolean isBeingControlled;


    public Marionette(boolean isBeyonder, int sequence, int pathway, EntityType entityType, UUID ownerId, Location location, String name, SpiritBodyThreads ability, double health) {
        this.isBeyonder = isBeyonder;
        this.sequence = sequence;
        this.pathway = pathway;
        this.entityType = entityType;
        this.ownerId = ownerId;
        this.name = name == null ? "" : name;
        this.ability = ability;

        shouldFollow = true;

        isBeingControlled = false;

        init();
        spawnMarionette(location, health);
        start();
    }

    private static SkinTrait getSkinTrait() {
        SkinTrait skinTrait = new SkinTrait();
        skinTrait.setTexture("ewogICJ0aW1lc3RhbXAiIDogMTY4NDI1ODY0NTIyMywKICAicHJvZmlsZUlkIiA6ICJkOGNkMTNjZGRmNGU0Y2IzODJmYWZiYWIwOGIyNzQ4OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJaYWNoeVphY2giLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBiMTIxNzE1MjIxMjhmZGQ0YTZjYzFlMDRlMDZmNzk0ZmMxZTc5N2I2YmM2MGQ0N2E2NGMzYmZiMGEyM2VjMSIKICAgIH0KICB9Cn0=", "AoRkj9+lOTkFAFLOC9mY3zvLIooEINec6S2DKrO6om5Pq5CxJ289znUeZkKuxa5XbvlPr2FpwY2siaNMeQcJQwrBnv+8fDua4BR2dBEKge7w/4E3WHttn+hJsr6CGQ2j2FYlNMcnpJ6n/HNws58APBW2BhRwqIU6upH6p8XTmedIlCQHO1hv6pi9D82pF7eAn6dLuyKCYeDUZ9uPrOTZ9PUviQwqMFJn6Z+gAtN1Sg+tTXkEitp+HAvfYd7E71NQNFC7A35Bh2f6dlsjHgpESV5rWBtv9DN0tYtTF68CaNZUzSl5Wt+4U5mcIQ6zfNnlBIWe1eXYN5zSXsEQMnYwDAmPhWPnjD6L7TkWBIvqdh9YfGxbUyqRbzMVL0VrS+P6ZmTPMbPxUblFJLb95swMgpUwOQG9vcJvL/9Pp9ZpVednDk3g27Cp25QWZf1HNiIDgfrBsLEpKn2dmCFKGzCV7nlcNHqpnXCThSJZB/ALN2vTxF1MOJ22AIOAffHUCKARUZhxAVISYx1ZDr+FABAi6wHtyF5fJamgSVfoNnmCRvNCc0pzl9WEU1I66ddfwlcS2bHWdAsjq+xayftTGCDHIOk1w8fSHhGwSUz0wMY8Wco6gYxoRJAIeZ5OfxJD5lvP99H1htPf4hioK4/b+NwAYvJoG9TjDheLjJT2o+uQbfI=");
        return skinTrait;
    }

    private void init() {
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    private void start() {
        new BukkitRunnable() {
            int regenCounter = 40;

            @Override
            public void run() {
                if(!alive) {
                    cancel();
                    return;
                }

                if (getPlayer() == null || !getPlayer().isValid()) {
                    destroyMarionette();
                    cancel();
                    return;
                }

                if (getEntity()== null || !getEntity().isValid()) {
                    destroyMarionette();
                    cancel();
                    return;
                }

                if(isBeingControlled) {
                    playerControlled();
                    return;
                }

                if (!isAngry)
                    followPlayer();

                if (isAngry) {
                    if(currentTarget.hasMetadata("isBeingControlled"))
                        currentTarget = null;
                    if (currentTarget == null || !currentTarget.isValid()) {
                        isAngry = false;
                        return;
                    }

                    attackCurrentTarget();
                }

                regenCounter--;
                if (regenCounter == 0) {
                    regenCounter = 40;

                    AttributeInstance maxHealthAttribute = ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    if (maxHealthAttribute != null && ((LivingEntity) getEntity()).getHealth() < maxHealthAttribute.getBaseValue() - 1)
                        ((LivingEntity) getEntity()).setHealth(((LivingEntity) getEntity()).getHealth() + 1);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void attackCurrentTarget() {
        if(getEntity().getLocation().getWorld() != currentTarget.getLocation().getWorld())
            return;
        if (getEntity().getLocation().distance(currentTarget.getLocation()) > 150) {
            isAngry = false;
            return;
        }

        npc.getNavigator().setTarget(currentTarget, true);

        attackWithBeyonderPower(currentTarget.getLocation());
    }

    public void attackMob(Entity entity, double damage) {
        if(getEntity() instanceof LivingEntity livingEntity && livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
            livingEntity.attack(entity);
        else if (entity instanceof LivingEntity livingEntity)
            livingEntity.damage(damage, getEntity());
    }

    public void attackWithBeyonderPower(Location location) {
        if ((pathway == -1 || sequence == -1 ) && isBeyonder )
            return;

        if(!isBeyonder && isBeingControlled)
            return;

        if (new Random().nextInt(cooldown) != 0 && !isBeingControlled)
            return;

        if(!isBeyonder && ability.getPathway().getSequence().getCurrentSequence() >= 5)
            return;

        List<NPCAbility> abilities;
        if ((!isBeyonder || new Random().nextBoolean()) && !isBeingControlled) {
            abilities = AbilityUtilHandler.getAllAbilitiesUpToSequence(1, ability.getPathway().getSequence().getCurrentSequence());
        } else {
            abilities = AbilityUtilHandler.getAllAbilitiesUpToSequence(pathway, sequence);
        }

        if(abilities.isEmpty())
            return;
        NPCAbility usedAbility = abilities.get(new Random().nextInt(abilities.size()));
        usedAbility.useNPCAbility(location, getEntity(), 3);
        cooldown = Math.round(60f / usedAbility.getSequence() * 5f);
    }

    private void followPlayer() {
        if(getEntity().getWorld() != getPlayer().getWorld())
            return;

        if(!shouldFollow) {
            npc.getNavigator().cancelNavigation();
            return;
        }

        if (getEntity().getLocation().distance(getPlayer().getLocation()) > 75)
            npc.getEntity().teleport(getPlayer());

        npc.getNavigator().setTarget(getPlayer(), false);
    }

    private void playerControlled() {
        npc.getNavigator().cancelNavigation();

        getPlayer().setFireTicks(0);
        getPlayer().setFreezeTicks(0);

        getEntity().teleport(getPlayer());

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(Plugin.instance, getPlayer());
        }
    }

    private void spawnMarionette(Location location, double health) {
        npc = CitizensAPI.getNPCRegistry().createNPC(entityType, name);

        if (entityType == EntityType.PLAYER)
            npc.addTrait(getSkinTrait());

        if (entityType == EntityType.WITHER)
            npc.addTrait(new WitherTrait());

        npc.setProtected(false);
        npc.spawn(location);

        entity = npc.getEntity();
        Objects.requireNonNull(((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
        ((LivingEntity) entity).setHealth(health);

        alive = true;

        if(!isBeyonder)
            npc.getEntity().setCustomNameVisible(false);
    }

    public Entity getEntity() {
        entity = npc.getEntity();
        return entity;
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(ownerId);
    }

    @EventHandler
    public void OnDeath(EntityDeathEvent e) {
        if(!alive)
            return;
        if (e.getEntity() != entity)
            return;

        destroyMarionette();
    }

    public void destroyMarionette() {
        if(isBeingControlled)
            marionetteControllingAbility.stopControlling();

        alive = false;

        getPlayer().sendMessage("Your Marionette " + name + " §rhas been killed.");

        ability.removeMarionette(this);
        npc.destroy();

        alive = false;
    }

    public void setMarionetteControllingAbility(MarionetteControlling marionetteControllingAbility) {
        this.marionetteControllingAbility = marionetteControllingAbility;
    }

    @EventHandler
    public void onDamageToPlayer(EntityDamageByEntityEvent e) {
        if(!alive)
            return;
        if (!(e.getEntity() instanceof LivingEntity attacked) || !(e.getDamager() instanceof LivingEntity attacker))
            return;

        if (e.getDamager() == getEntity() || e.getEntity() == getEntity())
            return;

        if (e.getDamager() == e.getEntity())
            return;

        if(ability.getMarionettes().stream().anyMatch(marionette -> marionette.getEntity() == e.getEntity()))
            return;

        if(isBeingControlled && e.getDamager() == getPlayer() && e.getEntity() == getEntity()) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() == getPlayer() && e.getDamager() == getEntity()) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() == getPlayer()) {
            isAngry = true;
            currentTarget = attacker;
        }
        if (e.getDamager() == getPlayer()) {
            isAngry = true;
            currentTarget = attacked;
        }
    }

    @EventHandler
    public void onMarionetteDamage(EntityDamageEvent e) {
        if(!alive)
            return;

        if(!e.getEntity().isValid())
            return;

        if(e.getEntity() != getEntity())
            return;

        EntityDamageEvent.DamageCause[] causes = {
                EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.FIRE,
                EntityDamageEvent.DamageCause.FIRE_TICK,
                EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
        };

        if(!Arrays.asList(causes).contains(e.getCause()))
            return;

        e.setCancelled(true);
    }

    public void setBeingControlled(boolean beingControlled) {
        isBeingControlled = beingControlled;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setShouldFollow(boolean shouldFollow) {
        this.shouldFollow = shouldFollow;
    }

    public boolean shouldFollow() {
        return shouldFollow;
    }
}
