package com.flechazo.StareTillTheyGrowRb;

import com.flechazo.StareTillTheyGrowRb.Config.Config;
import com.flechazo.StareTillTheyGrowRb.EventHandlers.PlayerLeaveEventHandler;
import com.flechazo.StareTillTheyGrowRb.EventHandlers.ServerTickEventHandler;
import com.flechazo.StareTillTheyGrowRb.EventHandlers.TraderOpenHandler;
import com.flechazo.StareTillTheyGrowRb.Network.Network;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StareTillTheyGrow - 一个使作物在玩家注视时生长更快的Mod。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
@Mod(StareTillTheyGrowReborn.MOD_ID)
public class StareTillTheyGrowReborn {

    public static final String MOD_ID = "staretilltheygrow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Mod主类的构造函数。
     * 初始化配置、事件处理器和网络组件。
     */
    public StareTillTheyGrowReborn() {
        LOGGER.info("初始化StareTillTheyGrow Mod");

        // 注册配置文件
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);

        // 获取事件总线
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        // 注册事件处理器
        forgeEventBus.register(new ServerTickEventHandler());
        forgeEventBus.register(new PlayerLeaveEventHandler());
        forgeEventBus.register(new TraderOpenHandler());

        // 设置网络组件
        modEventBus.addListener(this::onCommonSetup);

        LOGGER.info("StareTillTheyGrow Mod 初始化成功");
    }

    /**
     * 公共设置事件处理器
     * 初始化网络组件
     *
     * @param event 公共设置事件
     */
    private void onCommonSetup(final FMLCommonSetupEvent event) {
        LOGGER.debug("初始化网络处理器");
        event.enqueueWork(Network::initialize);
    }
}