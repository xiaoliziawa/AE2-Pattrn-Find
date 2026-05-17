package com.lirxowo.ae2_pattern_find.client;

import com.lirxowo.ae2_pattern_find.Config;
import com.lirxowo.ae2_pattern_find.network.s2c.LocateResultS2CPacket;
import net.minecraft.client.Minecraft;

public final class ClientPacketHandler {

    public static void onLocateResult(LocateResultS2CPacket pkt) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;

        long expire = level.getGameTime() + Config.HIGHLIGHT_TICKS.get();
        MarkerStore.add(new PatternLocationMarker(
                pkt.serverId(),
                pkt.slot(),
                pkt.pos(),
                pkt.dimension(),
                pkt.pattern(),
                pkt.icon(),
                expire));

        var mc = Minecraft.getInstance();
        if (mc.screen != null) {
            mc.screen.onClose();
        }
    }

    private ClientPacketHandler() {}
}
