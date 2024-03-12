package dev.ua.ikeepcalm.mystical.pathways.fool.abilities.marionetteAbilities;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.mystical.parents.abilitiies.Ability;
import dev.ua.ikeepcalm.mystical.parents.Items;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.fool.FoolItems;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MarionetteControlling extends Ability implements Listener {

    private final SpiritBodyThreads spiritBodyThreadsAbility;

    private final Particle.DustOptions dustWhite, dustBlue;

    private Marionette selectedMarionette;
    private int index;
    private boolean controlling;

    private Location playerLoc;
    private NPC fakePlayer;


    //TODO: Hiding in FOH enable controlling

    public MarionetteControlling(int identifier, Pathway pathway, int sequence, Items items, SpiritBodyThreads spiritBodyThreadsAbility) {
        super(identifier, pathway, sequence, items);

        this.spiritBodyThreadsAbility = spiritBodyThreadsAbility;

        p = pathway.getBeyonder().getPlayer();
        items.addToSequenceItems(identifier - 1, sequence);

        dustBlue = new Particle.DustOptions(Color.fromRGB(0, 128, 255), .75f);
        dustWhite = new Particle.DustOptions(Color.fromRGB(255, 255, 255), .75f);

        index = 0;

        LordOfTheMinecraft.instance.getServer().getPluginManager().registerEvents(this, LordOfTheMinecraft.instance);
        controlling = false;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if (controlling) {
            stopControlling();
            return;
        }

        if (selectedMarionette == null)
            return;

        if (p.isSneaking()) {
            Location playerLoc = p.getLocation();
            p.teleport(selectedMarionette.getEntity());
            selectedMarionette.getEntity().teleport(playerLoc);
            return;
        }

        playerLoc = p.getLocation();
        controlling = true;

        p.teleport(selectedMarionette.getEntity());
        p.setInvisible(true);
        p.setInvulnerable(true);

        selectedMarionette.setBeingControlled(true);
        selectedMarionette.setMarionetteControllingAbility(this);

        fakePlayer = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, p.getName());
        fakePlayer.setProtected(false);
        fakePlayer.spawn(playerLoc);

    }

    @EventHandler
    public void onFakePlayerDamage(EntityDamageEvent e) {
        if (!controlling)
            return;

        if (fakePlayer == null)
            return;

        if (!fakePlayer.isSpawned())
            return;

        if (e.getEntity() == fakePlayer.getEntity()) {
            stopControlling();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (!controlling)
            return;

        if(selectedMarionette == null)
            return;

        if (e.getPlayer() != p)
            return;

        if(p.getNearbyEntities(5, 5, 5).stream().anyMatch(entity -> entity instanceof LivingEntity && entity != selectedMarionette.getEntity()))
            return;

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location startLoc = p.getEyeLocation();

        if (startLoc.getWorld() == null)
            return;

        e.setCancelled(true);

        for (int i = 0; i < 100; i++) {
            if (
                    startLoc.getBlock().getType().isSolid() ||
                    startLoc.getWorld()
                            .getNearbyEntities(startLoc, 1, 1, 1)
                            .stream()
                            .anyMatch(entity -> entity != p && entity != selectedMarionette.getEntity() && entity instanceof LivingEntity)
            ) {
                break;
            }

            startLoc.add(dir);
        }

        selectedMarionette.attackWithBeyonderPower(startLoc);
    }

    public void stopControlling() {
        controlling = false;
        if (playerLoc != null)
            p.teleport(playerLoc);

        if (selectedMarionette.isAlive()) {
            selectedMarionette.setBeingControlled(false);
        }

        p.setInvisible(false);
        p.setInvulnerable(false);

        fakePlayer.destroy();
    }

    @Override
    public void leftClick() {
        p = pathway.getBeyonder().getPlayer();

        if (controlling)
            return;

        if (p.isSneaking()) {
            if (selectedMarionette == null)
                return;
            selectedMarionette.setShouldFollow(!selectedMarionette.shouldFollow());

            p.sendMessage("Set should follow to " + selectedMarionette.shouldFollow());
            return;
        }

        if (spiritBodyThreadsAbility.getMarionettes().isEmpty()) {
            index = 0;
            return;
        }

        index++;

        if (index >= spiritBodyThreadsAbility.getMarionettes().size())
            index = 0;

        selectedMarionette = spiritBodyThreadsAbility.getMarionettes().get(index);
    }

    @Override
    public void onHold() {
        if (controlling)
            return;

        if (selectedMarionette == null || !selectedMarionette.isAlive()) {
            selectedMarionette = null;
            index = 0;
            if (!spiritBodyThreadsAbility.getMarionettes().isEmpty())
                selectedMarionette = spiritBodyThreadsAbility.getMarionettes().get(index);
            return;
        }

        for (Marionette marionette : spiritBodyThreadsAbility.getMarionettes()) {
            if (marionette.getEntity() == null) {
                marionette.destroyMarionette();
                continue;
            }
            if (marionette.getEntity().getWorld() != p.getWorld())
                continue;

            if (marionette == selectedMarionette)
                drawLineToEntity(p.getEyeLocation(), marionette.getEntity().getLocation(), dustWhite);
            else
                drawLineToEntity(p.getEyeLocation(), marionette.getEntity().getLocation(), dustBlue);
        }
    }

    private void drawLineToEntity(Location startLoc, Location target, Particle.DustOptions dust) {
        Location loc = startLoc.clone();
        Vector dir = target
                .toVector()
                .subtract(loc.toVector())
                .normalize()
                .multiply(.75);

        for (int i = 0; i < target.distance(startLoc); i++) {
            p.spawnParticle(
                    Particle.REDSTONE,
                    loc,
                    1,
                    0,
                    0,
                    0,
                    dust
            );
            loc.add(dir);
        }
    }

    @EventHandler
    public void onEntityDamageByPlayerWhileControlling(EntityDamageByEntityEvent e) {
        if(!controlling)
            return;

        p = pathway.getBeyonder().getPlayer();

        if(e.getDamager() != p)
            return;

        if(selectedMarionette.getEntity() != e.getEntity())
            selectedMarionette.attackMob(e.getEntity(), 4);

        e.setCancelled(true);
    }


    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.STRING, "Marionette Controlling", "none", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
