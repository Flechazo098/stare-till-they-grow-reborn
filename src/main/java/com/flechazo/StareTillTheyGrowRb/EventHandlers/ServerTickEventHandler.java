package com.flechazo.StareTillTheyGrowRb.EventHandlers;

import com.flechazo.StareTillTheyGrowRb.Actions.Block.ApplyBoneMealAction;
import com.flechazo.StareTillTheyGrowRb.Actions.Block.RegrowCakeAction;
import com.flechazo.StareTillTheyGrowRb.Actions.Entity.FallInLoveAction;
import com.flechazo.StareTillTheyGrowRb.Actions.Entity.GrowUpAction;
import com.flechazo.StareTillTheyGrowRb.Actions.Entity.RegrowWoolAction;
import com.flechazo.StareTillTheyGrowRb.Actions.Entity.TraderRestockAction;
import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

/**
 * 服务器事件处理器，用于处理服务器端的每帧事件。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public final class ServerTickEventHandler {

    /**
     * 处理服务器端的每帧事件。
     * <p>
     * 当事件类型为服务器并且处于事件阶段结束时，遍历所有玩家目标字典中的条目。
     * 对每个玩家目标执行tick()方法，并在满足条件时调用相应的动作。
     *
     * @param event 服务器每帧事件
     */
    @SubscribeEvent
    public void serverTickHandler(TickEvent.ServerTickEvent event) {
        if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END) {
            // 遍历玩家目标字典中的每一个条目
            PlayerTargetDictionary.forEach((player, playerTarget) -> {
                // 更新玩家目标的内部计数器
                playerTarget.tick();
                // 检查是否可以触发动作
                if (playerTarget.canInvoke()) {
                    // 如果是方块目标，则执行相关方块的动作
                    if (playerTarget instanceof PlayerTargetDictionary.PlayerBlockTarget playerBlockTarget) {
                        new ApplyBoneMealAction(playerBlockTarget).invoke();
                        new RegrowCakeAction(playerBlockTarget).invoke();
                    }
                    // 如果是实体目标，则执行相关实体的动作
                    else if (playerTarget instanceof PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
                        new FallInLoveAction(playerEntityTarget).invoke();
                        new GrowUpAction(playerEntityTarget).invoke();
                        new RegrowWoolAction(playerEntityTarget).invoke();

                        // 获取 Villager 实例
                        Optional<AbstractVillager> optionalTrader = playerEntityTarget.getEntity();
                        optionalTrader.ifPresent(trader -> 
                            new TraderRestockAction((PlayerTargetDictionary.PlayerEntityTarget) playerTarget, trader, player).invoke()
                        );
                    }
                }
            });
        }
    }
}