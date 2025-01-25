package com.flechazo.StareTillTheyGrowRb.Actions.Entity;

import com.flechazo.StareTillTheyGrowRb.Config.Config;
import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.animal.Sheep;

/**
 * 让绵羊重新长出羊毛的操作类。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class RegrowWoolAction extends AbstractEntityAction {

    /**
     * 构造函数，初始化让绵羊重新长出羊毛的操作。
     *
     * @param playerEntityTarget 玩家的目标实体信息
     */
    public RegrowWoolAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }

    /**
     * 执行当前实体的动作。
     *
     * <p>该方法会检查配置是否启用，实体是否为绵羊且已被剪毛。</p>
     *
     * <p>如果满足条件，则使绵羊重新长出羊毛，并在实体位置发射粒子效果。</p>
     */
    @Override
    public void invoke() {
        if (
                isEnabledInConfig
                        && entity instanceof Sheep sheep
                        && sheep.isSheared()
        ) {
            // 使绵羊重新长出羊毛
            sheep.setSheared(false);
            // 发射粒子效果
            emitParticles(entity.position());
        }
    }

    /**
     * 获取配置中是否启用了让绵羊重新长出羊毛的功能。
     *
     * @return 如果在配置中启用了让绵羊重新长出羊毛的功能，则返回 true；否则返回 false。
     */
    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.COMMON.enableRegrowWool.get();
    }
}