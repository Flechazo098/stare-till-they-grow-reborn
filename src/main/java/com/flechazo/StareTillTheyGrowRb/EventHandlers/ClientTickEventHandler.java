package com.flechazo.StareTillTheyGrowRb.EventHandlers;

import com.flechazo.StareTillTheyGrowRb.Network.Messages.RegisterBlockNetworkMessage;
import com.flechazo.StareTillTheyGrowRb.Network.Messages.RegisterEntityNetworkMessage;
import com.flechazo.StareTillTheyGrowRb.Network.Messages.UnregisterNetworkMessage;
import com.flechazo.StareTillTheyGrowRb.Network.Network;
import com.flechazo.StareTillTheyGrowRb.StareTillTheyGrowReborn;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

/**
 * 客户端事件处理器，用于处理客户端的每帧事件。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = StareTillTheyGrowReborn.MOD_ID, value = Dist.CLIENT)
public final class ClientTickEventHandler {
    /**
     * 当前玩家视线中的方块位置。
     */
    private static @Nullable BlockPos currentBlockPos = null;

    /**
     * 当前玩家视线中的实体UUID。
     */
    private static @Nullable UUID currentEntityUuid = null;

    /**
     * 处理客户端的每帧事件。
     *
     * @param event 客户端每帧事件
     */
    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        // 只在世界中或连接到服务器时运行
        if (null == minecraft.level) return;

        if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
            registerBlockHitResult(blockHitResult);
        } else if (minecraft.hitResult instanceof EntityHitResult entityHitResult) {
            registerEntityHitResult(entityHitResult);
        } else {
            unregisterHitResults();
        }
    }

    /**
     * 注册玩家视线中方块的结果。
     *
     * @param blockHitResult 方块命中结果
     */
    private static void registerBlockHitResult(BlockHitResult blockHitResult) {
        BlockPos newBlockPos = blockHitResult.getBlockPos();
        if (null == currentBlockPos || !currentBlockPos.equals(newBlockPos)) {
            currentBlockPos = newBlockPos;
            currentEntityUuid = null;
            Network.sendToServer(new RegisterBlockNetworkMessage(newBlockPos));
        }
    }

    /**
     * 注册玩家视线中实体的结果。
     *
     * @param entityHitResult 实体命中结果
     */
    private static void registerEntityHitResult(EntityHitResult entityHitResult) {
        UUID newEntityUuid = entityHitResult.getEntity().getUUID();
        if (null == currentEntityUuid || !currentEntityUuid.equals(newEntityUuid)) {
            currentEntityUuid = newEntityUuid;
            currentBlockPos = null;
            Network.sendToServer(new RegisterEntityNetworkMessage(newEntityUuid));
        }
    }

    /**
     * 取消注册当前的命中结果。
     */
    private static void unregisterHitResults() {
        currentBlockPos = null;
        currentEntityUuid = null;
        Network.sendToServer(new UnregisterNetworkMessage());
    }
}