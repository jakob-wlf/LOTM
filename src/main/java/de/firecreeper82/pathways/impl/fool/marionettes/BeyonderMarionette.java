package de.firecreeper82.pathways.impl.fool.marionettes;

import de.firecreeper82.handlers.mobs.CustomEntity;
import de.firecreeper82.handlers.mobs.MobParticleEffects;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.MobUsableAbility;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Random;


@SuppressWarnings("all")
public class BeyonderMarionette implements Listener {

    private Player p;
    private Pathway pathway;
    private Mob entity;

    private CustomEntity customEntity;

    private LivingEntity currentTarget;

    private boolean active;
    private boolean alive;

    private final String[] flyingEntities;
    private final String[] rangedEntities;

    public BeyonderMarionette(String id, Location location, Pathway pathway) {
        p = pathway.getBeyonder().getPlayer();

        flyingEntities = new String[]{
                "plunderer",
                "wraith",
                "bane"
        };
        rangedEntities = new String[]{
                "plunderer",
                "bane",
                "divine-bird",
                "thousand-faced"
        };

        if (p == null || location.getWorld() == null)
            return;

        this.pathway = pathway;

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        entity = null;

        for (CustomEntity customEntity : Plugin.instance.getBeyonderMobsHandler().getCustomEntities()) {
            if (!customEntity.id().equalsIgnoreCase(id))
                continue;

            this.customEntity = customEntity;

            Entity e = location.getWorld().spawnEntity(location, customEntity.spawnType());

            if (!(e instanceof Mob))
                return;

            entity = (Mob) e;

            entity.setCustomName("§5" + p.getName() + "'s Marionette");
            entity.setRemoveWhenFarAway(false);

            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(customEntity.maxHealth());
            entity.setHealth(customEntity.maxHealth());

            pathway.getBeyonder().getBeyonderMarionetteEntities().add(entity);
            pathway.getBeyonder().getBeyonderMarionettes().add(this);

            active = true;
            alive = true;
            currentTarget = null;
        }

        if (entity == null)
            return;

        Random random = new Random();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!pathway.getBeyonder().online)
                    return;

                if (!pathway.getBeyonder().isBeyonder()) {
                    cancel();
                    return;
                }

                p = pathway.getBeyonder().getPlayer();

                if (currentTarget != null && !currentTarget.isValid())
                    currentTarget = null;

                if (!active)
                    return;

                if (!alive)
                    cancel();

                if (currentTarget == entity || pathway.getBeyonder().getMarionetteEntities().contains(currentTarget))
                    currentTarget = null;

                if (currentTarget == null && (Arrays.asList(flyingEntities).contains(entity) || Arrays.asList(rangedEntities).contains(entity)) && pathway.getBeyonder().getPlayer().getLocation().distance(entity.getLocation()) > 10) {
                    if (pathway.getBeyonder().getPlayer().getLocation().distance(entity.getLocation()) > 10)
                        entity.setVelocity(pathway.getBeyonder().getPlayer().getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.25));
                    entity.setTarget(null);
                    entity.setAware(false);
                } else if (currentTarget == null && pathway.getBeyonder().getPlayer().getLocation().distance(entity.getLocation()) > 8) {
                    entity.setTarget(p);
                    entity.setAware(true);
                } else if (currentTarget == null) {
                    entity.setTarget(null);
                    entity.setAware(false);
                } else {
                    entity.setTarget(currentTarget);
                    entity.setAware(true);
                }

                //Playing the particle effect
                if (customEntity.repeatingParticles())
                    MobParticleEffects.playParticleEffect(entity.getLocation(), customEntity.particle(), entity);

                if (currentTarget == null)
                    return;

                //Use abilities
                for (MobUsableAbility ability : customEntity.abilities()) {
                    if (random.nextInt(ability.getFrequency()) != 0)
                        continue;

                    ability.useAbility(entity.getLocation(), currentTarget.getLocation(), 1, entity, currentTarget);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @EventHandler
    public void onTargetEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p) || p != pathway.getBeyonder().getPlayer() || !(e.getEntity() instanceof LivingEntity ent))
            return;

        currentTarget = ent;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() != entity)
            return;

        alive = false;
        active = false;
        pathway.getBeyonder().getBeyonderMarionetteEntities().remove(entity);
        pathway.getBeyonder().getBeyonderMarionettes().remove(this);
    }

    public void removeEntity() {
        pathway.getBeyonder().getMarionetteEntities().remove(entity);
        active = false;
        entity.remove();
    }

    public String getName() {
        return customEntity.name();
    }

    public boolean isActive() {
        return active;
    }

    public Mob getEntity() {
        return entity;
    }

    public void respawnEntity() {
        if (pathway == null)
            return;

        p = pathway.getBeyonder().getPlayer();

        if (p == null)
            return;

        Location loc = p.getLocation();
        World world = p.getWorld();

        Entity e = world.spawnEntity(loc, customEntity.spawnType());

        if (!(e instanceof Mob))
            return;

        entity = (Mob) e;

        entity.setCustomName("§5" + p.getName() + "'s Marionette");
        entity.setRemoveWhenFarAway(false);

        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(customEntity.maxHealth());
        entity.setHealth(customEntity.maxHealth());

        pathway.getBeyonder().getBeyonderMarionetteEntities().add(entity);
        pathway.getBeyonder().getBeyonderMarionettes().add(this);

        active = true;
        alive = true;
        currentTarget = null;
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
}
