package com.flechazo.StareTillTheyGrowRb.Actions.Block;

import com.flechazo.StareTillTheyGrowRb.Actions.AbstractAction;
import com.flechazo.StareTillTheyGrowRb.Config.Config;
import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 抽象块操作类，用于处理与方块相关的动作。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
abstract public class AbstractBlockAction extends AbstractAction {

    /**
     * 方块的位置。
     */
    protected BlockPos blockPos;

    /**
     * 标识该方块是否被阻止（取决于配置）。
     */
    protected boolean isBlocked;

    /**
     * 构造函数，初始化抽象块操作。
     *
     * @param playerBlockTarget 玩家的目标方块信息
     */
    public AbstractBlockAction(PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
        blockPos = playerBlockTarget.getTarget();
        isBlocked = getIsBlocked();

    }

    /**
     * 获取当前方块是否被阻止。
     *
     * <p>根据配置文件中的黑名单或白名单判断方块是否被阻止。</p>
     *
     * @return 如果方块在列表中且是黑名单模式，则返回 true；如果不在列表中且是白名单模式，也返回 true。
     */
    private boolean getIsBlocked() {
        BlockState blockState = dimension.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ResourceLocation tag = ForgeRegistries.BLOCKS.getKey(block);
        List<? extends String> blockOrAllowList = Config.COMMON.blockOrAllowList.get();

        boolean isBlocked = true;
        if (tag != null) {
            boolean isBlocklist = Config.COMMON.isBlockList.get();
            String[] filterList = Arrays.copyOf(blockOrAllowList.toArray(), blockOrAllowList.size(), String[].class);

            isBlocked = ArrayUtils.contains(filterList, tag.toString());
            if (!isBlocklist) {
                isBlocked = !isBlocked;
            }
        }

        return isBlocked;
    }
}