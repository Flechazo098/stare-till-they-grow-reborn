package com.flechazo.StareTillTheyGrowRb.Network.Messages;

import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 用于注册玩家正在查看的块位置的网络通信。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class RegisterBlockNetworkMessage {

    private final double x;
    private final double y;
    private final double z;

    /**
     * 创建具有特定坐标的新消息。
     *
     * @param x x 坐标
     * @param y y 坐标
     * @param z z 坐标
     */
    public RegisterBlockNetworkMessage(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 从 BlockPos 创建新消息。
     *
     * @param blockPos 区块位置
     */
    public RegisterBlockNetworkMessage(BlockPos blockPos) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }

    /**
     *从存储的坐标中获取方块位置。
     *
     * @return BlockPos
     */
    public BlockPos getBlockPos() {
        return new BlockPos((int) this.x, (int) this.y, (int) this.z);
    }

    /**
     * 将此消息编码到给定的缓冲区中。
     *
     * @param message 要编码的消息
     * @param buffer 要写入的缓冲区
     */
    public static void encode(RegisterBlockNetworkMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
    }

    /**
     * 从给定的缓冲区解码消息。
     *
     * @param buffer 要从中读取的缓冲区
     * @return 解码后的消息
     */
    public static RegisterBlockNetworkMessage decode(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new RegisterBlockNetworkMessage(x, y, z);
    }

    /**
     * 在接收方处理消息。
     *
     * @param message 要处理的消息
     * @param supplierContext 网络上下文
     */
    public static void handle(final RegisterBlockNetworkMessage message, final Supplier<NetworkEvent.Context> supplierContext) {
        NetworkEvent.Context context = supplierContext.get();

        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (null != player) {
                    PlayerTargetDictionary.registerBlock(
                            player,
                            (ServerLevel)player.level(),
                            message.getBlockPos()
                    );
                }
            });
        }

        supplierContext.get().setPacketHandled(true);
    }
}
