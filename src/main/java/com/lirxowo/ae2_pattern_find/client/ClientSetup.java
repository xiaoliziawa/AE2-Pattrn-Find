package com.lirxowo.ae2_pattern_find.client;

import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import com.lirxowo.ae2_pattern_find.network.ModNetwork;
import com.lirxowo.ae2_pattern_find.network.c2s.LocatePatternC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientSetup {

    private static final HoldProgressTracker TRACKER = new HoldProgressTracker();

    public static HoldProgressTracker tracker() {
        return TRACKER;
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        MarkerStore.tick();

        if (!(Minecraft.getInstance().screen instanceof PatternAccessTermScreen<?>)) {
            TRACKER.reset();
            return;
        }

        TRACKER.onClientTick();
        if (TRACKER.isReady()) {
            long id = TRACKER.currentServerId();
            int slot = TRACKER.currentSlot();
            TRACKER.consume();
            ModNetwork.CHANNEL.sendToServer(new LocatePatternC2SPacket(id, slot));
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            MarkerStore.clear();
            TRACKER.reset();
        }
    }

    private ClientSetup() {}
}
