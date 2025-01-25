package com.flechazo.StareTillTheyGrowRb.Network;

import com.flechazo.StareTillTheyGrowRb.Network.Messages.RegisterBlockNetworkMessage;
import com.flechazo.StareTillTheyGrowRb.Network.Messages.RegisterEntityNetworkMessage;
import com.flechazo.StareTillTheyGrowRb.Network.Messages.UnregisterNetworkMessage;
import com.flechazo.StareTillTheyGrowRb.StareTillTheyGrowReborn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * 网络管理类，用于处理Mod中的网络通信。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class Network {
    // 协议版本常量
    private static final String PROTOCOL_VERSION = "1";

    // 创建网络实例
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            // 创建通道的研究位置
            new ResourceLocation(StareTillTheyGrowReborn.MOD_ID, "main"),
            // 返回协议版本
            () -> PROTOCOL_VERSION,
            // 客户端协议版本检查
            PROTOCOL_VERSION::equals,
            // 服务器协议版本检查
            PROTOCOL_VERSION::equals
    );

    /**
     * 初始化网络消息处理器。
     */
    public static void initialize() {
        int id = 0;
        INSTANCE.registerMessage(id++, RegisterBlockNetworkMessage.class, RegisterBlockNetworkMessage::encode, RegisterBlockNetworkMessage::decode, RegisterBlockNetworkMessage::handle);
        INSTANCE.registerMessage(id++, RegisterEntityNetworkMessage.class, RegisterEntityNetworkMessage::encode, RegisterEntityNetworkMessage::decode, RegisterEntityNetworkMessage::handle);
        INSTANCE.registerMessage(id++, UnregisterNetworkMessage.class, UnregisterNetworkMessage::encode, UnregisterNetworkMessage::decode, UnregisterNetworkMessage::handle);
    }

    /**
     * 发送消息给指定的玩家。
     *
     * @param player  目标玩家
     * @param message 要发送的消息对象
     */
    public static void sendToPlayer(ServerPlayer player, Object message) {
        INSTANCE.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    /**
     * 发送消息到服务器。
     *
     * @param message 要发送的消息对象
     */
    public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }
}