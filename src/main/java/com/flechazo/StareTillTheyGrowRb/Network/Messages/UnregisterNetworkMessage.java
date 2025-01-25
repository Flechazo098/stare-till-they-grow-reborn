package com.flechazo.StareTillTheyGrowRb.Network.Messages;

import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 网络通信，用于取消注册玩家的目标。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class UnregisterNetworkMessage {

    /**
     * 默认构造函数。
     */
    public UnregisterNetworkMessage () {}

    /**
     * 编码此消息到给定的缓冲区中。
     * <p>
     * 注意：由于本消息不携带数据，因此此方法为空实现。
     *
     * @param message 消息实例
     * @param buffer 用于写入数据的缓冲区
     */
    public static void encode(UnregisterNetworkMessage message, FriendlyByteBuf buffer) {}

    /**
     * 从给定的缓冲区解码消息。
     * <p>
     * 注意：由于本消息不携带数据，因此直接返回一个新的消息实例。
     *
     * @param buffer 用于读取数据的缓冲区
     * @return 解码后的消息实例
     */
    public static UnregisterNetworkMessage decode(FriendlyByteBuf buffer) {
        return new UnregisterNetworkMessage();
    }

    /**
     * 在接收端处理消息。
     * <p>
     * 当在服务器端接收到此消息时，取消注册发送者的玩家目标。
     *
     * @param message 消息实例
     * @param supplierContext 网络上下文提供者
     */
    public static void handle(final UnregisterNetworkMessage message, final Supplier<NetworkEvent.Context> supplierContext) {
        NetworkEvent.Context context = supplierContext.get();

        // 如果是在服务器端接收到的消息
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (null != player) {
                    // 取消注册玩家的目标
                    PlayerTargetDictionary.unregister(player);
                }
            });
        }

        // 标记消息已处理
        context.setPacketHandled(true);
    }
}