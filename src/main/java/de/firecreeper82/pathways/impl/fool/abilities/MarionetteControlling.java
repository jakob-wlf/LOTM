package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import de.firecreeper82.pathways.impl.fool.marionettes.Marionette;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MarionetteControlling extends Ability {

    int currentIndex;

    public MarionetteControlling(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        currentIndex = 0;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if(pathway.getBeyonder().getMarionettes().isEmpty())
            return;

        Marionette marionette = pathway.getBeyonder().getMarionettes().get(currentIndex);
        if(marionette.isActive())
            marionette.removeEntity();
        else
            marionette.respawnEntity();
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.TRIPWIRE_HOOK, "Marionette Controlling", "None", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void leftClick() {
        if(pathway.getBeyonder().getMarionettes().isEmpty())
            return;
        if(currentIndex == pathway.getBeyonder().getMarionettes().size() - 1)
            currentIndex = 0;
        else
            currentIndex++;
    }

    @Override
    public void onHold() {
        p = pathway.getBeyonder().getPlayer();

        if(pathway.getBeyonder().getMarionettes().isEmpty()) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cYou don't have any Marionettes!"));
            return;
        }

        while(currentIndex >= pathway.getBeyonder().getMarionettes().size()) {
            currentIndex--;
        }

        Marionette marionette = pathway.getBeyonder().getMarionettes().get(currentIndex);

        String entityName = pathway.getBeyonder().getMarionettes().get(currentIndex).getType().name().substring(0, 1).toUpperCase() + pathway.getBeyonder().getMarionettes().get(currentIndex).getType().name().substring(1).toLowerCase();
        String status = marionette.isActive() ? "despawn" : "respawn";
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected: §7" + entityName + " §5-- §7Right-Click §5to " + status));
    }
}
