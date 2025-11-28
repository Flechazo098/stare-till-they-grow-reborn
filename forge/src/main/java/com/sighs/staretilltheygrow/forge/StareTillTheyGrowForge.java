package com.sighs.staretilltheygrow.forge;

import com.sighs.staretilltheygrow.forge.config.ConfigForgeService;
import com.sighs.staretilltheygrow.forge.network.NetworkForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(StareTillTheyGrowForge.MOD_ID)
public class StareTillTheyGrowForge {
    public static final String MOD_ID = "staretilltheygrow";

    public StareTillTheyGrowForge() {
        ConfigForgeService.setup();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkForge::initialize);
    }
}