package de.firecreeper82.pathways.impl.fool.marionettes;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;

public class Marionette implements Listener {

    private Mob entity;
    private Pathway pathway;
    private EntityType type;

    private boolean active;
    private boolean alive;

    private Team team;

    private boolean beingControlled;

    private Mob currentTarget;

    private final EntityType[] rangedEntities;

    public Marionette(EntityType entityType, Location loc, Pathway pathway) {

        rangedEntities = new EntityType[]{
                EntityType.WITHER,
                EntityType.GHAST,
                EntityType.EVOKER,
                EntityType.WITCH,
                EntityType.BLAZE,
                EntityType.BLAZE,
                EntityType.BEE
        };

        World world = loc.getWorld();
        if (world == null)
            return;
        Entity e = world.spawnEntity(loc, entityType);
        if (!(e instanceof Mob livingEntity))
            return;

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        this.entity = livingEntity;
        this.pathway = pathway;
        this.type = entityType;

        team = pathway.getBeyonder().getTeam();
        team.addEntry(entity.getUniqueId().toString());

        active = true;
        alive = true;

        entity.setRemoveWhenFarAway(false);
        entity.setCustomName("§5" + pathway.getBeyonder().getPlayer().getName() + "'s Marionette");

        pathway.getBeyonder().getMarionettes().add(this);
        pathway.getBeyonder().getMarionetteEntities().add(entity);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (currentTarget != null && !currentTarget.isValid())
                    currentTarget = null;

                if (!active)
                    return;

                if (!alive)
                    cancel();

                if (beingControlled) {
                    entity.setTarget(null);
                    return;
                }

                if (currentTarget == entity || pathway.getBeyonder().getMarionetteEntities().contains(currentTarget))
                    currentTarget = null;

                if (currentTarget == null && pathway.getBeyonder().getPlayer().getLocation().distance(entity.getLocation()) > 8) {
                    if (!Arrays.asList(rangedEntities).contains(entity.getType())) {
                        entity.setTarget(pathway.getBeyonder().getPlayer());
                        entity.setAware(true);
                    } else {
                        if (pathway.getBeyonder().getPlayer().getLocation().distance(entity.getLocation()) > 10)
                            entity.setVelocity(pathway.getBeyonder().getPlayer().getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.25));
                        entity.setTarget(null);
                        entity.setAware(false);
                    }

                } else if (currentTarget != null) {
                    entity.setTarget(currentTarget);
                    entity.setAware(true);
                } else {
                    entity.setTarget(null);
                    entity.setAware(false);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        if (e.getEntity() == entity && entity.getTarget() == pathway.getBeyonder().getPlayer())
            e.setCancelled(true);
    }


    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent e) {
        Player p = pathway.getBeyonder().getPlayer();

        if (e.getEntity() instanceof Mob m && e.getDamager() == entity && pathway.getBeyonder().getMarionetteEntities().contains(m))
            e.setCancelled(true);

        if (e.getEntity() instanceof Mob m && m == entity && e.getDamager() == p) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() == p && e.getDamager() == entity)
            e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() != entity)
            return;

        alive = false;
        active = false;
        pathway.getBeyonder().getMarionetteEntities().remove(entity);
        pathway.getBeyonder().getMarionettes().remove(this);
    }


    @EventHandler
    public void onTargetEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p) || p != pathway.getBeyonder().getPlayer() || !(e.getEntity() instanceof Mob ent))
            return;

        currentTarget = ent;
    }

    public void removeEntity() {
        pathway.getBeyonder().getMarionetteEntities().remove(entity);
        active = false;
        team.removeEntry(entity.getUniqueId().toString());
        entity.remove();
    }

    public void respawnEntity() {
        active = true;

        Location loc = pathway.getBeyonder().getPlayer().getLocation();
        World world = loc.getWorld();
        if (world == null)
            return;
        Entity e = world.spawnEntity(loc, type);
        if (!(e instanceof LivingEntity livingEntity))
            return;

        this.entity = (Mob) livingEntity;
        entity.setRemoveWhenFarAway(false);
        entity.setCustomName("§5" + pathway.getBeyonder().getPlayer().getName() + "'s Marionette");

        team = pathway.getBeyonder().getTeam();
        team.addEntry(entity.getUniqueId().toString());
    }

    public boolean isActive() {
        return active;
    }

    public EntityType getType() {
        return type;
    }

    public Mob getEntity() {
        if (!active)
            return null;
        return entity;
    }

    public void setBeingControlled(boolean beingControlled) {
        this.beingControlled = beingControlled;

        entity.setAware(!beingControlled);
    }

    public boolean isBeingControlled() {
        return beingControlled;
    }

    public boolean isAlive() {
        return alive;
    }
}
