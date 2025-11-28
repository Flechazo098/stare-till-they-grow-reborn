package com.sighs.staretilltheygrow.actions.block;

import com.sighs.staretilltheygrow.config.Config;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RegrowCakeAction extends AbstractBlockAction {

    public RegrowCakeAction(PlayerTargetDictionary.PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
    }

    @Override
    public void invoke() {
        BlockState blockState = dimension.getBlockState(blockPos);
        if (
                isEnabledInConfig
                        && !isBlocked
                        && blockState.getBlock() instanceof CakeBlock
        ) {
            int bites = blockState.getValue(CakeBlock.BITES);
            if (bites > 0) {
                dimension.setBlock(blockPos, blockState.setValue(CakeBlock.BITES, bites - 1), 3);
                emitParticles(blockPos);
            }
        }
    }

    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.get().enableRegrowCake();
    }
}