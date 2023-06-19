package de.firecreeper82.handlers.mobs.beyonders;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.NPCAbility;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;


public class RogueBeyonder implements Listener {

    private final boolean aggressive;
    private final int sequence;
    private final int pathway;

    private final RogueBeyonders rogueBeyonders;

    private boolean isWandering;

    private final NPC beyonder;
    private Entity entity;

    private Entity target;

    private float attackTimer;
    private boolean initHealth;

    private final double[] multiplier;

    private enum STATE {
        WANDER,
        ATTACK
    }

    private STATE state;

    private final String[][] characteristicIndex;

    private String name;

    public RogueBeyonder(boolean aggressive, int sequence, int pathway, RogueBeyonders rogueBeyonders) {
        this.aggressive = aggressive;
        this.sequence = sequence;
        this.pathway = pathway;
        this.rogueBeyonders = rogueBeyonders;

        initHealth = false;
        attackTimer = 0;

        multiplier = new double[] {
            0, 3.5, 3, 2.5, 1.9, 1.7, 1.6, 1.4, 1.3, 1
        };

        Plugin.instance.addRogueBeyonder(this);

        characteristicIndex = new String[][] {
                {"sun", "fool", "door", "demoness"},
                {"§6", "§5", "§b", "§d"}
        };

        Random random = new Random();

        name = Plugin.instance.getNames().get(random.nextInt(Plugin.instance.getNames().size()));
        name = rogueBeyonders.getColorPrefix().get(pathway) + name + " - " + sequence + " (" + aggressive + ")";

        beyonder = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        beyonder.setProtected(false);

        SkinTrait skinTrait = new SkinTrait();
        skinTrait.setTexture("ewogICJ0aW1lc3RhbXAiIDogMTY4NDI1ODY0NTIyMywKICAicHJvZmlsZUlkIiA6ICJkOGNkMTNjZGRmNGU0Y2IzODJmYWZiYWIwOGIyNzQ4OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJaYWNoeVphY2giLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBiMTIxNzE1MjIxMjhmZGQ0YTZjYzFlMDRlMDZmNzk0ZmMxZTc5N2I2YmM2MGQ0N2E2NGMzYmZiMGEyM2VjMSIKICAgIH0KICB9Cn0=", "AoRkj9+lOTkFAFLOC9mY3zvLIooEINec6S2DKrO6om5Pq5CxJ289znUeZkKuxa5XbvlPr2FpwY2siaNMeQcJQwrBnv+8fDua4BR2dBEKge7w/4E3WHttn+hJsr6CGQ2j2FYlNMcnpJ6n/HNws58APBW2BhRwqIU6upH6p8XTmedIlCQHO1hv6pi9D82pF7eAn6dLuyKCYeDUZ9uPrOTZ9PUviQwqMFJn6Z+gAtN1Sg+tTXkEitp+HAvfYd7E71NQNFC7A35Bh2f6dlsjHgpESV5rWBtv9DN0tYtTF68CaNZUzSl5Wt+4U5mcIQ6zfNnlBIWe1eXYN5zSXsEQMnYwDAmPhWPnjD6L7TkWBIvqdh9YfGxbUyqRbzMVL0VrS+P6ZmTPMbPxUblFJLb95swMgpUwOQG9vcJvL/9Pp9ZpVednDk3g27Cp25QWZf1HNiIDgfrBsLEpKn2dmCFKGzCV7nlcNHqpnXCThSJZB/ALN2vTxF1MOJ22AIOAffHUCKARUZhxAVISYx1ZDr+FABAi6wHtyF5fJamgSVfoNnmCRvNCc0pzl9WEU1I66ddfwlcS2bHWdAsjq+xayftTGCDHIOk1w8fSHhGwSUz0wMY8Wco6gYxoRJAIeZ5OfxJD5lvP99H1htPf4hioK4/b+NwAYvJoG9TjDheLjJT2o+uQbfI=");
        beyonder.addTrait(skinTrait);
        beyonder.setName(name);

        state = STATE.WANDER;

        isWandering = false;

        new BukkitRunnable() {
            @Override
            public void run() {
                RogueBeyonder.this.run();
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    int counter = 0;

    private void run() {
        if (beyonder == null || !beyonder.isSpawned())
            return;

        if(!initHealth) {
            if(beyonder.getEntity() instanceof LivingEntity livingEntity && livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(500f * (2 / (float) sequence));
                livingEntity.setHealth(Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
            }
            initHealth = true;

            entity = beyonder.getEntity();
            entity.setMetadata("Beyonder", new FixedMetadataValue(Plugin.instance, true));
            entity.setCustomName(name);
        }

        counter++;

        if(counter >= 20 * 10) {
            boolean nearbyPlayer = false;
            for(Entity nearby : entity.getNearbyEntities(150, 100, 150)) {
                if (nearby instanceof Player) {
                    nearbyPlayer = true;
                    break;
                }
            }
            if(!nearbyPlayer)
                remove();
        }

        if(beyonder.getEntity() == null)
            return;

        if(target != null) {
            if(!target.isValid())
                target = null;
            else if(target.getWorld() != beyonder.getEntity().getWorld())
                target = null;

            else if(target.getLocation().distance(beyonder.getEntity().getLocation()) > 60)
                target = null;
            else
                state = STATE.ATTACK;
        }

        else {
            if(aggressive) {
                for(Entity entity : beyonder.getEntity().getNearbyEntities(8, 8, 8)) {
                    if((!(entity instanceof Mob) && !(entity instanceof Player)) || entity.getType() == EntityType.ARMOR_STAND)
                        continue;
                    if(entity instanceof Player temp && (temp.getGameMode() == GameMode.CREATIVE || temp.getGameMode() == GameMode.SPECTATOR))
                        continue;
                    target = entity;
                    break;
                }
            }

            if(target == null)
                state = STATE.WANDER;
        }

        if (state == STATE.WANDER) {
            wanderState();
        }
        if(state == STATE.ATTACK) {
            attackState();
        }

        if(pathway == 0)
            beyonder.getEntity().setFireTicks(0);
    }

    private void attackState() {
        attackTimer--;

        beyonder.getDefaultGoalController().clear();

        if(target == null)
            return;

        beyonder.getNavigator().setTarget(target, true);

        if(attackTimer > 0)
            return;

        if(rogueBeyonders.getAbilities().get(pathway) == null)
            return;

        Random random = new Random();

        NPCAbility currentAbility = rogueBeyonders.getAbilities().get(pathway).get(random.nextInt(rogueBeyonders.getAbilities().get(pathway).size()));
        if(currentAbility.getSequence() < sequence)
            return;

        attackTimer = (60 * 20f) / ((float) currentAbility.getSequence() * 5);

        currentAbility.useNPCAbility(target.getLocation(), entity, multiplier[sequence]);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(!beyonder.isSpawned())
            return;

        if(!(e.getDamager() instanceof LivingEntity))
            return;

        if(target != null)
            return;

        if(e.getEntity() != entity)
            return;

        target = e.getDamager();
    }

    private void wanderState() {
        if (isWandering) {
            return;
        }

        isWandering = true;
        beyonder.getDefaultGoalController().clear();
        beyonder.getDefaultGoalController().addGoal(WanderGoal.builder(beyonder).build(), 1);
    }

    public void spawn(Location location) {
        beyonder.spawn(location);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() == entity) {
            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), Plugin.instance.getCharacteristic().getCharacteristic(sequence, characteristicIndex[0][pathway], characteristicIndex[1][pathway]));
            remove();
        }
    }

    public void remove() {
        final RogueBeyonder removeItem = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                Plugin.instance.removeRogueBeyonder(removeItem);
                beyonder.despawn();
                beyonder.destroy();
            }
        }.runTaskLater(Plugin.instance, 1);
    }
}
