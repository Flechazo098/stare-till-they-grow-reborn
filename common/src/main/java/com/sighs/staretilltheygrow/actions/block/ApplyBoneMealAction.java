package com.sighs.staretilltheygrow.actions.block;

import com.sighs.staretilltheygrow.config.Config;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ApplyBoneMealAction extends AbstractBlockAction {

    public ApplyBoneMealAction (PlayerTargetDictionary.PlayerBlockTarget playerBlockTarget) {
        super (playerBlockTarget);
    }

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

    @Override
    protected boolean getIsEnabledInConfig () {
        return Config.get().enableApplyBoneMeal();
    }
}