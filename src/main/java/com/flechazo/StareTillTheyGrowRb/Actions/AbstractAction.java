package com.flechazo.StareTillTheyGrowRb.Actions;

import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary.PlayerTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * 抽象动作类，实现了基本的动作接口和功能。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
abstract public class AbstractAction implements ActionInterface {

    /**
     * 游戏维度（服务器级别）。
     */
    protected ServerLevel dimension;

    /**
     * 玩家对象。
     */
    protected Player player;

    /**
     * 配置中是否启用该动作的标志。
     */
    protected boolean isEnabledInConfig;

    /**
     * 构造函数，初始化抽象动作。
     *
     * @param playerTarget 玩家的目标信息
     */
    public AbstractAction(PlayerTarget playerTarget) {
        dimension = playerTarget.getDimension();
        player = playerTarget.getPlayer();
        isEnabledInConfig = getIsEnabledInConfig();
    }

    /**
     * 获取配置中是否启用了当前动作的功能。
     *
     * @return 如果在配置中启用了当前动作的功能，则返回 true；否则返回 false。
     */
    protected abstract boolean getIsEnabledInConfig();

    /**
     * 发射粒子效果，基于方块位置。
     *
     * @param pos 方块的位置
     */
    protected void emitParticles(BlockPos pos) {
        emitParticles(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * 发射粒子效果，基于向量位置。
     *
     * @param pos 向量位置
     */
    protected void emitParticles(Vec3 pos) {
        emitParticles(pos.x(), pos.y(), pos.z());
    }

    /**
     * 发射粒子效果，基于具体的坐标值。
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @param z Z 坐标
     */
    protected void emitParticles(double x, double y, double z) {
        dimension.sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                x + 0.5,
                y + 0.5,
                z + 0.5,
                75,
                1,
                1,
                1,
                1);
    }
}