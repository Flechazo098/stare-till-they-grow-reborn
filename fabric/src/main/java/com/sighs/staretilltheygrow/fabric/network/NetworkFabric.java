package com.sighs.staretilltheygrow.fabric.network;

import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import com.sighs.staretilltheygrow.network.messages.RegisterBlockPayload;
import com.sighs.staretilltheygrow.network.messages.RegisterEntityPayload;
import com.sighs.staretilltheygrow.network.messages.UnregisterPayload;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public final class NetworkFabric {
    public static final String MOD_ID = "staretilltheygrow";
    public static final ResourceLocation REG_BLOCK = new ResourceLocation(MOD_ID, "register_block");
    public static final ResourceLocation REG_ENTITY = new ResourceLocation(MOD_ID, "register_entity");
    public static final ResourceLocation UNREG = new ResourceLocation(MOD_ID, "unregister");

    public static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(REG_BLOCK, (server, player, handler, buf, responseSender) -> {
            RegisterBlockPayload m = RegisterBlockPayload.decode(new FriendlyByteBuf(buf));
            server.execute(() -> PlayerTargetDictionary.registerBlock(player, (ServerLevel) player.level(), m.toBlockPos()));
        });
        ServerPlayNetworking.registerGlobalReceiver(REG_ENTITY, (server, player, handler, buf, responseSender) -> {
            RegisterEntityPayload m = RegisterEntityPayload.decode(new FriendlyByteBuf(buf));
            server.execute(() -> PlayerTargetDictionary.registerEntity(player, (ServerLevel) player.level(), m.uuid()));
        });
        ServerPlayNetworking.registerGlobalReceiver(UNREG, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> PlayerTargetDictionary.unregister(player));
        });
    }

    public static void sendRegisterBlock(BlockPos pos) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        RegisterBlockPayload.encode(new RegisterBlockPayload(pos), buf);
        ClientPlayNetworking.send(REG_BLOCK, buf);
    }

    public static void sendRegisterEntity(java.util.UUID uuid) {
        FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        RegisterEntityPayload.encode(new RegisterEntityPayload(uuid), buf);
        ClientPlayNetworking.send(REG_ENTITY, buf);
    }

    public static void sendUnregister() {
        FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        UnregisterPayload.encode(new UnregisterPayload(), buf);
        ClientPlayNetworking.send(UNREG, buf);
    }
}
