package com.lirxowo.ae2_pattern_find;

import com.lirxowo.ae2_pattern_find.client.ClientSetup;
import com.lirxowo.ae2_pattern_find.client.PatternAccessTermHandler;
import com.lirxowo.ae2_pattern_find.client.render.ArrowOverlayRenderer;
import com.lirxowo.ae2_pattern_find.client.render.MarkerRenderer;
import com.lirxowo.ae2_pattern_find.network.ModNetwork;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(AE2PatternFind.MODID)
public class AE2PatternFind {

    public static final String MODID = "ae2_pattern_find";

    public AE2PatternFind(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC, MODID + "-common.toml");

        modBus.addListener(this::onCommonSetup);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(ClientSetup::onClientSetup);
            MinecraftForge.EVENT_BUS.register(ClientSetup.class);
            MinecraftForge.EVENT_BUS.register(PatternAccessTermHandler.class);
            MinecraftForge.EVENT_BUS.register(MarkerRenderer.class);
            MinecraftForge.EVENT_BUS.register(ArrowOverlayRenderer.class);
        }
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }
}
