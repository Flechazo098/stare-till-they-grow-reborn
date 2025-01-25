package com.flechazo.StareTillTheyGrowRb.Actions.Block;

import com.flechazo.StareTillTheyGrowRb.Config.Config;
import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 蛋糕再生动作类，用于处理蛋糕方块的再生操作。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class RegrowCakeAction extends AbstractBlockAction {

    /**
     * 构造函数，初始化蛋糕再生动作。
     *
     * @param playerBlockTarget 玩家的目标方块信息
     */
    public RegrowCakeAction(PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
    }

    /**
     * 执行当前方块的动作。
     *
     * <p>该方法会检查配置是否启用，方块是否被阻止，以及方块是否为蛋糕类型。</p>
     *
     * <p>如果满足条件，则减少蛋糕的咬痕数，并在方块位置发射粒子效果。</p>
     */
    @Override
    public void invoke() {
        BlockState blockState = dimension.getBlockState(blockPos);
        if (
                isEnabledInConfig
                        && !isBlocked
                        && blockState.getBlock() instanceof CakeBlock
        ) {
            // 获取当前蛋糕的咬痕数量
            int bites = blockState.getValue(CakeBlock.BITES);
            if (bites > 0) {
                // 减少蛋糕的咬痕数
                dimension.setBlock(blockPos, blockState.setValue(CakeBlock.BITES, bites - 1), 3);
                // 发射粒子效果
                emitParticles(blockPos);
            }
        }
    }

    /**
     * 获取配置中是否启用了蛋糕再生功能。
     *
     * @return 如果在配置中启用了蛋糕再生功能，则返回 true；否则返回 false。
     */
    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.COMMON.enableRegrowCake.get();
    }
}