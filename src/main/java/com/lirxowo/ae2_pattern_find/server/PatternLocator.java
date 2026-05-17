package com.lirxowo.ae2_pattern_find.server;

import appeng.helpers.patternprovider.PatternContainer;
import appeng.menu.implementations.PatternAccessTermMenu;
import com.lirxowo.ae2_pattern_find.mixin.ContainerTrackerAccessor;
import com.lirxowo.ae2_pattern_find.mixin.PatternAccessTermMenuAccessor;
import com.lirxowo.ae2_pattern_find.network.s2c.LocateResultS2CPacket;
import com.lirxowo.ae2_pattern_find.network.ModNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;

public final class PatternLocator {

    public static void handleRequest(ServerPlayer player, long serverId, int slot) {
        if (!(player.containerMenu instanceof PatternAccessTermMenu menu)) {
            return;
        }

        Object tracker = ((PatternAccessTermMenuAccessor) menu).ae2pf$getById().get(serverId);
        if (!(tracker instanceof ContainerTrackerAccessor accessor)) {
            return;
        }

        PatternContainer container = accessor.ae2pf$getContainer();
        if (container == null) {
            return;
        }

        BlockEntity be = PatternHostResolver.resolve(container);
        if (be == null) {
            sendChat(player, Component.translatable("message.ae2_pattern_find.unsupported_container")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        if (be.getLevel() == null) {
            return;
        }

        if (be.getLevel() != player.level()) {
            var pos = be.getBlockPos();
            String dimName = be.getLevel().dimension().location().toString();
            sendChat(player, Component.translatable("message.ae2_pattern_find.cross_dimension",
                            dimName, pos.getX(), pos.getY(), pos.getZ())
                    .withStyle(ChatFormatting.YELLOW));
            return;
        }

        ItemStack pattern = ItemStack.EMPTY;
        var inv = container.getTerminalPatternInventory();
        if (inv != null && slot >= 0 && slot < inv.size()) {
            pattern = inv.getStackInSlot(slot).copy();
        }

        ItemStack icon = ItemStack.EMPTY;
        var group = container.getTerminalGroup();
        if (group != null && group.icon() != null) {
            icon = group.icon().toStack();
        }

        var result = new LocateResultS2CPacket(
                serverId,
                slot,
                be.getBlockPos(),
                be.getLevel().dimension(),
                pattern,
                icon);

        ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), result);
    }

    private static void sendChat(ServerPlayer player, Component msg) {
        player.displayClientMessage(msg, true);
    }

    private PatternLocator() {}
}
