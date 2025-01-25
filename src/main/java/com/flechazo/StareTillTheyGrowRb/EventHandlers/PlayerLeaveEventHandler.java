package com.flechazo.StareTillTheyGrowRb.EventHandlers;

import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 玩家离开事件处理器，用于处理玩家离开世界时的事件。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class PlayerLeaveEventHandler {

    /**
     * 处理玩家离开世界的事件。
     * <p>
     * 当玩家离开当前维度或服务器时，取消注册该玩家的目标信息。
     *
     * @param event 玩家离开事件
     */
    @SubscribeEvent
    public void playerLeftHandler(EntityLeaveLevelEvent event) {
        // 检查事件实体是否为玩家
        if (event.getEntity() instanceof Player player) {
            // 取消注册玩家的目标信息
            PlayerTargetDictionary.unregister(player);
        }
    }
}