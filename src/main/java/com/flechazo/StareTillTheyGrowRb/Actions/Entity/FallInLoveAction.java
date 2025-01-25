package com.flechazo.StareTillTheyGrowRb.Actions.Entity;

import com.flechazo.StareTillTheyGrowRb.Config.Config;
import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

/**
 * 让动物进入繁殖状态的操作类。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class FallInLoveAction extends AbstractEntityAction {

    /**
     * 构造函数，初始化让动物进入繁殖状态的操作。
     *
     * @param playerEntityTarget 玩家的目标实体信息
     */
    public FallInLoveAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }

    /**
     * 执行当前实体的动作。
     *
     * <p>该方法会检查配置是否启用，实体是否为成年动物，以及动物是否可以进入繁殖状态。</p>
     *
     * <p>如果满足条件，则让动物进入繁殖状态，并在实体位置发射粒子效果。</p>
     */
    @Override
    public void invoke() {
        if (
                isEnabledInConfig
                        && entity instanceof Animal animal
                        && animal.getAge() == 0
                        && animal.canFallInLove()
        ) {
            // 设置动物进入繁殖状态
            animal.setInLove(player);
            // 发射粒子效果
            emitParticles(entity.position());
            Vec3 position = entity.position();
            dimension.gameEvent(entity, GameEvent.ENTITY_INTERACT, new BlockPos((int) position.x, (int) position.y, (int) position.z));
        }
    }

    /**
     * 获取配置中是否启用了让动物进入繁殖状态的功能。
     *
     * @return 如果在配置中启用了让动物进入繁殖状态的功能，则返回 true；否则返回 false。
     */
    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.COMMON.enableFallInLove.get();
    }
}