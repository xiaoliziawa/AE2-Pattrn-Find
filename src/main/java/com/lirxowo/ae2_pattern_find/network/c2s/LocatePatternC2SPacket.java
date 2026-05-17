package com.lirxowo.ae2_pattern_find.network.c2s;

import com.lirxowo.ae2_pattern_find.server.PatternLocator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record LocatePatternC2SPacket(long serverId, int slot) {

    public static void encode(LocatePatternC2SPacket pkt, FriendlyByteBuf buf) {
        buf.writeLong(pkt.serverId);
        buf.writeVarInt(pkt.slot);
    }

    public static LocatePatternC2SPacket decode(FriendlyByteBuf buf) {
        return new LocatePatternC2SPacket(buf.readLong(), buf.readVarInt());
    }

    public static void handle(LocatePatternC2SPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        var player = context.getSender();
        if (player != null) {
            PatternLocator.handleRequest(player, pkt.serverId, pkt.slot);
        }
        context.setPacketHandled(true);
    }
}
