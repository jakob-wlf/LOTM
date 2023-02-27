package de.firecreeper82.lotm.util;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;

public class TutNetworkManager extends Connection {
    public TutNetworkManager(PacketFlow enumprotocoldirection) {
        super(enumprotocoldirection);
    }
}