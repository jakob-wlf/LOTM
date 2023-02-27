package de.firecreeper82.pathways.impl.fool.abilities;

import com.mojang.authlib.properties.Property;
import de.firecreeper82.lotm.util.NPC;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PaperSubstitute extends Ability {

    public PaperSubstitute(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        Player p = pathway.getBeyonder().getPlayer();
        Location loc = p.getLocation();

        ServerPlayer player = ((CraftPlayer) p).getHandle();
        Property property = player.getGameProfile().getProperties().get("textures").iterator().next();
        String[] skin = {
                property.getValue(),
                property.getSignature()
        };
        NPC.create(loc, p.getName(), skin);
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.ARMOR_STAND, "Paper Figurine Substitute", "35", identifier, 7, pathway.getBeyonder().getPlayer().getName());
    }
}
