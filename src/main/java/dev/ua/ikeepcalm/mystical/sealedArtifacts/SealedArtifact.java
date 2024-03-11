package dev.ua.ikeepcalm.mystical.sealedArtifacts;

import dev.ua.ikeepcalm.utils.AbilityInitHandUtil;
import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.utils.GeneralPurposeUtil;
import dev.ua.ikeepcalm.mystical.NpcAbility;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.NegativeEffects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.List;

public class SealedArtifact implements Listener {

    private ItemStack item;
    private final int pathway;
    private final NpcAbility ability;

    public SealedArtifact(Material material, String name, int pathway, NpcAbility ability, NegativeEffects negativeEffects, boolean negativeEffect) {

        this.pathway = pathway;
        this.ability = ability;

        LordOfTheMinecraft.instance.getServer().getPluginManager().registerEvents(this, LordOfTheMinecraft.instance);

        createItem(material, name);

        if (negativeEffect)
            negativeEffects.addEffectToArtifact(this, getSequence());
    }

    private void createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        String colorPrefix = AbilityInitHandUtil.getColorPrefix().get(pathway);
        String pathwayName = GeneralPurposeUtil.capitalize(AbilityInitHandUtil.getPathwayNames().get(pathway));

        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(colorPrefix + name);
        itemMeta.setLore(
                List.of(
                        "",
                        "§7Ability: " + colorPrefix + ability.getClass().getSimpleName().replaceAll("(.)([A-Z])", "$1 $2"),
                        "§7---------------------------------------",
                        colorPrefix + pathwayName + " (" + ability.getSequence() + ")"
                )
        );
        item.setItemMeta(itemMeta);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL)
            return;

        if (!item.isSimilar(e.getItem()))
            return;

        e.setCancelled(true);

        Vector dir = e.getPlayer().getLocation().getDirection().normalize();
        Location loc = e.getPlayer().getLocation().add(0, 1.5, 0);
        if (loc.getWorld() == null)
            return;

        outerloop:
        for (int i = 0; i < 50; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if ((!(entity instanceof Mob) && !(entity instanceof Player)) || entity == e.getPlayer())
                    continue;
                break outerloop;
            }

            loc.add(dir);

            if (loc.getBlock().getType().isSolid()) {
                break;
            }
        }

        ability.useNPCAbility(loc, e.getPlayer(), 1.7);

    }

    public int getPathway() {
        return pathway;
    }

    public int getSequence() {
        return ability.getSequence();
    }
}
