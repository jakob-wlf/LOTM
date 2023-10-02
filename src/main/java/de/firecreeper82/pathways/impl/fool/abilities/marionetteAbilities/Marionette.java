package de.firecreeper82.pathways.impl.fool.abilities.marionetteAbilities;

import de.firecreeper82.lotm.AbilityUtilHandler;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.NPCAbility;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Marionette implements Listener {

    private boolean isBeyonder;
    private int sequence;
    private int pathway;
    private EntityType entityType;
    private UUID ownerId;
    private String name;
    private Entity entity;
    private NPC npc;
    private SpiritBodyThreads ability;

    private boolean isAngry;
    private LivingEntity currentTarget;

    public Marionette(boolean isBeyonder, int sequence, int pathway, EntityType entityType, UUID ownerId, Location location, String name, SpiritBodyThreads ability, double health) {
        this.isBeyonder = isBeyonder;
        this.sequence = sequence;
        this.pathway = pathway;
        this.entityType = entityType;
        this.ownerId = ownerId;
        this.name = name;
        this.ability = ability;

        init();
        spawnMarionette(location, health);
        start();
    }

    private void init() {
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    private void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(getPlayer() == null || !getPlayer().isValid())
                    return;

                if(npc.getEntity() == null || !npc.getEntity().isValid()) {
                    cancel();
                    return;
                }

                if(!isAngry)
                    followPlayer();

                if(isAngry) {
                    if(currentTarget == null || !currentTarget.isValid()) {
                        isAngry = false;
                        return;
                    }

                    attackCurrentTarget();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void attackCurrentTarget() {
        if(npc.getEntity().getLocation().distance(currentTarget.getLocation()) > 150) {
            isAngry = false;
            return;
        }

        npc.getNavigator().setTarget(currentTarget, true);

        if(isBeyonder) {
            attackWithBeyonderPower();
        }

    }

    private int cooldown = 10;

    private void attackWithBeyonderPower() {
        if(pathway == -1 || sequence == -1)
            return;

        if(new Random().nextInt(cooldown) != 0)
            return;

        List<NPCAbility> abilities = AbilityUtilHandler.getAllAbilitiesUpToSequence(pathway, sequence);
        NPCAbility ability = abilities.get(new Random().nextInt(abilities.size()));

        ability.useNPCAbility(currentTarget.getLocation(), getEntity(), 3);
        cooldown = Math.round(100f / ability.getSequence() * 5f);
    }

    private void followPlayer() {
        if(npc.getEntity().getLocation().distance(getPlayer().getLocation()) > 75)
            npc.getEntity().teleport(getPlayer());

        npc.getNavigator().setTarget(getPlayer(), false);
    }

    private void spawnMarionette(Location location, double health) {
        npc = CitizensAPI.getNPCRegistry().createNPC(entityType, name);

        if(entityType == EntityType.PLAYER)
            npc.addTrait(getSkinTrait());

        npc.setProtected(false);
        npc.spawn(location);

        entity = npc.getEntity();
        Objects.requireNonNull(((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
    }

    private static SkinTrait getSkinTrait() {
        SkinTrait skinTrait = new SkinTrait();
        skinTrait.setTexture("ewogICJ0aW1lc3RhbXAiIDogMTY4NDI1ODY0NTIyMywKICAicHJvZmlsZUlkIiA6ICJkOGNkMTNjZGRmNGU0Y2IzODJmYWZiYWIwOGIyNzQ4OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJaYWNoeVphY2giLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBiMTIxNzE1MjIxMjhmZGQ0YTZjYzFlMDRlMDZmNzk0ZmMxZTc5N2I2YmM2MGQ0N2E2NGMzYmZiMGEyM2VjMSIKICAgIH0KICB9Cn0=", "AoRkj9+lOTkFAFLOC9mY3zvLIooEINec6S2DKrO6om5Pq5CxJ289znUeZkKuxa5XbvlPr2FpwY2siaNMeQcJQwrBnv+8fDua4BR2dBEKge7w/4E3WHttn+hJsr6CGQ2j2FYlNMcnpJ6n/HNws58APBW2BhRwqIU6upH6p8XTmedIlCQHO1hv6pi9D82pF7eAn6dLuyKCYeDUZ9uPrOTZ9PUviQwqMFJn6Z+gAtN1Sg+tTXkEitp+HAvfYd7E71NQNFC7A35Bh2f6dlsjHgpESV5rWBtv9DN0tYtTF68CaNZUzSl5Wt+4U5mcIQ6zfNnlBIWe1eXYN5zSXsEQMnYwDAmPhWPnjD6L7TkWBIvqdh9YfGxbUyqRbzMVL0VrS+P6ZmTPMbPxUblFJLb95swMgpUwOQG9vcJvL/9Pp9ZpVednDk3g27Cp25QWZf1HNiIDgfrBsLEpKn2dmCFKGzCV7nlcNHqpnXCThSJZB/ALN2vTxF1MOJ22AIOAffHUCKARUZhxAVISYx1ZDr+FABAi6wHtyF5fJamgSVfoNnmCRvNCc0pzl9WEU1I66ddfwlcS2bHWdAsjq+xayftTGCDHIOk1w8fSHhGwSUz0wMY8Wco6gYxoRJAIeZ5OfxJD5lvP99H1htPf4hioK4/b+NwAYvJoG9TjDheLjJT2o+uQbfI=");
        return skinTrait;
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
        if(e.getEntity() != entity)
            return;

        destroyMarionette();
    }

    private void destroyMarionette() {
        ability.removeMarionette(this);
        npc.destroy();
    }

    @EventHandler
    public void onDamageToPlayer(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof LivingEntity attacked) || !(e.getDamager() instanceof LivingEntity attacker))
            return;

        if(e.getEntity() == getPlayer()) {
            isAngry = true;
            currentTarget = attacker;
        }
        if(e.getDamager() == getPlayer()) {
            isAngry = true;
            currentTarget = attacked;
        }
    }
}
