package com.flechazo.StareTillTheyGrowRb.Actions.Entity;

import com.flechazo.StareTillTheyGrowRb.Actions.AbstractAction;
import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary.PlayerEntityTarget;
import net.minecraft.world.entity.Entity;

/**
 * 抽象实体操作类，用于处理与实体相关的动作。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
abstract public class AbstractEntityAction extends AbstractAction {

    /**
     * 目标实体。
     */
    protected Entity entity;

    /**
     * 构造函数，初始化抽象实体操作。
     *
     * @param playerEntityTarget 玩家的目标实体信息
     */
    public AbstractEntityAction(PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
        entity = playerEntityTarget.getTarget();

    }
}