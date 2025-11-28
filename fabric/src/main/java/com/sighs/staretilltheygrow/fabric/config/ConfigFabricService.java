package com.sighs.staretilltheygrow.fabric.config;

import com.sighs.staretilltheygrow.config.ConfigService;
import com.sighs.staretilltheygrow.fabric.StareTillTheyGrowFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

import java.util.Arrays;
import java.util.List;

@Config(name = StareTillTheyGrowFabric.MOD_ID)
public final class ConfigFabricService implements ConfigService, ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enableApplyBoneMeal = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableFallInLove = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableGrowUp = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableRegrowCake = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableRegrowWool = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableTraderRestock = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.BoundedDiscrete(min = 0, max = 1200)
    public int ticksDelay = 20;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1200)
    public int ticksBetween = 10;

    @ConfigEntry.Gui.Tooltip
    public boolean isBlockList = true;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public List<String> blockOrAllowList = Arrays.asList(
            "minecraft:grass_block",
            "minecraft:grass",
            "minecraft:fern",
            "minecraft:netherrack",
            "minecraft:warped_nylium",
            "minecraft:crimson_nylium"
    );

    @Override
    public boolean enableApplyBoneMeal() {
        return get().enableApplyBoneMeal;
    }

    @Override
    public boolean enableFallInLove() {
        return get().enableFallInLove;
    }

    @Override
    public boolean enableGrowUp() {
        return get().enableGrowUp;
    }

    @Override
    public boolean enableRegrowCake() {
        return get().enableRegrowCake;
    }

    @Override
    public boolean enableRegrowWool() {
        return get().enableRegrowWool;
    }

    @Override
    public boolean enableTraderRestock() {
        return get().enableTraderRestock;
    }

    @Override
    public int ticksDelay() {
        return get().ticksDelay;
    }

    @Override
    public int ticksBetween() {
        return get().ticksBetween;
    }

    @Override
    public boolean isBlockList() {
        return get().isBlockList;
    }

    @Override
    public List<String> blockOrAllowList() {
        return get().blockOrAllowList;
    }

    public static void register() {
        AutoConfig.register(ConfigFabricService.class, JanksonConfigSerializer::new);
    }

    public static ConfigFabricService get() {
        return AutoConfig.getConfigHolder(ConfigFabricService.class).getConfig();
    }
}