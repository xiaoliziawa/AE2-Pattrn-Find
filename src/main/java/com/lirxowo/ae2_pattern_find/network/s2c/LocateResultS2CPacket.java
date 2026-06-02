package com.lirxowo.ae2_pattern_find.network.s2c;

import com.lirxowo.ae2_pattern_find.client.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record LocateResultS2CPacket(
        long serverId,
        int slot,
        BlockPos pos,
        ResourceKey<Level> dimension,
        ItemStack pattern,
        ItemStack icon
) {

    public static void encode(LocateResultS2CPacket pkt, FriendlyByteBuf buf) {
        buf.writeLong(pkt.serverId);
        buf.writeVarInt(pkt.slot);
        buf.writeBlockPos(pkt.pos);
        buf.writeResourceLocation(pkt.dimension.location());
        buf.writeItem(pkt.pattern);
        buf.writeItem(pkt.icon);
    }

    public static LocateResultS2CPacket decode(FriendlyByteBuf buf) {
        long id = buf.readLong();
        int slot = buf.readVarInt();
        BlockPos pos = buf.readBlockPos();
        ResourceLocation dimRl = buf.readResourceLocation();
        ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, dimRl);
        ItemStack pattern = buf.readItem();
        ItemStack icon = buf.readItem();
        return new LocateResultS2CPacket(id, slot, pos, dim, pattern, icon);
    }

    public static void handle(LocateResultS2CPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> ClientPacketHandler.onLocateResult(pkt));
        context.setPacketHandled(true);
    }
}
