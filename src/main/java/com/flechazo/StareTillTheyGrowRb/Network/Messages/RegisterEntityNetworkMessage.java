package com.flechazo.StareTillTheyGrowRb.Network.Messages;

import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 网络通信，用于注册玩家视线中的实体。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class RegisterEntityNetworkMessage {
    private final UUID entityUuid;

    /**
     * 使用给定的实体创建一个新的消息实例。
     *
     * @param entity 要注册的实体
     */
    public RegisterEntityNetworkMessage(Entity entity) {
        this.entityUuid = entity.getUUID();
    }

    /**
     * 使用给定的实体UUID创建一个新的消息实例。
     *
     * @param entityUuid 要注册的实体的UUID
     */
    public RegisterEntityNetworkMessage(UUID entityUuid) {
        this.entityUuid = entityUuid;
    }

    /**
     * 将此消息编码到给定的缓冲区中。
     *
     * @param message 消息实例
     * @param buffer 用于写入数据的缓冲区
     */
    public static void encode(RegisterEntityNetworkMessage message, FriendlyByteBuf buffer) {
        buffer.writeUUID(message.entityUuid);
    }

    /**
     * 从给定的缓冲区解码消息。
     *
     * @param buffer 用于读取数据的缓冲区
     * @return 解码后的消息实例
     */
    public static RegisterEntityNetworkMessage decode(FriendlyByteBuf buffer) {
        UUID uuid = buffer.readUUID();
        return new RegisterEntityNetworkMessage(uuid);
    }

    /**
     * 获取消息中的实体UUID。
     *
     * @return 实体的UUID
     */
    public UUID getEntityUuid() {
        return entityUuid;
    }

    /**
     * 在接收端处理消息。
     *
     * @param message 消息实例
     * @param supplierContext 网络上下文提供者
     */
    public static void handle(final RegisterEntityNetworkMessage message, final Supplier<NetworkEvent.Context> supplierContext) {
        NetworkEvent.Context context = supplierContext.get();

        // 如果是在服务器端接收到的消息
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (null != player) {
                    // 注册玩家视线中的实体
                    PlayerTargetDictionary.registerEntity(
                            player,
                            (ServerLevel) player.level(),
                            message.getEntityUuid()
                    );
                }
            });
        }

        // 标记消息已处理
        context.setPacketHandled(true);
    }
}