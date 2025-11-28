package com.sighs.staretilltheygrow.config;

import java.util.List;

public interface ConfigService {
    boolean enableApplyBoneMeal();
    boolean enableFallInLove();
    boolean enableGrowUp();
    boolean enableRegrowCake();
    boolean enableRegrowWool();
    boolean enableTraderRestock();
    int ticksDelay();
    int ticksBetween();
    boolean isBlockList();
    List<String> blockOrAllowList();
}
