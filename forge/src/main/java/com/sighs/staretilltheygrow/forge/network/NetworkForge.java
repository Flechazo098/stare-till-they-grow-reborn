package com.sighs.staretilltheygrow.forge.network;

import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import com.sighs.staretilltheygrow.network.messages.RegisterBlockPayload;
import com.sighs.staretilltheygrow.network.messages.RegisterEntityPayload;
import com.sighs.staretilltheygrow.network.messages.UnregisterPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkForge {
    public static final String MOD_ID = "staretilltheygrow";
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void initialize() {
        int id = 0;
        CHANNEL.registerMessage(id++, RegisterBlockPayload.class, RegisterBlockPayload::encode, RegisterBlockPayload::decode,
                (m, ctxSup) -> {
                    var ctx = ctxSup.get();
                    if (ctx.getDirection().getReceptionSide().isServer()) {
                        ctx.enqueueWork(() -> {
                            ServerPlayer p = ctx.getSender();
                            if (p != null) PlayerTargetDictionary.registerBlock(p, (ServerLevel) p.level(), m.toBlockPos());
                        });
                    }
                    ctx.setPacketHandled(true);
                });
        CHANNEL.registerMessage(id++, RegisterEntityPayload.class, RegisterEntityPayload::encode, RegisterEntityPayload::decode,
                (m, ctxSup) -> {
                    var ctx = ctxSup.get();
                    if (ctx.getDirection().getReceptionSide().isServer()) {
                        ctx.enqueueWork(() -> {
                            ServerPlayer p = ctx.getSender();
                            if (p != null) PlayerTargetDictionary.registerEntity(p, (ServerLevel) p.level(), m.uuid());
                        });
                    }
                    ctx.setPacketHandled(true);
                });
        CHANNEL.registerMessage(id++, UnregisterPayload.class, UnregisterPayload::encode, UnregisterPayload::decode,
                (m, ctxSup) -> {
                    var ctx = ctxSup.get();
                    if (ctx.getDirection().getReceptionSide().isServer()) {
                        ctx.enqueueWork(() -> {
                            ServerPlayer p = ctx.getSender();
                            if (p != null) PlayerTargetDictionary.unregister(p);
                        });
                    }
                    ctx.setPacketHandled(true);
                });
    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    public static void sendToPlayer(ServerPlayer player, Object message) {
        CHANNEL.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}