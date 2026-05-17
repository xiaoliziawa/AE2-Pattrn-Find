package com.lirxowo.ae2_pattern_find.client;

import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.gui.me.patternaccess.PatternSlot;
import com.lirxowo.ae2_pattern_find.client.render.LoadingProgressRenderer;
import com.lirxowo.ae2_pattern_find.mixin.client.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class PatternAccessTermHandler {

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof PatternAccessTermScreen<?> term)) return;

        HoldProgressTracker tracker = ClientSetup.tracker();
        Slot hovered = ((AbstractContainerScreenAccessor) screen).ae2pf$getHoveredSlot();

        if (hovered instanceof PatternSlot patternSlot
                && Screen.hasShiftDown()
                && Screen.hasAltDown()) {
            long id = patternSlot.getMachineInv().getServerId();
            tracker.update(true, id, patternSlot.getSlotIndex());
            tracker.addRenderPartial(event.getPartialTick());

            AbstractContainerScreen<?> acs = term;
            float cx = acs.getGuiLeft() + patternSlot.x + 8f;
            float cy = acs.getGuiTop() + patternSlot.y + 8f;
            LoadingProgressRenderer.draw(event.getGuiGraphics(), cx, cy, tracker.progress());
        } else {
            tracker.update(false, 0, 0);
        }
    }

    private PatternAccessTermHandler() {}
}
