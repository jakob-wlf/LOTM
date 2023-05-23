package de.firecreeper82.lotm.util;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class TutNetHandler extends ServerGamePacketListenerImpl {
    public TutNetHandler(MinecraftServer minecraftserver, Connection networkmanager, ServerPlayer entityplayer) {
        super(minecraftserver, networkmanager, entityplayer);
    }

    @Override
    public void send(Packet<?> packet) {
    }
}