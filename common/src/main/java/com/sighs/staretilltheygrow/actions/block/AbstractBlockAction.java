package com.sighs.staretilltheygrow.actions.block;

import com.sighs.staretilltheygrow.actions.AbstractAction;
import com.sighs.staretilltheygrow.config.Config;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

abstract public class AbstractBlockAction extends AbstractAction {

    protected BlockPos blockPos;
    protected boolean isBlocked;

    public AbstractBlockAction(PlayerTargetDictionary.PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
        blockPos = playerBlockTarget.getTarget();
        isBlocked = getIsBlocked();

    }

    private boolean getIsBlocked() {
        BlockState blockState = dimension.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ResourceLocation tag = BuiltInRegistries.BLOCK.getKey(block);
        List<? extends String> blockOrAllowList = Config.get().blockOrAllowList();

        boolean isBlocked;
        boolean isBlocklist = Config.get().isBlockList();
        String[] filterList = Arrays.copyOf(blockOrAllowList.toArray(), blockOrAllowList.size(), String[].class);

        isBlocked = ArrayUtils.contains(filterList, tag.toString());
        if (!isBlocklist) {
            isBlocked = !isBlocked;
        }

        return isBlocked;
    }
}