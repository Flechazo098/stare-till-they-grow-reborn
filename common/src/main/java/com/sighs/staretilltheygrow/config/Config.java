package com.sighs.staretilltheygrow.config;

import java.util.List;
import java.util.ServiceLoader;

public final class Config {
    private static final ConfigService INSTANCE = load();

    private static ConfigService load() {
        for (ConfigService s : ServiceLoader.load(ConfigService.class)) return s;
        return Defaults.INSTANCE;
    }

    public static ConfigService get() {
        return INSTANCE;
    }

    private enum Defaults implements ConfigService {
        INSTANCE;
        public boolean enableApplyBoneMeal() { return true; }
        public boolean enableFallInLove() { return true; }
        public boolean enableGrowUp() { return true; }
        public boolean enableRegrowCake() { return true; }
        public boolean enableRegrowWool() { return true; }
        public boolean enableTraderRestock() { return false; }
        public int ticksDelay() { return 20; }
        public int ticksBetween() { return 10; }
        public boolean isBlockList() { return true; }
        public List<String> blockOrAllowList() {
            return List.of(
                    "minecraft:grass_block",
                    "minecraft:grass",
                    "minecraft:fern",
                    "minecraft:netherrack",
                    "minecraft:warped_nylium",
                    "minecraft:crimson_nylium"
            );
        }
    }
}