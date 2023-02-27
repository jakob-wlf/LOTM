package de.firecreeper82.lotm.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.firecreeper82.lotm.Plugin;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NPC extends ServerPlayer {

    private NPC(MinecraftServer minecraftServer, ServerLevel worldServer, GameProfile gameProfile) {
        super(minecraftServer, worldServer, gameProfile, null);
    }

    public static void create(Location location, String name, String[] skin) {
        UUID uuid = UUID.randomUUID();
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        if(location.getWorld() == null)
            return;
        ServerLevel nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = new GameProfile(uuid, name);
        NPC npc = new NPC(nmsServer, nmsWorld, profile);
        npc.connection = new TutNetHandler(nmsServer, new TutNetworkManager(PacketFlow.CLIENTBOUND), npc);
        setSkin(npc, skin);
        setPosRot(npc, location);
        npc.getBukkitEntity().setNoDamageTicks(0);
        Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc)));
        nmsWorld.addNewPlayer(npc);
        Plugin.fakePlayers.add(uuid);
        NPC.showAll(npc, location);
    }

    private static void setSkin(ServerPlayer npc, String[] skin) {
        String texture = skin[0];
        String signature = skin[1];
        npc.getGameProfile().getProperties().put("textures", new Property("textures", texture, signature));
    }

    private static void setPosRot(ServerPlayer test, Location location) {
        test.setPos(location.getX(), location.getY(), location.getZ());
        test.setXRot(location.getYaw());
        test.setYRot(location.getPitch());
    }

    public static void showAll(ServerPlayer entityPlayer, Location location) {
        ClientboundPlayerInfoPacket playerInfoAdd = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER);
        ClientboundAddPlayerPacket namedEntitySpawn = new ClientboundAddPlayerPacket(entityPlayer);
        ClientboundRotateHeadPacket headRotation = new ClientboundRotateHeadPacket(entityPlayer, (byte) ((location.getYaw() * 256f) / 360f));
        ClientboundPlayerInfoPacket playerInfoRemove = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
            connection.send(playerInfoAdd);
            connection.send(namedEntitySpawn);
            connection.send(headRotation);
            connection.send(playerInfoRemove);
        }
        entityPlayer.getEntityData().
                set(net.minecraft.world.entity.player.Player.DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0xFF);
    }
}