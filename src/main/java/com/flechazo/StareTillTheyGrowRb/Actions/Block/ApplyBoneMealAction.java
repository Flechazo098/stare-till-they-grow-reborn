package com.flechazo.StareTillTheyGrowRb.Actions.Block;

import com.flechazo.StareTillTheyGrowRb.Config.Config;
import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 应用骨粉动作类，用于处理对特定方块应用骨粉的操作。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class ApplyBoneMealAction extends AbstractBlockAction {

    /**
     * 构造函数，初始化应用骨粉动作。
     *
     * @param playerBlockTarget 玩家的目标方块信息
     */
    public ApplyBoneMealAction (PlayerBlockTarget playerBlockTarget) {
        super (playerBlockTarget);
    }

    /**
     * 执行当前方块的动作。
     *
     * <p>该方法会检查配置是否启用，方块是否被阻止，以及方块是否支持骨粉操作。</p>
     *
     * @see #getIsEnabledInConfig()
     * @see #isBlocked
     */
    @Override
    public void invoke () {

        BlockState blockState = dimension.getBlockState (blockPos);
        if (
                        isEnabledInConfig
                        && ! isBlocked
                        && blockState.getBlock () instanceof BonemealableBlock bonemealableBlock
                        && bonemealableBlock.isValidBonemealTarget (dimension, blockPos, blockState, dimension.isClientSide ())
        ) {
            bonemealableBlock.performBonemeal (dimension, dimension.random, blockPos, blockState);
            emitParticles (blockPos);
        }
    }

    /**
     * 获取配置中是否启用了应用骨粉功能。
     *
     * @return 如果在配置中启用了应用骨粉功能，则返回 true；否则返回 false。
     */
    @Override
    protected boolean getIsEnabledInConfig () {
        return Config.COMMON.enableApplyBoneMeal.get ();
    }
}