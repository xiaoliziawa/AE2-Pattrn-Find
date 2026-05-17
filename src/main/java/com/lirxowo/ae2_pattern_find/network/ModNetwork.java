package com.lirxowo.ae2_pattern_find.network;

import com.lirxowo.ae2_pattern_find.AE2PatternFind;
import com.lirxowo.ae2_pattern_find.network.c2s.LocatePatternC2SPacket;
import com.lirxowo.ae2_pattern_find.network.s2c.LocateResultS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModNetwork {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(AE2PatternFind.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals);

    private static int nextId = 0;

    public static void register() {
        CHANNEL.messageBuilder(LocatePatternC2SPacket.class, nextId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(LocatePatternC2SPacket::encode)
                .decoder(LocatePatternC2SPacket::decode)
                .consumerMainThread(LocatePatternC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(LocateResultS2CPacket.class, nextId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(LocateResultS2CPacket::encode)
                .decoder(LocateResultS2CPacket::decode)
                .consumerMainThread(LocateResultS2CPacket::handle)
                .add();
    }

    private ModNetwork() {}
}
