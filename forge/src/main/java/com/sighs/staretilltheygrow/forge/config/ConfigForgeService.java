package com.sighs.staretilltheygrow.forge.config;

import com.google.common.collect.Lists;
import com.sighs.staretilltheygrow.config.ConfigService;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public final class ConfigForgeService implements ConfigService {
    private final Forge forge;

    public ConfigForgeService() {
        Pair<Forge, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Forge::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, pair.getRight());
        this.forge = pair.getLeft();
    }

    public static void setup() { new ConfigForgeService(); }

    public boolean enableApplyBoneMeal() { return forge.enableApplyBoneMeal.get(); }
    public boolean enableFallInLove() { return forge.enableFallInLove.get(); }
    public boolean enableGrowUp() { return forge.enableGrowUp.get(); }
    public boolean enableRegrowCake() { return forge.enableRegrowCake.get(); }
    public boolean enableRegrowWool() { return forge.enableRegrowWool.get(); }
    public boolean enableTraderRestock() { return forge.enableTraderRestock.get(); }
    public int ticksDelay() { return forge.ticksDelay.get(); }
    public int ticksBetween() { return forge.ticksBetween.get(); }
    public boolean isBlockList() { return forge.isBlockList.get(); }
    public List<String> blockOrAllowList() { return (List<String>) forge.blockOrAllowList.get(); }

    private static class Forge {
        final ForgeConfigSpec.BooleanValue enableApplyBoneMeal;
        final ForgeConfigSpec.BooleanValue enableFallInLove;
        final ForgeConfigSpec.BooleanValue enableGrowUp;
        final ForgeConfigSpec.BooleanValue enableRegrowCake;
        final ForgeConfigSpec.BooleanValue enableRegrowWool;
        final ForgeConfigSpec.BooleanValue enableTraderRestock;
        final ForgeConfigSpec.IntValue ticksDelay;
        final ForgeConfigSpec.IntValue ticksBetween;
        final ForgeConfigSpec.BooleanValue isBlockList;
        final ForgeConfigSpec.ConfigValue<List<? extends String>> blockOrAllowList;

        Forge(ForgeConfigSpec.Builder b) {
            b.push("general");
            enableApplyBoneMeal = b.define("general.enableApplyBoneMeal", true);
            enableFallInLove = b.define("general.enableFallInLove", true);
            enableGrowUp = b.define("general.enableGrowUp", true);
            enableRegrowCake = b.define("general.enableRegrowCake", true);
            enableRegrowWool = b.define("general.enableRegrowWool", true);
            b.pop();
            enableTraderRestock = b.define("enableTraderRestock", false);
            b.push("timing");
            ticksDelay = b.defineInRange("timing.ticksDelay", 20, 1, Integer.MAX_VALUE);
            ticksBetween = b.defineInRange("timing.ticksBetween", 10, 1, Integer.MAX_VALUE);
            b.pop();
            b.push("filter");
            isBlockList = b.define("filter.isBlockList", true);
            blockOrAllowList = b.defineList("filter.blockOrAllowList",
                    Lists.newArrayList("minecraft:grass_block","minecraft:grass","minecraft:fern","minecraft:netherrack","minecraft:warped_nylium","minecraft:crimson_nylium"),
                    e -> e instanceof String);
            b.pop();
        }
    }
}